package com.example.pj2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pj2.tab1.PhoneBook;
import com.example.pj2.tab3.custom_jsonparsor;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private CallbackManager mCallbackManager;

    @SuppressWarnings("ClickableViewAccessibility")

    public static List<String> names;
    public static List<PhoneBook> phoneBooks;
    private FusedLocationProviderClient mFusedLocationClient;
    public static double latitude, longitude;


    private String[] permission_list = { Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            System.out.println("9999999999999999999999" + latitude + " " + longitude);
                        }
                    }
                });

        names = new ArrayList<>();
        phoneBooks = new ArrayList<>();
        Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        c.moveToFirst();
        do {
            PhoneBook pb = new PhoneBook();
            String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            pb.setName(name);
            pb.setTel(phoneNumber);
            names.add(name);
            phoneBooks.add(pb);
        } while (c.moveToNext());

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission_group.CONTACTS}, 1);

//        FacebookSdk.sdkInitialize(this.getApplicationContext());

        mCallbackManager = CallbackManager.Factory.create();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token

                        String token = task.getResult().getToken();
                        Log.w("Firebase", "Token : "+ token);
                        saveTokenToServer(token);
                    }
                });



        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        setContentView(R.layout.activity_main);
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.add(R.id.outerfragment, new fragment_viewpager());
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
//        getHashKey(this);
    }

    @Nullable
    public static String getHashKey(Context context) {
        final String TAG = "KeyHash";
        String keyHash = null;
        try {
            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, keyHash);

            }

        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }

        if (keyHash != null) {
            return keyHash;
        } else {
            return null;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = this.checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(this.getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    this.finish();
                }
            }
        }
    }
    public void saveTokenToServer(String token){
        String request = "http://192.249.19.254:7280/toilet/token";
        request += "?token="+token;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        result = response;
////                        Log.d("response", result);
////                        custom_jsonparsor aa = new custom_jsonparsor();
////                        data = aa.weatherjsonParsor(response);
////                        Log.d("parsing", data.toString());
////                        if (data.isEmpty()){ // Data is not updated yet. Take data from one hour ago.
////                            Log.d("getinfo","No data : Take data from past");
//////                        getinfo_past();
////                        }
////                        else{
////                            setView(data, city, sector);
////                        }
                        Log.d("SendToken",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tab3", "Connecting problem");
                if (error instanceof TimeoutError) {
                    Log.d("tab3", "Timeout");
                } else if (error instanceof NoConnectionError) {
                    //TODO
                    Log.d("tab3", "NoConnectionError problem");
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    Log.d("tab3", "AuthFailureError problem");
                } else if (error instanceof ServerError) {
                    //TODO
                    Log.d("tab3", "ServerError problem");
                } else if (error instanceof NetworkError) {
                    //TODO
                    Log.d("tab3", "NetworkError problem");
                } else if (error instanceof ParseError) {
                    //TODO
                    Log.d("tab3", "ParseError problem");
                }
            }
        });
        queue.add(stringRequest);
    }
}





