package com.etiennehr.myweather

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var requestQueue:RequestQueue
    var lastCity = "Bruxelles"

    val SETTING_FILE_NAME = "com.etiennehr.myweather"
    val FULL_TEXT_SETTING_KEY = "city"
    val SETTING_API_KEY = "dd4839d9f18f1c2fb9284b7926c613b0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestQueue = Volley.newRequestQueue(this)

        val sharedPreferencesManager = getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE)
        lastCity = sharedPreferencesManager.getString(FULL_TEXT_SETTING_KEY, lastCity) ?: ""


        launchJsonObjectRequest("https://api.openweathermap.org/data/2.5/weather?q=$lastCity&units=metric&appid=$SETTING_API_KEY")

        downloadButton.setOnClickListener {
            var city = urlField.text
            launchJsonObjectRequest("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$SETTING_API_KEY")
        }
    }

    fun launchJsonObjectRequest(url:String){
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {taskObject ->
            val main = taskObject.getJSONObject("main")
            val temp = main.getString("temp") + "°"
            val tempMax = main.getString("temp_max") + "°"
            val tempMin = main.getString("temp_min") + "°"
            val cityData = urlField.text
            if (cityData.isEmpty())
            {
                cityView.text = lastCity
                maxtempView.text = tempMax
                mintempView.text = tempMin
            }
            else
            {
                cityView.text = "$cityData"
                lastCity = cityData.toString()
                stockedData()
                urlField.text = null
            }
            outputView.text = "$temp"
        }, Response.ErrorListener {
            outputView.text = "unknown"
        })
        requestQueue.add(jsonObjectRequest)
    }
    fun stockedData(){
        val sharedPreferencesManager = getSharedPreferences(SETTING_FILE_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferencesManager.putString(FULL_TEXT_SETTING_KEY, lastCity)
        sharedPreferencesManager.apply()
    }
}



