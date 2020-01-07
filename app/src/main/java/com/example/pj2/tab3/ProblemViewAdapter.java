package com.example.pj2.tab3;

import android.content.Context;
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

import com.example.pj2.R;

import java.util.ArrayList;
import java.util.List;

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
}
