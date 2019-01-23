package com.appcoins.sdk.android_appcoins_billing.helpers;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.appcoins.billing.AppcoinsBilling;

public class PayloadHelper {
    private static final String SCHEME = "appcoins";
    private static final String ADDRESS_PARAMETER = "address";
    private static final String PAYLOAD_PARAMETER = "payload";

    /**
     * Method to build the payload required on the {@link AppcoinsBilling#getBuyIntent} method.
     * @param developerAddress The developer's ethereum address
     * @param developerPayload The additional payload to be sent
     *
     * @return The final developers payload to be sent
     */
    public static String buildIntentPayload(@NonNull String developerAddress, String developerPayload) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority("appcoins.io")
                .appendQueryParameter(ADDRESS_PARAMETER, developerAddress);
        if (developerPayload != null) {
            builder.appendQueryParameter(PAYLOAD_PARAMETER, developerPayload);
        }
        return builder.toString();
    }

    /**
     * Given a uri string validate if it is part of the expected scheme and if so return the
     * developer's ethereum address.
     *
     * @param uriString The payload uri content
     *
     * @return The developers ethereum address
     */
    public static String getAddress(String uriString) {
        Uri uri = Uri.parse(uriString);
        if (uri.getScheme().equalsIgnoreCase(SCHEME)) {
            return uri.getQueryParameter(ADDRESS_PARAMETER);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Given a uri string validate if it is part of the expected scheme and if so return the
     * addition payload content.
     *
     * @param uriString The payload uri content
     *
     * @return The additional payload content
     */
    public static String getPayload(String uriString) {
        Uri uri = Uri.parse(uriString);
        if (uri.getScheme().equalsIgnoreCase(SCHEME)) {
            return uri.getQueryParameter(PAYLOAD_PARAMETER);
        } else {
            throw new IllegalArgumentException();
        }
    }
}

