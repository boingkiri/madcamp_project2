package com.example.pj2.tab3;

import android.content.DialogInterface;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.pj2.MainActivity;
import com.example.pj2.tab1.Net;
import com.example.pj2.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class tab3 extends Fragment implements OnMapReadyCallback {

    ProblemViewAdapter adapter;
    private List<String> list;
    private ArrayList<String> arraylist;
    ArrayList<Problem> PROBLEM = new ArrayList<Problem>();
    GoogleMap mMap;
    MapView mapView;
    private EditText editSearch;
    private boolean mapsSupported = true;
    View view;
    double mLatitude = MainActivity.latitude;
    double mLongitude = MainActivity.longitude;
    ListView listView = null;


    public tab3() {
        // Required empty public constructor
        Problem first_prob = new Problem();
        first_prob.setBuilding("Building");
        first_prob.setfloor("Floor");
        first_prob.setproblem("Content");
        PROBLEM.add(first_prob);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab3, container, false);


        // 리스트 생성
        list = new ArrayList<String>();
        if(PROBLEM != null){
            settingList();
        }
        arraylist = new ArrayList<String>();
        arraylist.addAll(list);

        /// 리스트뷰 어댑터 연결
        adapter = new ProblemViewAdapter(getActivity(), R.layout.problemlist_item, PROBLEM);
        listView = view.findViewById(R.id.tab3_listview);
        listView.setAdapter(adapter);

        editSearch = (EditText)view.findViewById(R.id.editSearch) ;
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView.setFilterText(filterText) ;
                } else {
                    listView.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;


        Button register = (Button) view.findViewById(R.id.register);
//        Button complete = (Button) view.findViewById(R.id.solve);
        final Button emergency = (Button) view.findViewById(R.id.emergency);

        register.setOnClickListener(new View.OnClickListener() { // 클릭시 등록 다이얼로그 생성
            @Override
            public void onClick(View v) {
                // 다이얼로그 등록 내용을 통해 리스트뷰에 업로드
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.register_dialog, null);
                ad.setView(view);
                ad.setTitle("문제 사항 등록");
                ad.setMessage("건물명 / 층수 / 내용을 입력해주세요");

                final EditText edit1 = (EditText) view.findViewById(R.id.edittext_building); // 건물이름등록
                final EditText edit2 = (EditText) view.findViewById(R.id.edittext_floor); // 층수 등록
                final EditText edit3 = (EditText) view.findViewById(R.id.edittext_content); // 내용 등록
                final Button submit = (Button) view.findViewById(R.id.register_submit); // 버튼 누르면 등록

                final AlertDialog dialog = ad.create();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Problem problem = new Problem();

                        final String building = edit1.getText().toString();
                        final String floor = edit2.getText().toString();
                        final String problem_content = edit3.getText().toString();

                        problem.setBuilding(building);
                        problem.setfloor(floor);
                        problem.setproblem(problem_content);

                        adapter.problemViewItems.add(problem); // 입력한 건물, 층, 문제를 리스트뷰에 넣어주기
                        adapter.postBoard(problem);
                        dialog.dismiss(); // 닫기
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


        emergency.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Problem send_emergency = new Problem();
                AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.register_dialog, null);
                ad.setView(view);
                ad.setTitle("문제 사항 등록");
                ad.setMessage("건물명 / 층수 / 내용을 입력해주세요");

                final EditText edit1 = (EditText) view.findViewById(R.id.edittext_building); // 건물이름등록
                final EditText edit2 = (EditText) view.findViewById(R.id.edittext_floor); // 층수 등록
                final EditText edit3 = (EditText) view.findViewById(R.id.edittext_content); // 내용 등록
                final Button submit = (Button) view.findViewById(R.id.register_submit); // 버튼 누르면 등록

                final AlertDialog dialog = ad.create();
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Problem problem = new Problem();

                        final String building = edit1.getText().toString();
                        final String floor = edit2.getText().toString();
                        final String problem_content = edit3.getText().toString();

                        send_emergency.setBuilding(building);
                        send_emergency.setfloor(floor);
                        send_emergency.setproblem(problem_content);


                        problem.setBuilding(building);
                        problem.setfloor(floor);
                        problem.setproblem(problem_content);

                        adapter.problemViewItems.add(problem); // 입력한 건물, 층, 문제를 리스트뷰에 넣어주기
                        Call<Problem> emergency_Problem = Net_tab3.getInstance().getRetro().emergency_Problem(send_emergency);
                        emergency_Problem.enqueue(new Callback<Problem>() {
                            @Override
                            public void onResponse(Call<Problem> call, Response<Problem> response) {
                            }

                            @Override
                            public void onFailure(Call<Problem> call, Throwable t) {

                            }
                        });

                        dialog.dismiss(); // 닫기
                    }
                });
                ad.setNegativeButton("취소하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                    }
                });
                dialog.show();
                // 긴급 알림 전송 버튼 - 앱의 푸시알림을 통해서 알림을 보냄
                ///toilet/emergency - POST 형식으로 body는 problem 안에 있는것들 보내기

                // 클릭 시 긴급요청을 한 정보만 리퀘스트로 가도록 하기
                // 리스트에도 들어가게 하기
            }
        });
        adapter.notifyDataSetChanged();

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(@NonNull View mview, @Nullable Bundle savedInstanceState){
        super.onViewCreated(mview, savedInstanceState);

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override //구글맵을 띄울준비가 됬으면 자동호출된다.
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        System.out.println("666666666666666666" + mLatitude + " " + mLongitude);
        LatLng myloca = new LatLng(mLatitude, mLongitude);
        googleMap.addMarker(new MarkerOptions().position(myloca).title("카이스트"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloca, 16));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate){
        super.onSaveInstanceState(outstate);
        mapView.onSaveInstanceState(outstate);
    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();
            System.out.println("2222222222222222222" + longitude);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };




    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    private void settingList(){
        for(int i = 0; i < PROBLEM.size(); i++){
            list.add(PROBLEM.get(i).getBuilding());
        }
    }



}


