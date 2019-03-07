package ac.ict.humanmotion.abracadabra

import ac.ict.humanmotion.abracadabra.HTTPAround.MyStringObserver
import ac.ict.humanmotion.abracadabra.WorkList.global.windowView
import ac.ict.humanmotion.abracadabra.tools.LoadingDialogUtils
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.work_input.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.concurrent.thread
import android.view.WindowManager
import android.view.View
import android.widget.Button
import io.reactivex.disposables.Disposable


class WorkList : BaseActivity(){
    private var btn_send: Button? = null
    private var loading: Dialog? = null
    private var dialog:ProgressDialog?=null
    private var pd: ProgressDialog? = null

    object global {
        var windowView: View? = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (Settings.canDrawOverlays(this)) {
                        //若用户开启了overlay权限,则打开window
                        openWindow()
                    } else {
                        Toast.makeText(this, "不开启overlay权限", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if(resultCode == Activity.RESULT_OK){
            when (requestCode) {
              // CROP_REQUEST_CODE -> setUriToView()
               CAMERA_REQUEST_CODE -> clipPhoto()
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            startActivity(Intent(baseContext, WorkList::class.java))
            finish()
        }

    }

  /*  // 创建对话框
    override fun onCreateDialog(id: Int, args: Bundle): Dialog? {
        when (id) {
            PROGRESS_DIALOG -> {
                // 创建进度对话框
                pd = ProgressDialog(this)
                pd?.setMax(100)
                // 设置对话框标题
                pd?.setTitle("任务完成百分比")
                // 设置对话框显示的内容
                pd?.setMessage("下载完成的百分比")
                // 设置对话框不能用取"消按"钮关闭
                pd?.setCancelable(false)
                // 设置对话框的进度条风格
                // pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                // 设置对话框的进度条是否显示进度
                pd.setIndeterminate(false)
            }
        }
        return pd
    }
*/
    //需要修改,回调判断
    private fun setUriToView() {
        /*dialog?.setTitle("这是一个 progressDialog");//2.设置标题
        dialog?.setMessage("正在加载中，请稍等......");//3.设置显示内容
        dialog?.show();//5.将ProgessDialog显示出来*/
        // cameraView.setCvCameraViewListener(this)
       thread {


            val file = File("/storage/emulated/0/tessdata/OUTPUT.jpg")
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            println("UPLOADING")

           val sharedPreferences = getSharedPreferences("work", Context.MODE_PRIVATE)
           val name=sharedPreferences.getString("filename","")
           /* val sharedPreferences = getSharedPreferences("work", Context.MODE_PRIVATE);
           //getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
           val name = sharedPreferences.getString("filename", "");
           showToast(name)*/
          // showToast(name)

            cloudAPI.uploadOCR(name,body).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyStringObserver() {
                        override fun onNext(t: String) {
                            println("OCCR" + t)
                            showToast("工单照片上传成功")
                            startActivity(Intent(baseContext, MainActivity::class.java))
                            finish()
                        }

                        override fun onError(e: Throwable) {
                            //startActivity(Intent(baseContext, MainActivity::class.java))
                            showToast("工单照片上传失败！！请重新上传！！"+e.message)
                            //finish()
                        }

//                        override fun onComplete() {
//                            super.onComplete()
//                            loading = LoadingDialogUtils.createLoadingDialog(basec, "加载中...")
//
//                        }
                    })

            runOnUiThread {
                showToast("工单照片正在上传...")
                //showToast("处理中...")
                //showToast("请等待...")
            }
      //      startActivity(Intent(baseContext, MainActivity::class.java))

//            finish()
        }
    }
    private fun clipPhoto() {
        windowManager.removeView(global.windowView)


       // progressDialog.setCancelable(true);//4.设置可否用back键关闭对话框


        setUriToView()
        //startActivity(Intent(baseContext, MainActivity::class.java))
        //finish()
    }

    override val layoutId: Int
        get() = R.layout.work_input

    override fun init() {
       // getStorageAccessPermissions()

        initRxJava()
        dialog=ProgressDialog(baseContext)//1.创建一个ProgressDialog的实例

        //dialog.setTitle("这是一个 progressDialog");//2.设置标题
        //dialog.setMessage("正在加载中，请稍等......");//3.设置显示内容
       // dialog.show();//5.将ProgessDialog显示出来*/
       // cameraView.setCvCameraViewListener(this)

        cover_user.setOnClickListener {
            thread {

                startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        .putExtra(MediaStore.EXTRA_OUTPUT, imageUri), CAMERA_REQUEST_CODE)
                //finish()
                }
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //版本大于6.0则需要判断是否获取了overlays权限
                if (!Settings.canDrawOverlays(this@WorkList)) {
                    startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:$packageName")), 1)
                } else
                    openWindow()
            }
        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        const val PERMISSION_TAG = "RequestPermissions"
        const val CAMERA_REQUEST_CODE = 10086
        const val CROP_REQUEST_CODE = 10085
        const val RES_REQUEST_CODE = 10000
        const val rex = "/*-+)(<>'\\~!@$%&^ -:;[]{}「『…【】_《》oo′\"`\'“”‘’,."

        val outUri = Uri.parse("file:///storage/emulated/0/tessdata/output.jpg")

        val imageUri = Uri.parse("file:///storage/emulated/0/tessdata/output.jpg")
    }


    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    private fun openWindow() {
        //获取WindowManager实例
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        //获取窗口布局参数实例
        val params = WindowManager.LayoutParams()
       /* //设置窗口布局参数属性
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        //设置window的显示特性
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        //设置窗口半透明
        params.format = PixelFormat.TRANSLUCENT
        //设置窗口类型
        params.type = WindowManager.LayoutParams.TYPE_PHONE*/



        // 靠手机屏幕的左边居中显示
		//params.gravity = Gravity.CENTER | Gravity.LEFT;

		params.type = WindowManager.LayoutParams.TYPE_PHONE
		params.format = PixelFormat.RGBA_8888

		// 如果设置以下属性，那么该悬浮窗口将不可触摸，不接受输入事件，不影响其他窗口事件的传递和分发
		// params.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		// |LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE

		// 可以设定坐标
		// params.x=xxxx
		// params.y=yyyy

		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

		// 透明度
		// params.alpha=0.8f

		params.width = WindowManager.LayoutParams.WRAP_CONTENT
		params.height = WindowManager.LayoutParams.WRAP_CONTENT

        //获取窗口布局实例
        windowView = View.inflate(this, R.layout.floatwindow, null)
        //获取窗口布局中的按钮实例
        //btn_send = windowView.findViewById<View>(R.id.btn_send) as Button
        //设置点击事件
        //btn_send?.setOnClickListener(View.OnClickListener { windowManager.removeView(windowView) })
        //显示窗口
        windowManager.addView(global.windowView, params)
    }


}
