package com.example.pj2.tab1;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
    final List<String> LIST_MENU = MainActivity.names;
    List<PhoneBook> REF_MENU = MainActivity.phoneBooks;
    ListViewAdapter adapter;
    String[] permission_list = { Manifest.permission.WRITE_CONTACTS };

    public Fragment1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, null);
        checkPermission();

        new JSONTask().execute("http://192.249.19.254:7080/Contacts");

        adapter = new ListViewAdapter(getActivity(), R.layout.listview_btn_item, REF_MENU);

        final ListView listview = (ListView) view.findViewById(R.id.listview1);
        ImageButton add_button = (ImageButton) view.findViewById(R.id.add_btn);
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

                        contactAdd(full_name, phone_number);
                        adapter.phoneBooks.add(add_phone);
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

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONArray jsonArray = new JSONArray();
//                List<PhoneBook> phoneBook = new ArrayList<PhoneBook>();

                for(int i = 0; i < REF_MENU.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("name", REF_MENU.get(i).getName());
                    jsonObject.accumulate("phone", REF_MENU.get(i).getTel());
                    jsonArray.put(jsonObject);
                    System.out.println(REF_MENU.get(i).getName());
                    System.out.println(REF_MENU.get(i).getTel());
                }

                System.out.println("55555555555555555555555" + jsonArray);

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.249.19.254/Contacts");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();

                    System.out.println("111111111111111111111111111111111222222" + con);


                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송


                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미

                    con.connect();

                    System.out.println("22222222222222222222222222222222" );

                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));

                    writer.write(jsonArray.toString()); /////////////////////////////여기부터
                    System.out.println("3333333333333333333333333333333333"+ jsonArray.toString());

                    writer.flush();
                    writer.close();//버퍼를 받아줌

                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();

                    System.out.println("44444444444444444444444444444444" + stream);

                    reader = new BufferedReader(new InputStreamReader(stream));

                    System.out.println("5555555555555555555555555555555555555" + reader);


                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public void contactAdd(final String name, final String phone_num){
        new Thread(){
            @Override
            public void run() {
                ArrayList<ContentProviderOperation> list = new ArrayList<>();
                try{
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build()
                    );
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name)   //이름
                                    .build()
                    );
                    list.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_num)           //전화번호
                                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE  , ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)   //번호타입(Type_Mobile : 모바일)
                                    .build()
                    );
                    checkPermission();
                    getActivity().getContentResolver().applyBatch(ContactsContract.AUTHORITY, list);  //주소록추가
                    list.clear();   //리스트 초기화
                }catch(RemoteException e){
                    e.printStackTrace();
                }catch(OperationApplicationException e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

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
