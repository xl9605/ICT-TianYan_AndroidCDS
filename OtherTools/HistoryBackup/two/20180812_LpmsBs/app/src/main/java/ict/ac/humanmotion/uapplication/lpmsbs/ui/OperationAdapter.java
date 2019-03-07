package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ict.ac.humanmotion.uapplication.lpmsbs.model.OperationDetail;


/**
 *
 * <p>
 * 作者： 周旭 on 2017/5/24/0024.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class OperationAdapter extends RecyclerView.Adapter<OperationAdapter.OperationViewHolder> {

    private static final int TYPE_CURR = 0; //进行中
    private static final int TYPE_NORMAL = 1; //不进行
    private Context mContext;
    private List<OperationDetail> mList;
    private LayoutInflater inflater;

    public OperationAdapter(Context mContext, List<OperationDetail> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public OperationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OperationViewHolder(inflater.inflate(R.layout.operation_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final OperationViewHolder holder, int position){
        //设置相关数据
        final OperationDetail trace = mList.get(position);
        final int type = trace.getType();
        final int pos=position;
        final int order = trace.getOrder();;
        if (type == TYPE_CURR) {
            holder.order.setTextColor(mContext.getResources().getColor(R.color.color_c03));
            holder.dot.setImageResource(R.mipmap.dot_red);
            holder.detail.setTextColor(mContext.getResources().getColor(R.color.color_c03));
            holder.img.setImageResource(R.mipmap.dot_red);
            holder.change.setTextColor(mContext.getResources().getColor(R.color.color_c03));
            holder.change.setText("结束");
        } else if (type == TYPE_NORMAL) {
            holder.order.setTextColor(mContext.getResources().getColor(R.color.color_6));
            holder.dot.setImageResource(R.mipmap.dot_black);
            holder.detail.setTextColor(mContext.getResources().getColor(R.color.color_6));
            holder.img.setImageResource(R.mipmap.dot_black);
            holder.change.setTextColor(mContext.getResources().getColor(R.color.color_6));
            holder.change.setText("开始");
        }

        holder.order.setText("第"+order+"步");
        holder.detail.setText(trace.getDetail());
       /* if (position == mList.size() - 1) {
            //最后一条数据，隐藏时间轴的竖线和水平的分割线
            holder.timeLineView.setVisibility(View.INVISIBLE);
            holder.dividerLineView.setVisibility(View.INVISIBLE);
        }*/

        holder.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (mList.get(pos).getType() == TYPE_NORMAL) {
                        //   trace.setType(0);
                        int count = 0;
                        for (int i = 0; i < mList.size(); i++) {
                            if (mList.get(i).getType() == 0)
                                count++;
                        }
                        if(count<1) {
                            mList.get(pos).setType(0);
                            holder.order.setTextColor(mContext.getResources().getColor(R.color.color_c03));
                            holder.dot.setImageResource(R.mipmap.dot_red);
                            holder.detail.setTextColor(mContext.getResources().getColor(R.color.color_c03));
                            holder.img.setImageResource(R.mipmap.dot_red);
                            holder.change.setTextColor(mContext.getResources().getColor(R.color.color_c03));
                            holder.change.setText("结束");
                        }else {
                            Toast.makeText(view.getContext(),"请结束当前操作动作，然后在进行其他操作",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //  trace.setType(1);
                        mList.get(pos).setType(1);

                        holder.order.setTextColor(mContext.getResources().getColor(R.color.color_6));
                        holder.dot.setImageResource(R.mipmap.dot_black);
                        holder.detail.setTextColor(mContext.getResources().getColor(R.color.color_6));
                        holder.img.setImageResource(R.mipmap.dot_black);
                        holder.change.setTextColor(mContext.getResources().getColor(R.color.color_6));
                        holder.change.setText("开始");
                    }
                 /*else {
                    Toast.makeText(view.getContext(),"请结束当前操作动作，然后在进行其他操作",Toast.LENGTH_SHORT).show();
                }*/
            }
        });



    }



    public class OperationViewHolder extends RecyclerView.ViewHolder {

        private TextView order;  //顺序
        private TextView detail;  //操作项目详情
        private TextView change;  //开始与结束
        private ImageView dot; //当前位置
        private ImageView img; //开始与结束

        private View dividerLineView; //时间轴的竖线
        private View timeLineView; //水平的分割线


        public OperationViewHolder(View itemView) {
            super(itemView);
            order = (TextView) itemView.findViewById(R.id.accept_station_tv);
            detail = (TextView) itemView.findViewById(R.id.accept_station);
            change = (TextView) itemView.findViewById(R.id.btn);
            dot = (ImageView) itemView.findViewById(R.id.dot_iv);
            img = (ImageView) itemView.findViewById(R.id.dot);

            dividerLineView = itemView.findViewById(R.id.divider_line_view);
            timeLineView = itemView.findViewById(R.id.time_line_view);

        }
    }
}
