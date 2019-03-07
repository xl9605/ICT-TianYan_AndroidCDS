package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WorkListOperationFragment extends Fragment {
    Button selectWorkList, newWorkList;
    Intent intent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.worklistoperation, null);

        selectWorkList = (Button)view.findViewById(R.id.select_work_list);
        newWorkList = (Button)view.findViewById(R.id.new_work_list);
        selectWorkList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(getActivity(), SelectWorkListFragment.class);
                startActivity(intent);
            }
        });
        newWorkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(getActivity(),NewWorkList.class));
            }
        });
        return view;
    }
}


