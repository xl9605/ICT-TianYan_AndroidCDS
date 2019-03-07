package ict.ac.humanmotion.uapplication.lpmsbs.hworkadapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ict.ac.humanmotion.uapplication.lpmsbs.model.WorkListDetail;
import ict.ac.humanmotion.uapplication.lpmsbs.ui.OperationReplay;
import ict.ac.humanmotion.uapplication.lpmsbs.ui.R;

/**
 * Created by hbh on 2017/4/20.
 * 子布局ViewHolder
 */

public class ChildViewHolder extends BaseViewHolder {

    private Context mContext;
    private View view;
    private TextView child_jperson;
    private TextView child_sperson;
    private TextView child_stime;
    private TextView child_time;
    private TextView child_ftime;
    private LinearLayout child_container;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final WorkListDetail dataBean, final int pos, final ItemClickListener listener){

        child_jperson = (TextView) view.findViewById(R.id.child_jperson);
        child_sperson = (TextView) view.findViewById(R.id.child_sperson);
        child_stime = (TextView) view.findViewById(R.id.child_stime);
        child_time = (TextView) view.findViewById(R.id.child_time);
        child_ftime = (TextView) view.findViewById(R.id.child_ftime);

        child_jperson.setText(dataBean.getSupervisor());
        child_sperson.setText(dataBean.getSupervisor());
        child_stime.setText(dataBean.getStartTime());
        child_time.setText(dataBean.getTime());
        child_ftime.setText(dataBean.getFinishTime());

        child_container=(LinearLayout)view.findViewById(R.id.child_container);

        child_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    Intent intent=new Intent(view.getContext(),OperationReplay.class);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }
}
