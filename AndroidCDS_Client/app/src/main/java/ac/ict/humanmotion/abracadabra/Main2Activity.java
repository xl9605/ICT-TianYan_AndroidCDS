package ac.ict.humanmotion.abracadabra;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main2Activity extends Activity {
    // 该程序模拟填充长度为100的数组
    private int[] data = new int[100];
    private int hasData = 0;
    // 定义进度对话框的标识
    private final int PROGRESS_DIALOG = 0x112;
    // 记录进度对话框完成的百分比
    private int progressStatus = 0;
    // 定义一个进度对话框对象
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                progressStatus = 0;
                hasData = 0;
                // 显示指定对话框
                showDialog(PROGRESS_DIALOG);
            }
        });
    }

    // 创建对话框
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        switch (id) {
            case PROGRESS_DIALOG:
                // 创建进度对话框
                pd = new ProgressDialog(this);
                pd.setMax(100);
                // 设置对话框标题
                pd.setTitle("任务完成百分比");
                // 设置对话框显示的内容
                pd.setMessage("下载完成的百分比");
                // 设置对话框不能用取"消按"钮关闭
                pd.setCancelable(false);
                // 设置对话框的进度条风格
                // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                // 设置对话框的进度条是否显示进度
                pd.setIndeterminate(false);
                break;
        }
        return pd;
    }

    // 该方法将在onCreateDialog调用后被回调
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        super.onPrepareDialog(id, dialog);
        switch (id) {
            case PROGRESS_DIALOG:
                // 对话框进度清零
                pd.incrementProgressBy(-pd.getProgress());
                new Thread() {
                    public void run() {
                        while (progressStatus < 100) {
                            // 获取耗时任务完成的百分比
                            progressStatus = doWork();
                            // 运行于UI线程
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    // 设置进度条的进度
                                    pd.setProgress(progressStatus);
                                }
                            });
                        }
                        // 如果任务已经完成
                        if (progressStatus >= 100) {
                            // 关闭对话框
                            pd.dismiss();
                        }
                    }

                }.start();
                break;
        }
    }

    /**
     * 模拟一个耗时的操作
     *
     * @return
     */
    public int doWork() {
        // 为数组元素赋值
        data[hasData++] = (int) (Math.random() * 100);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return hasData;
    }
}

