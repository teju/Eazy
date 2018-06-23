package eazi.com.eazi.configure;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import eazi.com.eazi.R;
import eazi.com.eazi.RegisterPhoneNumber;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.PrintClass;

/**
 * Created by tejaswini on 19/06/18.
 */

public class post_async extends AsyncTask<String, Integer, String> {

    private final RegisterPhoneNumber registerPhoneNumber;
    private final RegisterPhoneNumber context;
    private final String action;

    public post_async(RegisterPhoneNumber registerPhoneNumber, String action) {
        this.registerPhoneNumber =registerPhoneNumber;
        this.context = registerPhoneNumber;
        this.action = action;
    }

    @Override
    protected String doInBackground(String... params) {
        invoke(params[0], params[1]);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        CommonMethods.dialog(context).show();

    }
    public void invoke(String url, final String postString) {

        PrintClass.printValue("SYSTEMPRINT POST SYNC invoke ", url + " postString " + postString);
        String s = "";
        RequestQueue queue = Volley.newRequestQueue(context);
        int method ;
        if(url.endsWith("country")) {
            method = Request.Method.GET;
        } else {
            method = Request.Method.PUT;
        }

        final String mRequestBody = postString;

        StringRequest strReq = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            CommonMethods.dialog(context).cancel();
                            System.gc();
                            Runtime.getRuntime().gc();
                            PrintClass.printValue("SYSTEMPRINT onResponse ", response.toString());
                            sendResult(response);
                        } catch (Exception e) {
                            CommonMethods.dialog(context).cancel();

                            e.printStackTrace();
                            PrintClass.printValue("SYSTEMPRINT postsync " +
                                    "  Exception  ", e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CommonMethods.dialog(context).cancel();

                        System.out.println("SYSTEMPRINT error "  +
                                " error " + error.toString());
                    }
                }) {


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {

                    System.out.println("SYSTEMPRINT error  Unsupported Encoding while trying to get " +
                            "the bytes of %s using %s"+ mRequestBody+"utf-8");

                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s " +
                            "using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(strReq);
    }

    private void sendResult(String resultString) {
        PrintClass.printValue("SYSTEMPRINT postsync  if  ", "action " + action +
                " resultString " + resultString);
        try {
            CommonMethods.dialog(context).cancel();
            if (this.registerPhoneNumber != null && action.equalsIgnoreCase("confirmuser")) {
                this.registerPhoneNumber.ResponseOFConfirm(resultString);
            } else if (this.registerPhoneNumber != null && action.equalsIgnoreCase("register")) {
                this.registerPhoneNumber.ResponseOFRegister(resultString);
            }else if (this.registerPhoneNumber != null && action.equalsIgnoreCase("getCountries")) {
                this.registerPhoneNumber.ResponseOFCountriesr(resultString);
            }

        } catch (Exception e) {
            CommonMethods.dialog(context).cancel();
        }
    }

}
