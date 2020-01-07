package com.example.pj2.tab3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pj2.R;
import com.example.pj2.tab2.File_list;
import com.example.pj2.tab2.NetworkClient;
import com.example.pj2.tab2.tab2_download_ServerConnection;
import com.facebook.AccessToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static android.view.View.VISIBLE;

public class ProblemViewAdapter extends BaseAdapter implements Filterable {

    public ArrayList<Problem> problemViewItems;

    int resourceId;
    Context context;
    String building, floor, problem;
    private boolean nowVisible;
    private ArrayList<Problem> filteredItemList;

    Filter listFilter;


    public ProblemViewAdapter(Context context, int resource, ArrayList<Problem> problemViewItems){
        this.context = context;
        this.resourceId = resource;
        this.nowVisible = false;
        getBoard();
        if(problemViewItems == null){
            this.problemViewItems = new ArrayList<Problem>();
        }
        else{
            this.problemViewItems = problemViewItems;
        }
        this.filteredItemList = problemViewItems;
    }
    @Override
    public int getCount(){
        return filteredItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.problem_listview_item, parent, false);
        }

        ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
        layoutParams.height = 100;
        convertView.setLayoutParams(layoutParams);

        TextView buildingname = (TextView) convertView.findViewById(R.id.textViewBuilding);
        TextView floor_name = (TextView) convertView.findViewById(R.id.textViewFloor);
        TextView problem_name = (TextView) convertView.findViewById(R.id.textViewContent);

//        Problem problemViewItem = problemViewItems.get(position);\
        Problem problemViewItem = filteredItemList.get(position);


//        building = problemList.get(pos).getBuilding();
//        floor = problemList.get(pos).getfloor();
//        problem = problemList.get(pos).getproblem();

        buildingname.setText(problemViewItem.getBuilding());
        floor_name.setText(problemViewItem.getfloor());
        problem_name.setText(problemViewItem.getproblem());

        final ImageButton problem_delbtn = (ImageButton) convertView.findViewById(R.id.problem_delbtn);
        problem_delbtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                deleteBoard(problemViewItems.get(pos));
                problemViewItems.remove(pos);
                notifyDataSetChanged();
                problem_delbtn.setVisibility(View.GONE);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (nowVisible) {
                    problem_delbtn.setVisibility(View.GONE);
                    nowVisible = false;
                }
                else {
                    problem_delbtn.setVisibility(VISIBLE);
                    nowVisible = true;
                }
                return true;
            }
        });

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public Object getItem(int position){
        return filteredItemList.get(position);
    }

    public void addItem(String building, String floor, String content){
        Problem problem = new Problem();

        problem.setBuilding(building);
        problem.setfloor(floor);
        problem.setproblem(content);

        problemViewItems.add(problem);
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter() ;
        }

        return listFilter ;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = problemViewItems ;
                results.count = problemViewItems.size() ;
            } else {
                ArrayList<Problem> itemList = new ArrayList<Problem>() ;

                for (Problem item : problemViewItems) {
                    if (item.getBuilding().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            item.getfloor().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                                item.getproblem().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<Problem>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }

    public ArrayList<Problem> getItemList(){
        return problemViewItems;
    }

    public interface ManageBoard {
        @HTTP(method = "DELETE", path = "/toilet/board", hasBody = true)
        Call<ResponseBody> deleteBoard(@Body Problem building);

        @HTTP(method = "POST", path = "/toilet/board", hasBody = true)
        Call<ResponseBody> postBoard(@Body Problem building);

        @POST("/download/view")
        Call downloadImages(@Part("user_id") RequestBody user_id, @Query("filename") RequestBody filename);
    }

    public void getBoard(){
        String request = "http://192.249.19.254:7280/toilet/board";
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("getBoard",response);
                        try{
                            JSONArray arr = new JSONArray(response);
                            int length = arr.length();
                            for(int i = 0; i < length; i++){
                                addItem(arr.getJSONObject(i).getString("Building"),
                                        arr.getJSONObject(i).getString("Floor"),
                                        arr.getJSONObject(i).getString("Problem"));
                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }

    public void deleteBoard(Problem DeleteBoard){

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        ManageBoard manageboard = retrofit.create(ManageBoard.class);

//        Log.d("userid",accessToken.getUserId());

        Call call = manageboard.deleteBoard(DeleteBoard);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("Deleteboard",response.toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void postBoard(Problem PostBoard){

        Retrofit retrofit = NetworkClient.getRetrofitClient(context);
        ManageBoard manageboard = retrofit.create(ManageBoard.class);


        Call call = manageboard.postBoard(PostBoard);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("PostBoard",response.toString());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
