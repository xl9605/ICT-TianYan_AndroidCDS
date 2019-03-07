package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ict.ac.humanmotion.uapplication.lpmsbs.model.OperationDetail;


/**
 * Created by Administrator on 2018/8/6 0006.
 */

public class OperationReplay extends Activity {

    private RecyclerView traceRv;
    private List<OperationDetail> mTraceList;
    private OperationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.operation_repplay);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initData();
        initRecyclerView();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //这里是模拟一些假数据，加载数据
    private void initData() {
        mTraceList = new ArrayList<>();
        OperationDetail op=new OperationDetail();
        op.setType(1);
        op.setOrder(1);
        op.setDetail("悬挂检修标牌");
        mTraceList.add(op);

        OperationDetail op2=new OperationDetail();
        op2.setType(1);
        op2.setOrder(2);
        op2.setDetail("闭合旁路开关");
        mTraceList.add(op2);

        OperationDetail op3=new OperationDetail();
        op3.setOrder(3);
        op3.setType(1);
        op3.setDetail("关闭柜门");
        mTraceList.add(op3);

    }

    //初始化显示物流追踪的RecyclerView
    private void initRecyclerView() {
        traceRv = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mAdapter = new OperationAdapter(this, mTraceList);
        traceRv.setLayoutManager(layoutManager);
        traceRv.setAdapter(mAdapter);
    }




}
