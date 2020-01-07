package com.example.pj2.tab3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pj2.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import android.graphics.Typeface;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.facebook.share.internal.DeviceShareDialogFragment.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class tab3 extends Fragment {

    location loc;
    private TextView weather_text;
    private TextView now_tem;
    private Object TextView;

    public tab3() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        final weather cur_weather = new weather(container.getContext(), view);

        final Button citybtn = view.findViewById(R.id.citybtn);
        final Button sectorbtn = view.findViewById(R.id.sectorbtn);
        loc = new location(getActivity().getApplicationContext(), cur_weather);

        super.onCreate(savedInstanceState);

        weather_text = view.findViewById(R.id.weather);
        now_tem = view.findViewById(R.id.nowtext);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),"fonts/NanumSquareRoundB.ttf");
        weather_text.setTypeface(face);
        now_tem.setTypeface(face);

        new WeatherAsynTask(weather_text,now_tem).execute("https://weather.naver.com/","span[class=temp]");

        citybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn_view) {
                FirebaseInstanceId.getInstance().getId();
                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.send(new RemoteMessage.Builder(SENDER_ID + "@fcm.googleapis.com")
//                        .setMessageId(Integer.toString(messageId))
//                        .addData("my_message", "Hello World")
//                        .addData("my_action","SAY_HELLO")
//                        .build());

                show(loc.getCities(), citybtn);
                sectorbtn.setText("시/구/군/면");
            }
        });
        sectorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn_view) {
            if (loc.getCity().equals("")){
                Toast.makeText(getActivity().getApplicationContext(), "광역시/도 를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                show_1(loc.getSectors(), sectorbtn);
            }
            }
        });
        return view;
    }

    void show(Set<String> cities, final Button btn_view){
        final List<String> ListItems = new ArrayList<>();
        for (String elem: cities){
            ListItems.add(elem);
        }
        ListItems.sort(null);
        final CharSequence[] items =  ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("광역시/도 를 고르십시오.");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getActivity().getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                loc.setCity(selectedText);
                btn_view.setText(loc.getCity());
            }
        });
        builder.show();
    }

    void show_1(Set<String> sectors, final Button btn_view) {
        final List<String> ListItems = new ArrayList<>();
        for (String elem : sectors) {
            ListItems.add(elem);
        }
        ListItems.sort(null);
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("시/구/군/면 을 고르십시오.");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int pos) {
                String selectedText = items[pos].toString();
                Toast.makeText(getActivity().getApplicationContext(), selectedText, Toast.LENGTH_SHORT).show();
                loc.setSector(selectedText);
                btn_view.setText(loc.getSector());
            }
        });
        builder.show();
    }

//    public static void sendPushToSingleInstance(final Context activity, final HashMap dataValue /*your data from the activity*/, final String instanceIdToken /*firebase instance token you will find in documentation that how to get this*/ ) {
//
//
//        final String url = "https://fcm.googleapis.com/fcm/send";
//        StringRequest myReq = new StringRequest(Request.Method.POST,url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Toast.makeText(activity, "Bingo Success", Toast.LENGTH_SHORT).show();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(activity, "Oops error", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//
//            @Override
//            public byte[] getBody() throws com.android.volley.AuthFailureError {
//                Map<String,String> rawParameters = new Hashtable<String, String>();
//                rawParameters.put("data", new JSONObject(dataValue).toString());
//                rawParameters.put("to", instanceIdToken);
//                return new JSONObject(rawParameters).toString().getBytes();
//            };
//
//            public String getBodyContentType()
//            {
//                return "application/json; charset=utf-8";
//            }
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", "key="+YOUR_LEGACY_SERVER_KEY_FROM_FIREBASE_CONSOLE);
//                return headers;
//            }
//
//        };
//
//        Volley.newRequestQueue(activity).add(myReq);
//    }
}


