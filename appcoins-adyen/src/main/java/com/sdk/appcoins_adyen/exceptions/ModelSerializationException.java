package com.sdk.appcoins_adyen.exceptions;

import org.json.JSONException;

/**
 * Exception thrown when an issue occurs during serialization of a
 * {@link com.sdk.appcoins_adyen.api.ModelObject}.
 */
public class ModelSerializationException extends CheckoutException {

  private static final long serialVersionUID = -241916181048458214L;

  public ModelSerializationException(Class modelClass, JSONException cause) {
    super("Unexpected exception while serializing " + modelClass.getSimpleName() + ".", cause);
  }
}