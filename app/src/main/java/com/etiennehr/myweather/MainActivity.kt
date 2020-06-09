package com.etiennehr.myweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var requestQueue:RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)

        downloadButton.setOnClickListener {
            val key = "dd4839d9f18f1c2fb9284b7926c613b0"
            var city = urlField.text
            var url = city
            launchDownloadRequest(url.toString())
            launchJsonObjectRequest("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$key")
        }
    }

    fun launchDownloadRequest(url:String){
        val request = StringRequest(Request.Method.GET, url, Response.Listener {body:String ->
            outputView.text = body
        }, Response.ErrorListener {
            outputView.text = ""
        })

        requestQueue.add(request)
    }
    fun launchJsonObjectRequest(url:String){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {taskObject ->
            val main = taskObject.getJSONObject("main")
            outputView.text = main.getString("temp") + "°C"
        }, Response.ErrorListener {
            outputView.text = "unknown city"
        })
        requestQueue.add(jsonObjectRequest)
    }
}
