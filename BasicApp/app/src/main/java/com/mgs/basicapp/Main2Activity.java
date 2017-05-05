package com.mgs.basicapp;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity implements Animation.AnimationListener {

    Button btn1, btn2, btn3, btn4;
    TextView txtout;
    final String API1 = "http://freegeoip.net/json/";
    final String API2 = "http://ip-api.com/json/";
    RequestQueue requestQueue;
    JsonObjectRequest jsonObjectRequest;
    String btnActive = "";
    ProgressBar progressBar;
    ImageView progress;
    Animation animation;
    RelativeLayout relativeLayout;
    MyFirstAsync myFirstAsync;
    MySecondAsync mySecondAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);

        txtout = (TextView) findViewById(R.id.txtOut);

        progress = (ImageView) findViewById(R.id.progress_bar);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_main2);

        requestQueue = Volley.newRequestQueue(this);



//        progressBar = (ProgressBar) findViewById(R.id.progressBar);
       /* if(getIntent().getData()!=null)
        Toast.makeText(getApplicationContext()," Main2Activity "+getIntent().getData().toString(),Toast.LENGTH_LONG).show();*/

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCustom();
                btnActive = "API 1 \n";

                myFirstAsync=new MyFirstAsync();
                mySecondAsync=new MySecondAsync();

                myFirstAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                mySecondAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                //callApiGet(API1);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressCustom();
                btnActive = "API 2 \n";
                callApiPost(API2);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnActive = "Telephony Manager \n";
                final TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                final String simCountry = tm.getSimCountryIso();
                String networkOperator = tm.getNetworkOperator();

                if (!TextUtils.isEmpty(networkOperator)) {
                    int mcc = Integer.parseInt(networkOperator.substring(0, 3));
                    int mnc = Integer.parseInt(networkOperator.substring(3));
                    txtout.setText(btnActive + "Country SIM : " + simCountry + " ,  MCC :  " + mcc + " ,  MNC : " + mnc);
                }
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnActive = "WIFI Manager \n";
                WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);

                WifiInfo wifiInfo = wm.getConnectionInfo();
                int ip = wifiInfo.getIpAddress();

                String ipAddress = Formatter.formatIpAddress(ip);

                txtout.setText(btnActive + "IP : " + ipAddress + " \nWIFI info : " + wm.getConnectionInfo());

            }
        });
    }


    void progressCustom() {
//        SVG svg = SVGParser.getSVGFromResource(getResources(), R.raw.android,0xFF9FBF3B, 0xFF1756c9);
        // Get a drawable from the parsed SVG and set it as the drawable for the ImageView
//        imageView.setImageDrawable(svg.createPictureDrawable());
        // Set the ImageView as the content view for the Activity
//        setContentView(imageView);

        if (progress != null) {
            progress.setVisibility(View.VISIBLE);
            animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
            animation.setAnimationListener(this);
            progress.startAnimation(animation);

            /*AnimationDrawable frameAnimation = (AnimationDrawable)progress.getDrawable();
            frameAnimation.setCallback(progress);
            frameAnimation.setVisible(true, true);
            frameAnimation.start();*/
        }
//        progressBar.setVisibility(View.VISIBLE);
//        progressBar.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_IN);
       /* ObjectAnimator animation = ObjectAnimator.ofInt (progress, "progress", 0, 500); // see this max value coming back here, we animale towards that value
        animation.setDuration (5000); //in milliseconds
        animation.setInterpolator (new DecelerateInterpolator());
        animation.start ();*/
    }

    public class MyFirstAsync extends AsyncTask<String, Void, String> {

        boolean isCancelled = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("First Async "," onPreExecute ");
           /* progressDialog = new ProgressDialog(Main2Activity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("First Async...");
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            URLConnection yc=null;
            try
            {
                URL yahoo = new URL(API1);
                yc = yahoo.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    Log.d("API 1", " Response " + inputLine);

                    if (isCancelled())
                        return "null";

                    return inputLine;
                }
                in.close();

            }  catch (Exception e)
            {
                e.printStackTrace();
                return "Exception";
            } finally {
                if (yc != null) {
                    yc=null;
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            if(progressDialog !=null && progressDialog.isShowing())
//                progressDialog.dismiss();

            Log.d("First Async "," Response "+s);
            txtout.setText(btnActive + "Response :\n " + s);

            myFirstAsync=null;

        }

    }

    public class MySecondAsync extends AsyncTask<String, Void, String> {

        boolean isCancelled = false;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Second Async "," onPreExecute ");
          /*  progressDialog = new ProgressDialog(Main2Activity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Second Async...");
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            URLConnection yc=null;
            try
            {
                URL yahoo = new URL(API2);
                yc = yahoo.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    Log.d("API 2", " Response " + inputLine);

                    if (isCancelled())
                        return "null";

                    return inputLine;
                }
                in.close();

            }  catch (Exception e)
            {
                e.printStackTrace();
                return "Exception";
            } finally {
                if (yc != null) {
                    yc=null;
                }
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            if(progressDialog !=null && progressDialog.isShowing())
//                progressDialog.dismiss();

            Log.d("Second Async "," Response "+s);
            txtout.setText(btnActive + "Response :\n " + s);

            mySecondAsync=null;
        }

    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.i("MainAct2 ", "***** IP=" + ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("MainAct2 ", ex.toString());
        }
        return null;
    }

    private void callApiGet(final String url) {

        try {

            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.setVisibility(View.GONE);
//                    progressBar.clearAnimation();
                    txtout.setText(btnActive + "Response :\n " + response);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.setVisibility(View.GONE);
//                    progressBar.clearAnimation();
                    Toast.makeText(Main2Activity.this, "VolleyError " + error, Toast.LENGTH_LONG).show();
                }

            });

            RetryPolicy retryPolicy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObjectRequest.setRetryPolicy(retryPolicy);
            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callApiPost(final String url) {

        try {

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    progress.setVisibility(View.GONE);
//                    progressBar.clearAnimation();
                    txtout.setText(btnActive + "Response :\n " + response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progress.setVisibility(View.GONE);
//                    progressBar.clearAnimation();
                    Toast.makeText(Main2Activity.this, "VolleyError " + error, Toast.LENGTH_LONG).show();
                }

            });

            requestQueue.add(jsonObjectRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(myFirstAsync!=null)
        myFirstAsync.cancel(true);
        if(mySecondAsync!=null)
        mySecondAsync.cancel(true);
    }
}
