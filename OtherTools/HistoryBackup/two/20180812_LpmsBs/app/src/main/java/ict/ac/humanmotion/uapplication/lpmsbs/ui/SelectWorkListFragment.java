package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class SelectWorkListFragment extends FragmentActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worklist);
        listView = (ListView)findViewById(R.id.workListView);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new MyAdapter());
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        // TODO Auto-generated method stub
        intent = new Intent(SelectWorkListFragment.this, WorkListDetails.class);
        startActivity(intent);
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
    private int[] images = { R.drawable.worklist_logo, R.drawable.worklist_logo, R.drawable.worklist_logo,
            R.drawable.worklist_logo,R.drawable.worklist_logo,R.drawable.worklist_logo,R.drawable.worklist_logo,R.drawable.worklist_logo};
    private String[] names = { "工单01号", "工单02号", "工单03号", "工单04号", "工单05号", "工单06号" , "工单07号", "工单08号"};


    //自定义适配器

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return names.length;
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
           return names[position];
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            System.out.println("position=" + position);
            System.out.println(convertView);
            System.out.println("------------------------");
            ViewHolder viewHolder = new ViewHolder();
            //通过下面的条件判断语句，来循环利用。如果convertView = null ，表示屏幕上没有可以被重复利用的对象。
            if(convertView==null){
                //创建View
                convertView = getLayoutInflater().inflate(R.layout.worklist_items, null);
                viewHolder.iv = (ImageView) convertView.findViewById(R.id.worklistlogo);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.workListContent);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.iv.setImageResource(images[position]);
            viewHolder.tv.setText(names[position]);
            return convertView;
        }
    }
    static class ViewHolder{
        ImageView iv;
        TextView tv;
    }


}
