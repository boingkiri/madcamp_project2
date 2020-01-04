package com.example.pj2.tab1;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pj2.R;

public class tab1 extends Fragment{
    ListViewAdapter adapter;

    public tab1() {
        // Required empty public constructor
    }


    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_tab1, container, false);
        Button addContactbtn = view.findViewById(R.id.add_contact);
        Button addToServer = view.findViewById(R.id.add_server);

        ListView MyListView = view.findViewById(R.id.list);
        adapter = new ListViewAdapter(getActivity());
        MyListView.setAdapter(adapter);
        final Activity activity = getActivity();

        addContactbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                //Casting getitem value to integer -> can be vulnerable.
                Fragment newFragment = new tab1_addcontacts();

                fragmentTransaction.replace(R.id.outerfragment, newFragment);
//                FragmentStatePagerAdapter swipe = DemoCollectionPagerAdapter(fm);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        addToServer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                final TextView textView = (TextView) getActivity().findViewById(R.id.text);
                // ...

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url ="http://192.249.19.254:7080";

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
//                                textView.setText("Response is: "+ response.substring(0,500));
                                Log.d("YooHoo!","Server connection complete");
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("No!","Connection error");
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        });

        MyListView.setOnItemLongClickListener(new ListViewItemLongClickListener());
//        MyListView.setOnItemLongClickListener(new ListViewItemLongClickListener());
        return view;
    }

    // Long click된 item의 index(position)을 기록한다.
    int selectedPos = -1;

    /**
     * ListView의 item을 길게 클릭했을 경우.
     * 클릭된 item을 삭제한다.
     * @author stargatex
     *
     */
    private class ListViewItemLongClickListener implements AdapterView.OnItemLongClickListener
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
        {
            Log.d("AAA","AA");
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle(R.string.alert_title_question);

            // '예' 버튼이 클릭되면
            alertDlg.setPositiveButton( R.string.button_yes, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which )
                {
                    adapter.deleteContactFromNameLIKE(selectedPos);
                    // 아래 method를 호출하지 않을 경우, 삭제된 item이 화면에 계속 보여진다.
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });

            // '아니오' 버튼이 클릭되면
            alertDlg.setNegativeButton( R.string.button_no, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick( DialogInterface dialog, int which ) {
                    dialog.dismiss();  // AlertDialog를 닫는다.
                }
            });


            alertDlg.setMessage( String.format( getString(R.string.alert_msg_delete),
                    adapter.getItem(position).toString()) );
            alertDlg.show();
            return false;
        }

    }

}
