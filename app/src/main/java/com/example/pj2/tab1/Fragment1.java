package com.example.pj2.tab1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.pj2.MainActivity;
import com.example.pj2.R;
import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment1 extends Fragment {
    final List<String> LIST_MENU = MainActivity.names;
    List<PhoneBook> REF_MENU = MainActivity.phoneBooks;
    String fb_id;
    String get_name, get_phone, get_fb_id;
    String post_name, post_phone, post_fb_id;
    String start_name, start_phone, start_fb_id;
    String get_db;
    ListViewAdapter adapter;
    String[] permission_list = { Manifest.permission.WRITE_CONTACTS };

//    Retrofit retrofit;
//    RetroBaseApiService retroBaseApiService;

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, null);
        checkPermission();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        fb_id = accessToken.getUserId();
        System.out.println("4040404040404040" + fb_id);

        Call<List<Contacts>> res = Net.getInstance().getRetro().get_start_Contacts(start_name, start_phone, start_fb_id);
        res.enqueue(new Callback<List<Contacts>>() {
            @Override
            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
//                        List<Contacts> contacts = response.body();

                        for(int i = 0; i < response.body().size(); i++) {
                            PhoneBook start_contacts = new PhoneBook();
                            start_name = response.body().get(i).getName();
                            start_phone = response.body().get(i).getPhone();
                            start_fb_id = response.body().get(i).get_fb_Id();
                            if(fb_id.equals(start_fb_id)) {
                                start_contacts.setName(start_name);
                                start_contacts.setTel(start_phone);
                                adapter.phoneBooks.add(start_contacts);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Contacts>> call, Throwable t) {
                System.out.println("실패" + t.getLocalizedMessage());
            }
        });


        adapter = new ListViewAdapter(getActivity(), R.layout.listview_btn_item, REF_MENU);

        final ListView listview = (ListView) view.findViewById(R.id.listview1);
        ImageButton add_button = (ImageButton) view.findViewById(R.id.add_btn);
        Button get_db_button = (Button) view.findViewById(R.id.get_db_contacts);



        get_db_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("4434343434334");
                Call<List<Contacts>> res = Net.getInstance().getRetro().get_Contacts(get_name, get_phone);
                res.enqueue(new Callback<List<Contacts>>() {
                    @Override
                    public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
//                        List<Contacts> contacts = response.body();

                                List<PhoneBook> get_allContacts = new ArrayList<>();
                                for(int i = 0; i < response.body().size(); i++) {
                                    PhoneBook get_contacts = new PhoneBook(); // 객체 생성
                                    get_name = response.body().get(i).getName();
                                    get_phone = response.body().get(i).getPhone();
                                    get_fb_id = response.body().get(i).get_fb_Id();

                                    System.out.println("11111111111111" + "name" + get_name + "phone" + get_phone + "fb_id" + get_fb_id);
                                    int issame = 0;
                                    if (fb_id.equals(get_fb_id)) { // fb 아이디 같으면 추가 대상에 오름
                                        for (int j = 0; j < adapter.phoneBooks.size(); j++) { // phonebook에 있는 모든 데이터와 비교하기 위해 반복문
                                            if (adapter.phoneBooks.get(j).getTel().equals(get_phone)) { // 그 데이터랑 fb아이디가 같으면 get_contacts에 저장 안하고 다음 반복문
                                                issame = 1;
                                                break;
                                            }
                                            else {
                                                continue;
                                            }
                                        }
                                        if (issame == 0){
                                            // 만약에 현재 리스트뷰에 없는 contact일 경우 추가해줘야 함
                                            get_contacts.setName(get_name);
                                            get_contacts.setTel(get_phone);
                                            get_allContacts.add(get_contacts);  // get_contact가 중복되면서 phonebook에 쌓임
                                        }
                                    }
                                }
                                adapter.phoneBooks.addAll(get_allContacts);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Contacts>> call, Throwable t) {
                        System.out.println("실패" + t.getLocalizedMessage());
                    }
                });


            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.phone_add, null);
                ad.setView(view);
                ad.setTitle("연락처 추가");       // 제목 설정
                ad.setMessage("이름과 전화번호를 입력해주세요");   // 내용 설정
                // EditText 삽입하기

                final Button submit = (Button) view.findViewById(R.id.buttonSubmit);
                final EditText name = (EditText) view.findViewById(R.id.edittext_name);
                final EditText phone_num = (EditText) view.findViewById(R.id.edittext_phone);
                name.setHint("이름을 입력하세요");
                phone_num.setHint("번호를 입력하세요");
                final AlertDialog dialog = ad.create();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PhoneBook add_phone = new PhoneBook();
                        // Text 값 받아서 로그 남기기
                        String full_name = name.getText().toString();
                        String phone_number = phone_num.getText().toString();
                        add_phone.setName(full_name);
                        add_phone.setTel(phone_number);
//                        contactAdd(full_name, phone_number);
                        adapter.phoneBooks.add(add_phone);

                        /////////////////// 연락처 db에 facebook 고유 id 가지고 저장
                        List<Contacts> post_list = new ArrayList<Contacts>();
                        Contacts post_contacts = new Contacts(full_name, phone_number, fb_id);
                        post_list.add(post_contacts);

                        Call<List<Contacts>> post_Contacts = Net.getInstance().getRetro().post_Contacts(post_list);
                        post_Contacts.enqueue(new Callback<List<Contacts>>() {
                            @Override
                            public void onResponse(Call<List<Contacts>> call, Response<List<Contacts>> response) {
                            }

                            @Override
                            public void onFailure(Call<List<Contacts>> call, Throwable t) {

                            }
                        });
                        dialog.dismiss();     //닫기
                    }
                });
                ad.setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                dialog.show();
            }
        });
        adapter.notifyDataSetChanged();
        listview.setAdapter(adapter);
        REF_MENU = adapter.phoneBooks;

        return view;
    }


//    public void contactAdd(final String name, final String phone_num){
//        new Thread(){
//            @Override
//            public void run() {
//                ArrayList<ContentProviderOperation> list = new ArrayList<>();
//                try{
//                    list.add(
//                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
//                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
//                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
//                                    .build()
//                    );
//                    list.add(
//                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
//                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)   //이름
//                                    .build()
//                    );
//                    list.add(
//                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_num)           //전화번호
//                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)
//                                    .build()
//                    );
//                    checkPermission();
//                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
//                    list.clear();   //리스트 초기화
//                }catch(RemoteException e){
//                    e.printStackTrace();
//                }catch(OperationApplicationException e){
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }

    public void checkPermission() {
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for (String permission : permission_list) {
            //권한 허용 여부를 확인한다.
            int chk = getActivity().checkCallingOrSelfPermission(permission);

            if (chk == PackageManager.PERMISSION_DENIED) {
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list, 0);
            }
        }
    }
}
