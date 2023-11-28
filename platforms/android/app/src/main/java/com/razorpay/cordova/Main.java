package com.razorpay.cordova;

import com.razorpay.Checkout;
import com.razorpay.GenericPluginCallback;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;

import org.json.JSONObject;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import android.os.Bundle;
import android.content.Intent;

import androidx.annotation.NonNull;


public class Main extends CordovaPlugin implements PaymentResultWithDataListener, ExternalWalletListener {
  public static final String MAP_KEY_ERROR_CODE = "code";
  public static final String MAP_KEY_ERROR_DESC = "description";
  public static final String MAP_KEY_CONTACT = "contact";
  public static final String MAP_KEY_EMAIL = "email";
  public static final String MAP_KEY_EXTERNAL_WALLET_NAME = "external_wallet_name";
  public static final String MAP_METHOD_UNSUPPORTED = "method_not_supported";

  private String userAction;
  public CallbackContext cc;

  private Checkout checkout;

  @Override
  public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    this.cc = callbackContext;
    this.userAction = action;
      Log.d("DONNSB", "execute called");
    checkout = new Checkout().upiTurbo(this.cordova.getActivity());
    //checkout.upiTurbo.destroy();
    try{
        switch (action){
            case "open":
                Log.d("DONNSB", "case open");
                /*JSONObject payload = new JSONObject(data.getString(0));
                if (payload.has("key")) {
                    checkout.setKeyID(payload.getString("key"));
                    payload.remove("key");
                }*/
                JSONObject payload = new JSONObject(data.getString(0));
                checkout.open(this.cordova.getActivity(), payload);
                break;
            case "linkNewUPIAccount":
                Log.d("DONNSB", "case link new upi account");
                checkout.upiTurbo.linkNewUpiAccount("9928815231", "#aabb11", new GenericPluginCallback() {
                    @Override
                    public void onSuccess(@NonNull Object data) {
                       /*if(!((List<?>)data).isEmpty()){
                           cc.success((JSONObject) data);
                           Gson gson = new Gson();
                       }else {

                       }*/
                        Log.d("DONNSB", "linkNewUpiAccount success");
                    }

                    @Override
                    public void onError(@NonNull JSONObject jsonObject) {
                        Log.d("DONNSB", "linkNewUpiAccount failure");
                    }
                });
                break;
            case "manageUPIAccounts":
                Log.d("DONNSB", "case manage upi accounts");
                checkout.upiTurbo.manageUpiAccounts("9928815231", "#aabb11", new GenericPluginCallback() {
                    @Override
                    public void onSuccess(@NonNull Object data) {
                        Log.d("DONNSB", "manageUPIAccounts success");
                    }

                    @Override
                    public void onError(@NonNull JSONObject jsonObject) {
                        Log.d("DONNSB", "manageUPIAccounts failure");
                    }
                });
                break;
            default:
                JSONObject response = new JSONObject();
                response.put(MAP_METHOD_UNSUPPORTED, action);
                cc.error(response);
        }

    } catch (Exception e){
        e.printStackTrace();
        Log.d("DONNSB", "exception is : try catch occur");
      Toast.makeText(this.cordova.getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
    }
    return true;
  }

 /* private JSONObject getJsonFromModel(Object data, Object model){

  }*/

  public Bundle onSaveInstanceState() {
    Bundle bundle = new Bundle();
    bundle.putString("action", this.userAction);
    return bundle;
  }

  @Override 
  public void onRestoreStateForActivityResult(Bundle state, CallbackContext callbackContext) {
      if(state != null){
        this.userAction = state.getString("action");
      }
      this.cc = callbackContext;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    //Checkout.handleActivityResult(this.cordova.getActivity(), requestCode, resultCode, intent, this, this);
  }

  @Override
  public void onPaymentSuccess(String razorpayPaymentId, PaymentData paymentData) {
    if (this.userAction.equalsIgnoreCase("open")) {
        cc.success(paymentData.getData());
    }
  }

  @Override
  public void onPaymentError(int code, String description, PaymentData paymentData) {
    if (this.userAction.equalsIgnoreCase("open")) {
      try {
          JSONObject error = new JSONObject();
          error.put(MAP_KEY_ERROR_CODE, code);
          error.put(MAP_KEY_ERROR_DESC, description);
          error.put(MAP_KEY_CONTACT, paymentData.getUserContact());
          error.put(MAP_KEY_EMAIL, paymentData.getUserEmail());
          cc.error(error);
      } catch(Exception e){}
    }
  }

  @Override
  public void onExternalWalletSelected(String name, PaymentData paymentData) {
    if (this.userAction.equalsIgnoreCase("open")) {
      try {
          JSONObject response = new JSONObject();
          response.put(MAP_KEY_EXTERNAL_WALLET_NAME, name);
          response.put(MAP_KEY_EMAIL, paymentData.getUserEmail());
          response.put(MAP_KEY_CONTACT, paymentData.getUserContact());
          cc.error(response);
      } catch(Exception e){}
    }
  }
}
