package ac.ict.humanmotion.abracadabra

import ac.ict.humanmotion.abracadabra.Bean.Info
import ac.ict.humanmotion.abracadabra.HTTPAround.MyStringObserver
import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_front_camera.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs.imwrite
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class FrontCameraActivity : BaseActivity(), CameraBridgeViewBase.CvCameraViewListener2 {


    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        inputFrame?.rgba()?.let {
            //            selfBinary(it.nativeObjAddr, mRgba.nativeObjAddr)
            mRgba = it
        }

        return mRgba
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        println("onCameraViewStarted")
    }

    override fun onCameraViewStopped() {
        mRgba.release()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            println("FAIL")
        } else cameraView.enableView()
    }

    override fun onPause() {
        super.onPause()
        if (cameraView != null) {
            cameraView.disableView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraView != null) {
            cameraView.disableView()
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_front_camera

    override fun init() {
        // getStorageAccessPermissions()

        initRxJava()
        cameraView.setCvCameraViewListener(this)

        cameraFab.setOnClickListener {

            thread {
                val tempMat: Mat = mRgba

                Imgproc.cvtColor(mRgba, tempMat, Imgproc.COLOR_RGBA2BGR)
                Core.transpose(tempMat,tempMat)
                Core.flip(tempMat, tempMat, 1)

                imwrite("/storage/emulated/0/tessdata/result.jpg", tempMat)

                val file = File("/storage/emulated/0/tessdata/RESULT.jpg")
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

                println("UPLOADING")

                cloudAPI.uploadFace(body).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : MyStringObserver() {
                            //成功后提示
                            override fun onNext(t: String) {
                                /*println("OCCR" + t)*/
                                // showToast(t)
                                //  val fir = t.toCharArray().get(0)
                                // showToast(t[0]+"")
                                if (t[0] != 'N') {
                                    //sharedPreferences=getSharedPreferences("")

                                    val sharedPreferences = getSharedPreferences("work", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putString("filename", t)
                                    editor.commit()

                                    val intent = Intent()
                                    intent.setClass(baseContext, WorkList::class.java)
                                    startActivity(intent)
                                    showToast("图片上传成功")

                                } else {
                                    showToast("图片上传失败，请重新拍照")
                                    intent.setClass(baseContext, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                finish()
                            }

                            //错误情况下
                            override fun onError(e: Throwable) {
                                showToast("图片上传失败，请重新拍照" + e.message)
                                intent.setClass(baseContext, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
                //  saveResult("/storage/emulated/0/tessdata/result.jpg",bitmap)
                runOnUiThread {

                    //  showToast("图片大小是 ${tempMat.size().height * tempMat.size().width}...")
                    showToast("处理中...")
                    showToast("图片正在上传...")
                    showToast("请等候...")
                }


            }


        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        const val PERMISSION_TAG = "RequestPermissions"

        const val RES_REQUEST_CODE = 10000


    }


    private lateinit var mRgba: Mat


    private fun showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    @TargetApi(23)
    private fun getStorageAccessPermissions() {
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), RES_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("$PERMISSION_TAG:onRequestPermissionsResult: ${grantResults[0]}")
        when (requestCode) {
            RES_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) println("$PERMISSION_TAG:permission get!") else {
                println("$PERMISSION_TAG:permission denied! ")
                finish()
            }
        }
    }

}