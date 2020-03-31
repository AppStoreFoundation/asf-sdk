package com.appcoins.sdk.billing.mappers;

import com.appcoins.sdk.billing.models.Transaction;
import com.appcoins.sdk.billing.models.TransactionsListModel;
import com.appcoins.sdk.billing.models.billing.TransactionModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.appcoins.sdk.billing.utils.ServiceUtils.isSuccess;

public class TransactionMapper {

  public TransactionModel mapTransactionResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    TransactionModel transactionModel = new TransactionModel(code);
    if (isSuccess(code) && response != null) {
      try {
        jsonObject = new JSONObject(response);
        transactionModel = createTransactionModel(jsonObject, code);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return transactionModel;
  }

  public TransactionsListModel mapTransactionListResponse(RequestResponse requestResponse) {
    JSONObject jsonObject;
    String response = requestResponse.getResponse();
    int code = requestResponse.getResponseCode();
    TransactionsListModel transactionsListModel = new TransactionsListModel();
    List<TransactionModel> transactionModels = new ArrayList<>();
    if (isSuccess(code) && response != null) {
      try {
        JSONArray jsonArray = new JSONObject(response).optJSONArray("items");
        for (int i = 0; i < jsonArray.length(); i++) {
          jsonObject = jsonArray.getJSONObject(i);
          TransactionModel transactionModel = createTransactionModel(jsonObject, code);
          transactionModels.add(transactionModel);
        }
        transactionsListModel = new TransactionsListModel(transactionModels, false);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return transactionsListModel;
  }

  private TransactionModel createTransactionModel(JSONObject jsonObject, int code)
      throws JSONException {
    String uid = jsonObject.getString("uid");
    String hash = jsonObject.getString("hash");
    if (hash.equals("null")) {
      hash = null;
    }
    String orderReference = jsonObject.getString("reference");
    String status = jsonObject.getString("status");
    Transaction transaction = new Transaction(uid, hash, orderReference, status);
    return new TransactionModel(transaction, !isSuccess(code), code);
  }
}
