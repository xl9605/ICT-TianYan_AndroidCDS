package ict.ac.humanmotion.uapplication.lpmsbs.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class NewWorkList extends Activity implements OnClickListener{
    Intent intent;
    private Button btn_inputByOCR, btn_inputByEntry, btn_Cancel;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttommenue_layout);
        btn_inputByOCR = (Button) this.findViewById(R.id.btn_inputByOCR);
        btn_inputByEntry = (Button) this.findViewById(R.id.btn_inputByEntry);
        btn_Cancel = (Button) this.findViewById(R.id.btn_Cancel);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_inputByOCR.setOnClickListener(this);
        btn_inputByEntry.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        this.overridePendingTransition(0, R.anim.push_buttom_out);
        return true;
    }
    public void onClick(View v) {
        this.overridePendingTransition(0, R.anim.push_bottom_in);
        switch (v.getId()) {
            case R.id.btn_inputByOCR:
                Toast.makeText(getApplicationContext(),"调用摄像头进行OCR识别", Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_inputByEntry:
                intent = new Intent(NewWorkList.this, InputWorkList.class);
                startActivity(intent);
                break;
            case R.id.btn_Cancel:
                break;
            default:
                break;
        }
        finish();
    }

}