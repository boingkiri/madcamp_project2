package com.example.pj2.tab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pj2.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class ProblemViewAdapter extends BaseAdapter {

    public ArrayList<Problem> problemViewItems;

    int resourceId;
    Context context;
    String building, floor, problem;
    private boolean nowVisible;

    public ProblemViewAdapter(Context context, int resource, ArrayList<Problem> problemViewItems){
        this.context = context;
        this.resourceId = resource;
        this.nowVisible = false;
        if(problemViewItems == null){
            this.problemViewItems = new ArrayList<Problem>();
        }
        else{
            this.problemViewItems = problemViewItems;
        }
    }
    @Override
    public int getCount(){
        return problemViewItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.problem_listview_item, parent, false);
        }

        TextView buildingname = (TextView) convertView.findViewById(R.id.textViewBuilding);
        TextView floor_name = (TextView) convertView.findViewById(R.id.textViewFloor);
        TextView problem_name = (TextView) convertView.findViewById(R.id.textViewContent);

        Problem problemViewItem = problemViewItems.get(position);

//        building = problemList.get(pos).getBuilding();
//        floor = problemList.get(pos).getfloor();
//        problem = problemList.get(pos).getproblem();

        buildingname.setText(problemViewItem.getBuilding());
        floor_name.setText(problemViewItem.getfloor());
        problem_name.setText(problemViewItem.getproblem());

//        final ImageButton button3 = (ImageButton) convertView.findViewById(R.id.delbtn);
//        button3.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                phoneBooks.remove(pos);
//                notifyDataSetChanged();
//                button3.setVisibility(View.GONE);
//            }
//        });
//        convertView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (nowVisible) {
//                    button3.setVisibility(View.GONE);
//                    nowVisible = false;
//                }
//                else {
//                    button3.setVisibility(VISIBLE);
//                    nowVisible = true;
//                }
//                return true;
//            }
//        });

        return convertView;
    }

    @Override
    public long getItemId(int position){
        return position;
    }
    @Override
    public Object getItem(int position){
        return problemViewItems.get(position);
    }

    public void addItem(String building, String floor, String content){
        Problem problem = new Problem();

        problem.setBuilding(building);
        problem.setfloor(floor);
        problem.setproblem(content);

        problemViewItems.add(problem);
    }

    public ArrayList<Problem> getItemList(){
        return problemViewItems;
    }
}
