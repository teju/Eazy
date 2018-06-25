package eazi.com.eazi;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.model.Contact;
import eazi.com.eazi.model.Country;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;
import eazi.com.eazi.configure.IsNetworkConnection;
import eazi.com.eazi.configure.post_async;
import eazi.com.eazi.utils.PrintClass;
import eazi.com.eazi.view.CustomToast;

/**
 * Created by tejaswini on 18/05/18.
 */

public class RegisterPhoneNumber extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private static final int REQUEST_READ_PHONE_STATE = 2;
    private Spinner country_spinner;
    Button ok;
    private EditText phone_number;
    private View rootView;
    private List<Contact> contacts = new ArrayList<>();
    private Spinner country_code_spinner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_phonenumber);
        initUI();
        initData();
    }

    public void initUI(){
        rootView=findViewById(android.R.id.content);
        country_spinner = (Spinner)findViewById(R.id.country_list);
        country_code_spinner = (Spinner)findViewById(R.id.country_code);
        ok = (Button)findViewById(R.id.ok);
        phone_number = (EditText)findViewById(R.id.phone_number);
    }

    public void initData(){
        String locale = getResources().getConfiguration().locale.getDisplayCountry();
        System.out.println("locale123 "+locale);
        callCountryApi();
        ok.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok :
                int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
                } else {
                    if(phone_number.getText().toString().length() != 0) {
                        callApiRegister();
                    } else {
                        Toast.makeText(getApplicationContext(),"Enter the phone Number",Toast.LENGTH_LONG).show();
                        phone_number.setError("Enter Phone Number");
                    }
                }
                break;
        }
    }

    public void callApiRegister(){
        if (IsNetworkConnection.checkNetworkConnection(RegisterPhoneNumber.this)) {
            String url = Constants.SERVER_URL + Constants.register;
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("mdn", "91"+phone_number.getText().toString().replaceAll("\\s",""));
                ;
                jsonObject.put("type", "android");
                jsonObject.put("device_id", CommonMethods.getDeviceIMEI(RegisterPhoneNumber.this));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new post_async(RegisterPhoneNumber.this,"register").execute(url, jsonObject.toString());
        } else {
            new CustomToast().Show_Toast(getApplicationContext(), rootView,
                    "No Internet Connection");
        }
    }

    public void callApiLogin(){
        if (IsNetworkConnection.checkNetworkConnection(RegisterPhoneNumber.this)) {
            String url = Constants.SERVER_URL + Constants.confirm;
            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("mdn", "91"+phone_number.getText().toString().replaceAll("\\s",""));
                jsonObject.put("code", 1234);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new post_async(RegisterPhoneNumber.this,"confirmuser").execute(url, jsonObject.toString());
        } else {
            new CustomToast().Show_Toast(getApplicationContext(), rootView,
                    "No Internet Connection");
        }
    }

    public void callCountryApi(){
        /*if (IsNetworkConnection.checkNetworkConnection(RegisterPhoneNumber.this)) {
            String url = Constants.SERVER_URL + Constants.country;
            new post_async(RegisterPhoneNumber.this,"getCountries").execute(url, null);
        } else {
            new CustomToast().Show_Toast(getApplicationContext(), rootView,
                    "No Internet Connection");
        }*/
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    URL mUrl = new URL(Constants.SERVER_URL+Constants.country);
                    HttpURLConnection httpConnection = (HttpURLConnection) mUrl.openConnection();
                    httpConnection.setRequestMethod("GET");
                    httpConnection.setUseCaches(false);
                    httpConnection.setAllowUserInteraction(false);
                    httpConnection.setConnectTimeout(100000);
                    httpConnection.setReadTimeout(100000);

                    httpConnection.connect();

                    int responseCode = httpConnection.getResponseCode();
                    System.out.println("ResponseOFCountriesr  "+responseCode);

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println("ResponseOFCountriesr  "+sb.toString());

                        ResponseOFCountriesr(sb.toString());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ResponseOFCountriesr Exception "+e.toString());

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
        t.start();

    }

    public void ResponseOFRegister(String resultString) {
        if(resultString != null) {
            callApiLogin();
        }
    }

    public void ResponseOFConfirm(String resultString) {
        if(resultString != null) {
            try {
                JSONObject jsonObject = new JSONObject(resultString);
                CommonMethods.addSharedPref(RegisterPhoneNumber.this,Constants.user_name,jsonObject.getString("username"));
                CommonMethods.addSharedPref(RegisterPhoneNumber.this,Constants.user_pass,jsonObject.getString("password"));
                getContacts();

            } catch (Exception e){
                System.out.println("ResponseOConfirm Exception "+e.toString());
            }
        } else {
            new CustomToast().Show_Toast(getApplicationContext(), rootView,
                    "Something went wrong");
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void ResponseOFCountriesr(String resultString) {
        final List<Country> countries = new ArrayList<>();
        if(resultString != null) {
            System.out.println("ResponseOFCountriesr "+resultString);
            try {
                JSONObject json = new JSONObject(resultString);

                JSONArray jsonArray = json.getJSONArray("countries");

                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Country country = new Country();
                    country.setCountry_name(jsonObject.getString("name"));
                    country.setCountry_code(jsonObject.getString("code"));
                    countries.add(country);

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAdapters(countries);

                    }
                });
            } catch (Exception e){
                System.out.println("ResponseOFCountriesr Exception "+e.toString());
            }
        }
    }

    private void setAdapters(List<Country> countries) {
        List<String> country_name = new ArrayList<>();
        List<String> country_code = new ArrayList<>();
        for(int i = 0;i<countries.size();i++) {
            country_name.add(countries.get(i).getCountry_name());
            country_code.add(countries.get(i).getCountry_code());
        }

        ArrayAdapter countryAdapter = new ArrayAdapter(this,R.layout.spinner_item,country_name);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country_spinner.setAdapter(countryAdapter);
        country_spinner.setOnItemSelectedListener(this);

        ArrayAdapter countrycodeAdapter = new ArrayAdapter(this,R.layout.spinner_item,country_code);
        countrycodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        country_code_spinner.setAdapter(countrycodeAdapter);
        country_code_spinner.setOnItemSelectedListener(this);
    }

    public void getContacts() {

        if (ContextCompat.checkSelfPermission(RegisterPhoneNumber.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)

        {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterPhoneNumber.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(RegisterPhoneNumber.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            System.out.println("PHONEVERIFIED "+CommonMethods.getSharedPrefValue(this,Constants.user_verified));
            if(CommonMethods.getSharedPrefValue(this,Constants.user_verified).equals("false")
                    || CommonMethods.isEmpty(CommonMethods.getSharedPrefValue(this,Constants.user_verified))) {
                Intent i = new Intent(RegisterPhoneNumber.this, PhoneVerification.class);
                startActivity(i);
            } else {
                Intent i = new Intent(RegisterPhoneNumber.this, Invite.class);
                startActivity(i);
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS :
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CommonMethods.getContact(RegisterPhoneNumber.this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Intent i = new Intent(RegisterPhoneNumber.this,PhoneVerification.class);
                    startActivity(i);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;

            case REQUEST_READ_PHONE_STATE :
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callApiRegister();
                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        country_code_spinner.setSelection(position);
        country_spinner.setSelection(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        country_code_spinner.setSelection(0);
        country_spinner.setSelection(0);
    }
}
