package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class InputWorkList extends FragmentActivity {
    Button btn_Cononfirm,btn_Cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputworklist);
        btn_Cononfirm = (Button) findViewById(R.id.btn_Confirm);
        btn_Cancel = (Button) findViewById(R.id.btn_Cancel);
        btn_Cononfirm.setOnClickListener(new MyListener());
        btn_Cancel.setOnClickListener(new MyListener());
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.btn_Confirm:
                    Toast.makeText(getApplicationContext(),"确认数据并传送到数据库", Toast.LENGTH_LONG).show();
                    break;
                case R.id.btn_Cancel:
                    Toast.makeText(getApplicationContext(),"取消", Toast.LENGTH_LONG).show();
                    break;
                case android.R.id.home:
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
