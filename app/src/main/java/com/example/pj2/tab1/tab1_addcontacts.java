package com.example.pj2.tab1;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pj2.R;
import com.example.pj2.fragment_viewpager;

public class tab1_addcontacts extends Fragment {


    public tab1_addcontacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tab1_addcontacts", "start");
        View view = inflater.inflate(R.layout.fragment_tab1_addcontact, container, false);
//        return view;
        Button okbtn = view.findViewById(R.id.fragment1_ok);
        Button cancelbtn = view.findViewById(R.id.fragment1_cancel);
        final EditText name = view.findViewById(R.id.Edit_name);
        final EditText phone = view.findViewById(R.id.Edit_phone);
        final EditText email = view.findViewById(R.id.Edit_email);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameValue = name.getText().toString();
                String phoneValue = phone.getText().toString();
                String emailValue = email.getText().toString();
                ContactAdd(nameValue, phoneValue, emailValue);
            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

            }
        });
        return view;
    }
    public void ContactAdd(final String nameValue, final String phoneValue, final String emailValue){
//        ProgressDialog asyncDialog = new ProgressDialog(getActivity());
        ContactThread thr = new ContactThread(nameValue, phoneValue, emailValue, getActivity());
        thr.start();

        try {
            // 해당 쓰레드가 멈출때까지 멈춤
            thr.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }
}
