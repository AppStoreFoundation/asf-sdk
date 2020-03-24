package com.appcoins.sdk.billing.helpers.translations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.appcoins_wallet;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.button_ok;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.buy_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.cancel_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.error_contac_us_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_best_deal;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_card_cvv;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_card_cvv_cvc;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_card_expiry;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_card_number;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_change_card_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_as_guest_credit_card;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_as_guest_paypal;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_as_guest_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_with_wallet_reward_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_with_wallet_reward_no_connection_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_with_wallet_reward_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_pay_with_wallet_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_change_payment_method_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_reward_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_reward_no_connection_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_reward_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_title_long;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_support_1;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_support_2_link;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_switch_to_credit_card;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_switch_to_paypal;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_wallet_not_installed_popup_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_wallet_not_installed_popup_close_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_wallet_not_installed_popup_close_install;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iap_wallet_and_appstore_not_installed_popup_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.install_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.next_button;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.poa_wallet_not_installed_notification_body;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.poa_wallet_not_installed_notification_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_CVV;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_expired;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_general_1;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_general_2;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_invalid_details;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_no_funds;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_not_supported;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_security;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_title;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_card_error_wrong_CVV;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.purchase_error_item_owned;
import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.title_dialog_error;

//Class covered with AndroidTests. Always run them if you change this class
public class TranslationsModel {

  //This is needed as there may be an error parsing the Xml.
  private Map<TranslationsKeys, String> defaultStringsMap;

  /**
   * Whenever you add a string to an xml file, you should create the enum with the key name in
   * the TranslationsKeys.java and then add it to the defaultStringsMap in the position as in the
   * xml. After this you should run the AndroidTests to confirm that you add it correctly.
   */
  public TranslationsModel() {
    defaultStringsMap = new LinkedHashMap<TranslationsKeys, String>() {
      { //This list needs to be in the same order as the string xml file. If not the androidTests
        // will fail.
        put(iab_wallet_not_installed_popup_body, "To buy this item you first need to get the %s.");
        put(appcoins_wallet, "AppCoins Wallet");
        put(iab_wallet_not_installed_popup_close_button, "CLOSE");
        put(iap_wallet_and_appstore_not_installed_popup_body,
            "You need the AppCoins Wallet to make this purchase. Download it from Aptoide or Play "
                + "Store"
                + " and come back to complete your purchase!");
        put(iap_wallet_and_appstore_not_installed_popup_button, "GOT IT!");
        put(poa_wallet_not_installed_notification_title, "You need the AppCoins Wallet!");
        put(poa_wallet_not_installed_notification_body,
            "To get your reward you need the AppCoins Wallet.");
        put(iab_pay_as_guest_title, "Pay as a guest");
        put(iab_pay_as_guest_credit_card, "Credit Card");
        put(iab_pay_as_guest_paypal, "PayPal");
        put(iab_switch_to_paypal, "PAY USING PAYPAL");
        put(iab_switch_to_credit_card, "PAY USING CREDIT CARD");
        put(iab_card_number, "Card number");
        put(iab_card_expiry, "MM/YY");
        put(iab_card_cvv, "CVV");
        put(iab_card_cvv_cvc, "CVC/CVV");
        put(iab_change_card_button, "CHANGE CARD");
        put(iab_pay_with_wallet_title, "Pay with AppCoins Wallet");
        put(iab_pay_with_wallet_reward_title, "Get up to %s%% Bonus!");
        put(iab_pay_with_wallet_reward_body, "You\'ll receive %s on this purchase.");
        put(iab_pay_with_wallet_reward_no_connection_body,
            "You'll receive a Bonus on this purchase.");
        put(iab_best_deal, "BEST DEAL");
        put(iab_purchase_done_title, "DONE!");
        put(iab_purchase_done_reward_title,
            "Next time, get up to %s%% Bonus with the AppCoins Wallet!");
        put(iab_purchase_done_reward_no_connection_title,
            "Next time, get a Bonus with the AppCoins Wallet!");
        put(iab_purchase_done_reward_body, "You could have received %s on this purchase.");
        put(iab_purchase_done_title_long, "Purchase completed!");
        put(iab_purchase_change_payment_method_button, "MORE PAYMENT METHODS");
        put(next_button, "NEXT");
        put(cancel_button, "CANCEL");
        put(buy_button, "BUY");
        put(iab_wallet_not_installed_popup_close_install, "INSTALL WALLET");
        put(button_ok, "OK");
        put(install_button, "Install");
        put(title_dialog_error, "Error");
        put(purchase_error_item_owned, "You already own this item!");
        put(purchase_card_error_title, "Oops, something went wrong.");
        put(purchase_card_error_general_1,
            "There was a problem with your card. Please try again or contact us.");
        put(purchase_card_error_general_2,
            "The transaction has been rejected by your bank. Please try with a different card or "
                + "contact us.");
        put(purchase_card_error_no_funds,
            "It seems you don't have enough funds or there's a limit on your card. Please try "
                + "with a "
                + "different one.");
        put(purchase_card_error_expired,
            "It seems your card has expired. Please try with a different one.");
        put(purchase_card_error_invalid_details,
            "Are you sure your card number is correct? Please check and try again.");
        put(purchase_card_error_not_supported,
            "Your card type is not supported yet. Try with a different one.");
        put(purchase_card_error_security,
            "Are you sure the security information is correct? Please try again.");
        put(purchase_card_error_wrong_CVV,
            "Your CVV/CVC code seems to be wrong. Please try again.");
        put(purchase_card_error_CVV, "Wrong CVV/CVC");
        put(error_contac_us_body, "If the problem persists please contact us.");
        put(iab_purchase_support_1, "Need help?");
        put(iab_purchase_support_2_link, "Contact Support.");
      }
    };
  }

  public void mapStrings(List<String> list) {
    int position = 0;
    int listSize = list.size();
    for (Map.Entry<TranslationsKeys, String> entry : defaultStringsMap.entrySet()) {
      if (position >= listSize) {
        return;
      }
      entry.setValue(list.get(position));
      position++;
    }
  }

  public String getString(TranslationsKeys key) {
    return defaultStringsMap.get(key);
  }

  //Test only methods. The following methods are only to support tests.

  public Map<TranslationsKeys, String> getDefaultStringsMap() {
    return defaultStringsMap;
  }

  public int getNumberOfTranslations() {
    return defaultStringsMap.size();
  }
}
