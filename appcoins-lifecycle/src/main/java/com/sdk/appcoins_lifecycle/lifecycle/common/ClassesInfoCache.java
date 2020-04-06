package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import com.sdk.appcoins_lifecycle.lifecycle.LifecycleOwner;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reflection is expensive, so we cache information about methods
 * for {@link ReflectiveGenericLifecycleObserver}, so it can call them,
 * and for {@link Lifecycling} to determine which observer adapter to use.
 */
class ClassesInfoCache {

  private static final int CALL_TYPE_NO_ARG = 0;
  private static final int CALL_TYPE_PROVIDER = 1;
  private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
  static ClassesInfoCache sInstance = new ClassesInfoCache();
  private final Map<Class, CallbackInfo> mCallbackMap = new HashMap<>();
  private final Map<Class, Boolean> mHasLifecycleMethods = new HashMap<>();

  boolean hasLifecycleMethods(Class klass) {
    Boolean hasLifecycleMethods = mHasLifecycleMethods.get(klass);
    if (hasLifecycleMethods != null) {
      return hasLifecycleMethods;
    }

    Method[] methods = getDeclaredMethods(klass);
    for (Method method : methods) {
      OnLifecycleEvent annotation = method.getAnnotation(OnLifecycleEvent.class);
      if (annotation != null) {
        // Optimization for reflection, we know that this method is called
        // when there is no generated adapter. But there are methods with @OnLifecycleEvent
        // so we know that will use ReflectiveGenericLifecycleObserver,
        // so we createInfo in advance.
        // CreateInfo always initialize mHasLifecycleMethods for a class, so we don't do it
        // here.
        createInfo(klass, methods);
        return true;
      }
    }
    mHasLifecycleMethods.put(klass, false);
    return false;
  }

  private Method[] getDeclaredMethods(Class klass) {
    try {
      return klass.getDeclaredMethods();
    } catch (NoClassDefFoundError e) {
      throw new IllegalArgumentException("The observer class has some methods that use "
          + "newer APIs which are not available in the current OS version. Lifecycles "
          + "cannot access even other methods so you should make sure that your "
          + "observer classes only access framework classes that are available "
          + "in your min API level OR use lifecycle:compiler annotation processor.", e);
    }
  }

  CallbackInfo getInfo(Class klass) {
    CallbackInfo existing = mCallbackMap.get(klass);
    if (existing != null) {
      return existing;
    }
    existing = createInfo(klass, null);
    return existing;
  }

