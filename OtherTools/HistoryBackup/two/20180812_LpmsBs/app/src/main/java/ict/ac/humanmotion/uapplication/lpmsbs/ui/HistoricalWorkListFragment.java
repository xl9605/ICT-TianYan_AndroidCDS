package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ict.ac.humanmotion.uapplication.lpmsbs.hworkadapter.RecyclerAdapter;
import ict.ac.humanmotion.uapplication.lpmsbs.model.WorkListDetail;


/**
 * Created by Carson_Ho on 16/5/23.
 */
public class HistoricalWorkListFragment extends Fragment{
        private RecyclerView mRecyclerView;
        private List<WorkListDetail> dataBeanList;
        private WorkListDetail dataBean;
        private RecyclerAdapter mAdapter;
        private ImageView imageView;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historicalworklist, null);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
            imageView=(ImageView)view.findViewById(R.id.img);
            initData();
         //   imageView.setOnClickListener(View);


        return view;
    }
        /**
         * 模拟数据
         */
        private void initData(){
            dataBeanList = new ArrayList<>();
            for (int i = 1; i <= 50; i++) {
                dataBean = new WorkListDetail();
                dataBean.setID(i+"");
                dataBean.setType(0);
                dataBean.setWorkListName("工单名称"+i);
                dataBean.setFinishTime("完成时间：2018-08-2 9:02");
                dataBean.setTime("2分钟");
                dataBean.setStartTime("起始时间：2018-08-2 9:02");
                dataBean.setOprationPerson("张三");
                dataBean.setSupervisor("张三");
                dataBean.setChildBean(dataBean);
                dataBeanList.add(dataBean);
            }
            setData();
        }

        private void setData(){
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            mAdapter = new RecyclerAdapter(this.getActivity(),dataBeanList);
            mRecyclerView.setAdapter(mAdapter);
            //滚动监听
            mAdapter.setOnScrollListener(new RecyclerAdapter.OnScrollListener() {
                @Override
                public void scrollTo(int pos) {
                    mRecyclerView.scrollToPosition(pos);
                }
            });
        }
    }

