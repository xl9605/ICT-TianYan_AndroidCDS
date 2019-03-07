package ac.ict.humanmotion.abracadabra

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class ClipActivity : Activity() {
    // 该程序模拟填充长度为100的数组
    private val data = IntArray(100)
    private var hasData = 0
    // 定义进度对话框的标识
    private val PROGRESS_DIALOG = 0x112
    // 记录进度对话框完成的百分比
    private var progressStatus = 0
    // 定义一个进度对话框对象
    private var pd: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        val btn = findViewById<View>(R.id.btn) as Button
        btn.setOnClickListener {
            progressStatus = 0
            hasData = 0
            // 显示指定对话框
            showDialog(PROGRESS_DIALOG)
        }
    }

    // 创建对话框
    override fun onCreateDialog(id: Int, args: Bundle): Dialog? {
        when (id) {
            PROGRESS_DIALOG -> {
                // 创建进度对话框
                pd = ProgressDialog(this)
                pd!!.max = 100
                // 设置对话框标题
                pd!!.setTitle("任务完成百分比")
                // 设置对话框显示的内容
                pd!!.setMessage("下载完成的百分比")
                // 设置对话框不能用取"消按"钮关闭
                pd!!.setCancelable(false)
                // 设置对话框的进度条风格
                // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                // 设置对话框的进度条是否显示进度
                pd!!.isIndeterminate = false
            }
        }
        return pd
    }

    // 该方法将在onCreateDialog调用后被回调
    override fun onPrepareDialog(id: Int, dialog: Dialog) {

        super.onPrepareDialog(id, dialog)
        when (id) {
            PROGRESS_DIALOG -> {
                // 对话框进度清零
                pd!!.incrementProgressBy(-pd!!.progress)
                object : Thread() {
                    override fun run() {
                        while (progressStatus < 100) {
                            // 获取耗时任务完成的百分比
                            progressStatus = doWork()
                            // 运行于UI线程
                            runOnUiThread {
                                // 设置进度条的进度
                                pd!!.progress = progressStatus
                            }
                        }
                        // 如果任务已经完成
                        if (progressStatus >= 100) {
                            // 关闭对话框
                            pd!!.dismiss()
                        }
                    }

                }.start()
            }
        }
    }

    /**
     * 模拟一个耗时的操作
     *
     * @return
     */
    fun doWork(): Int {
        // 为数组元素赋值
        data[hasData++] = (Math.random() * 100).toInt()
        try {
            Thread.sleep(100)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return hasData
    }
}