  private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> handlers,
      MethodReference newHandler, Lifecycle.Event newEvent, Class klass) {
    Lifecycle.Event event = handlers.get(newHandler);
    if (event != null && newEvent != event) {
      Method method = newHandler.mMethod;
      throw new IllegalArgumentException("Method "
          + method.getName()
          + " in "
          + klass.getName()
          + " already declared with different @OnLifecycleEvent value: previous"
          + " value "
          + event
          + ", new value "
          + newEvent);
    }
    if (event == null) {
      handlers.put(newHandler, newEvent);
    }
  }

  private CallbackInfo createInfo(Class klass, Method[] declaredMethods) {
    Class superclass = klass.getSuperclass();
    Map<MethodReference, Lifecycle.Event> handlerToEvent = new HashMap<>();
    if (superclass != null) {
      CallbackInfo superInfo = getInfo(superclass);
      if (superInfo != null) {
        handlerToEvent.putAll(superInfo.mHandlerToEvent);
      }
    }

    Class[] interfaces = klass.getInterfaces();
    for (Class intrfc : interfaces) {
      for (Map.Entry<MethodReference, Lifecycle.Event> entry : getInfo(
          intrfc).mHandlerToEvent.entrySet()) {
        verifyAndPutHandler(handlerToEvent, entry.getKey(), entry.getValue(), klass);
      }
    }

    Method[] methods = declaredMethods != null ? declaredMethods : getDeclaredMethods(klass);
    boolean hasLifecycleMethods = false;
    for (Method method : methods) {
      OnLifecycleEvent annotation = method.getAnnotation(OnLifecycleEvent.class);
      if (annotation == null) {
        continue;
      }
      hasLifecycleMethods = true;
      Class<?>[] params = method.getParameterTypes();
      int callType = CALL_TYPE_NO_ARG;
      if (params.length > 0) {
        callType = CALL_TYPE_PROVIDER;
        if (!params[0].isAssignableFrom(LifecycleOwner.class)) {
          throw new IllegalArgumentException(
              "invalid parameter type. Must be one and instanceof LifecycleOwner");
        }
      }
      Lifecycle.Event event = annotation.value();

      if (params.length > 1) {
        callType = CALL_TYPE_PROVIDER_WITH_EVENT;
        if (!params[1].isAssignableFrom(Lifecycle.Event.class)) {
          throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
        }
        if (event != Lifecycle.Event.ON_ANY) {
          throw new IllegalArgumentException("Second arg is supported only for ON_ANY value");
        }
      }
      if (params.length > 2) {
        throw new IllegalArgumentException("cannot have more than 2 params");
      }
      MethodReference methodReference = new MethodReference(callType, method);
      verifyAndPutHandler(handlerToEvent, methodReference, event, klass);
    }
    CallbackInfo info = new CallbackInfo(handlerToEvent);
    mCallbackMap.put(klass, info);
    mHasLifecycleMethods.put(klass, hasLifecycleMethods);
    return info;
  }

  @SuppressWarnings("WeakerAccess") static class CallbackInfo {
    final Map<Lifecycle.Event, List<MethodReference>> mEventToHandlers;
    final Map<MethodReference, Lifecycle.Event> mHandlerToEvent;

    CallbackInfo(Map<MethodReference, Lifecycle.Event> handlerToEvent) {
      mHandlerToEvent = handlerToEvent;
      mEventToHandlers = new HashMap<>();
      for (Map.Entry<MethodReference, Lifecycle.Event> entry : handlerToEvent.entrySet()) {
        Lifecycle.Event event = entry.getValue();
        List<MethodReference> methodReferences = mEventToHandlers.get(event);
        if (methodReferences == null) {
          methodReferences = new ArrayList<>();
          mEventToHandlers.put(event, methodReferences);
        }
        methodReferences.add(entry.getKey());
      }
    }

    private static void invokeMethodsForEvent(List<MethodReference> handlers, LifecycleOwner source,
        Lifecycle.Event event, Object mWrapped) {
      if (handlers != null) {
        for (int i = handlers.size() - 1; i >= 0; i--) {
          handlers.get(i)
              .invokeCallback(source, event, mWrapped);
        }
      }
    }

    @SuppressWarnings("ConstantConditions") void invokeCallbacks(LifecycleOwner source,
        Lifecycle.Event event, Object target) {
      invokeMethodsForEvent(mEventToHandlers.get(event), source, event, target);
      invokeMethodsForEvent(mEventToHandlers.get(Lifecycle.Event.ON_ANY), source, event, target);
    }
  }

  @SuppressWarnings("WeakerAccess") static class MethodReference {
    final int mCallType;
    final Method mMethod;

    MethodReference(int callType, Method method) {
      mCallType = callType;
      mMethod = method;
      mMethod.setAccessible(true);
    }

    void invokeCallback(LifecycleOwner source, Lifecycle.Event event, Object target) {
      //noinspection TryWithIdenticalCatches
      try {
        switch (mCallType) {
          case CALL_TYPE_NO_ARG:
            mMethod.invoke(target);
            break;
          case CALL_TYPE_PROVIDER:
            mMethod.invoke(target, source);
            break;
          case CALL_TYPE_PROVIDER_WITH_EVENT:
            mMethod.invoke(target, source, event);
            break;
        }
      } catch (InvocationTargetException e) {
        throw new RuntimeException("Failed to call observer method", e.getCause());
      } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
      }
    }

    @Override public int hashCode() {
      return 31 * mCallType + mMethod.getName()
          .hashCode();
    }

    @Override public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      MethodReference that = (MethodReference) o;
      return mCallType == that.mCallType && mMethod.getName()
          .equals(that.mMethod.getName());
    }
  }
}
