package com.sdk.appcoins_lifecycle.lifecycle.common;

import com.sdk.appcoins_lifecycle.lifecycle.Lifecycle;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.METHOD) public @interface OnLifecycleEvent {
  Lifecycle.Event value();
}
