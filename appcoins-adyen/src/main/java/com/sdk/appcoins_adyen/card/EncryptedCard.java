package com.sdk.appcoins_adyen.card;

import android.os.Parcel;
import android.os.Parcelable;
import com.sdk.appcoins_adyen.utils.ParcelUtils;

public final class EncryptedCard implements Parcelable {
  public static final Parcelable.Creator<EncryptedCard> CREATOR = new Creator<EncryptedCard>() {
    @Override public EncryptedCard createFromParcel(Parcel source) {
      return new EncryptedCard(source);
    }

    @Override public EncryptedCard[] newArray(int size) {
      return new EncryptedCard[size];
    }
  };

  private String mEncryptedNumber;

  private String mEncryptedExpiryMonth;

  private String mEncryptedExpiryYear;

  private String mEncryptedSecurityCode;

  private EncryptedCard() {
    // Use builder.
  }

  private EncryptedCard(Parcel source) {
    mEncryptedNumber = source.readString();
    mEncryptedExpiryMonth = source.readString();
    mEncryptedExpiryYear = source.readString();
    mEncryptedSecurityCode = source.readString();
  }

  @Override public int describeContents() {
    return ParcelUtils.NO_FILE_DESCRIPTOR;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mEncryptedNumber);
    dest.writeString(mEncryptedExpiryMonth);
    dest.writeString(mEncryptedExpiryYear);
    dest.writeString(mEncryptedSecurityCode);
  }

  public String getEncryptedNumber() {
    return mEncryptedNumber;
  }

  public String getEncryptedExpiryMonth() {
    return mEncryptedExpiryMonth;
  }

  public String getEncryptedExpiryYear() {
    return mEncryptedExpiryYear;
  }

  public String getEncryptedSecurityCode() {
    return mEncryptedSecurityCode;
  }

  /**
   * Builder for {@link EncryptedCard}s.
   */
  public static final class Builder {
    private final EncryptedCard mEncryptedCard = new EncryptedCard();

    /**
     * Set encrypted number.
     *
     * @return {@link EncryptedCard.Builder}
     */
    public Builder setEncryptedNumber(String encryptedNumber) {
      mEncryptedCard.mEncryptedNumber = encryptedNumber;

      return this;
    }

    /**
     * Set encrypted expiry date.
     *
     * @return {@link EncryptedCard.Builder}
     */
    public Builder setEncryptedExpiryDate(String encryptedExpiryMonth, String encryptedExpiryYear) {
      mEncryptedCard.mEncryptedExpiryMonth = encryptedExpiryMonth;
      mEncryptedCard.mEncryptedExpiryYear = encryptedExpiryYear;

      return this;
    }

    /**
     * Clear expiry date.
     *
     * @return {@link EncryptedCard.Builder}
     */
    @SuppressWarnings("PMD.NullAssignment") public Builder clearEncryptedExpiryDate() {
      mEncryptedCard.mEncryptedExpiryMonth = null;
      mEncryptedCard.mEncryptedExpiryYear = null;

      return this;
    }

    /**
     * Set encrypted security code.
     *
     * @return {@link EncryptedCard.Builder}
     */
    public Builder setEncryptedSecurityCode(String encryptedSecurityCode) {
      mEncryptedCard.mEncryptedSecurityCode = encryptedSecurityCode;

      return this;
    }

    /**
     * Build EncryptedCard object.
     *
     * @return {@link EncryptedCard}
     */
    public EncryptedCard build() {
      return mEncryptedCard;
    }
  }
}

