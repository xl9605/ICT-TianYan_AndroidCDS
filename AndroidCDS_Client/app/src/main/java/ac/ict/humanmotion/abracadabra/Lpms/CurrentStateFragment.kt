package ac.ict.humanmotion.abracadabra.Lpms

import ac.ict.humanmotion.abracadabra.Bean.Info
import ac.ict.humanmotion.abracadabra.Bean.SVMDataBean
import ac.ict.humanmotion.abracadabra.Interface.CloudAPI
import ac.ict.humanmotion.abracadabra.Interface.URLAPI
import ac.ict.humanmotion.abracadabra.LoginActivity
import ac.ict.humanmotion.abracadabra.MainActivity
import ac.ict.humanmotion.abracadabra.R
import ac.ict.humanmotion.abracadabra.R.id.data
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import kotlinx.android.synthetic.main.state_fragment.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import android.R.attr.action
import android.support.v4.app.NotificationCompat.getExtras
import ac.ict.humanmotion.abracadabra.R.id.textView
import android.app.AlertDialog
import android.content.*
import android.graphics.Color
import android.os.SystemClock
import android.util.Log
import android.widget.*
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CurrentStateFragment : MyFragment()/*, Callback<String> */ {

    val url = URLAPI()

    override var myFragmentTag: Int = 2 /*FRAGMENT_TAG*/

    private lateinit var stateView: TextView

    private var dataCounter = 0
    internal var sharedPreferences: SharedPreferences? = null
    internal var cabinetnumber: TextView? = null

    companion object {
        init {
            System.loadLibrary("native-lib")
        }

        const val FRAGMENTTAG = "ConnectionFragment"
        val action = "jason.broadcast.action"
//        var globalData = LpmsBData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.startoperate, container, false)
        stateView = rootView.findViewById(R.id.data)
        val filter = IntentFilter(MainActivity.action)
        context?.registerReceiver(broadcastReceiver, filter)

        sharedPreferences = getContext()?.getSharedPreferences("work", Context.MODE_PRIVATE)
        cabinetnumber = rootView.findViewById(R.id.cabinetnumber) as TextView
        cabinetnumber?.setText(sharedPreferences?.getString("device_code", ""))
        var timer = rootView.findViewById<View>(R.id.chronometer) as Chronometer
        timer.setBase(SystemClock.elapsedRealtime())
        val hour = ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60).toInt()
        timer.setFormat("0" + hour.toString() + ":%s")
        timer.start()
        var end_operate = rootView.findViewById<Button>(R.id.end_operate)
        end_operate.setOnClickListener(View.OnClickListener {
            timer.stop()
            context?.unregisterReceiver(broadcastReceiver)

            val intent = Intent()
            intent.action = "com"
            context?.sendBroadcast(intent)
            updateJson()

        })
        return rootView
    }








    var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // TODO Auto-generated method stub
            stateView.setText(intent.extras!!.getString("data"))
        }
    }

    /*   companion object {//用companion object包裹方法，实现java中static的效果

 }*/

    fun update(str: String) {
        stateView.text = str
    }


    fun updateJson() {

        //存储
        val sharedPreferences = activity?.getSharedPreferences("work", Context.MODE_PRIVATE)
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(System.currentTimeMillis())
        //val device = sharedPreferences?.getString("now_device_code", "")
        val device = sharedPreferences?.getString("device_code", "")
        val time = simpleDateFormat.format(date)
        val info = Info(1, device, time)

        val gson = Gson()
        val obj = gson.toJson(info)

        Toast.makeText(context, obj, Toast.LENGTH_LONG).show()
        val name =sharedPreferences?.getString("filename", "")

        val retrofit = Retrofit.Builder().baseUrl(url?.roboturl).build()
        val cloudAPI = retrofit.create(CloudAPI::class.java)
        val requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), obj)
        val data = cloudAPI.postJson(name!!,requestBody)
        data.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                try {
                    // updateFlag = 1;
                    val jsonObject = JSONObject(response.body()!!.string())
                    val result = jsonObject.getString("result")
                    if (result == "success") {
                        Toast.makeText(context, "数据发送成功！！机器人监督结束！！", Toast.LENGTH_LONG).show()
                        val intent = Intent()
                        intent.setClass(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)

                    }

                    else
                        Toast.makeText(context, "数据传送失败", Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }
        })


    }


        override fun updateView(d: LpmsBData, s: ImuStatus) {

    }
}