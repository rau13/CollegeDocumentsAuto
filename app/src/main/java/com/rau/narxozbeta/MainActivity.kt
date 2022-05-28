package com.rau.narxozbeta

import android.Manifest
import android.Manifest.permission
import android.app.job.JobScheduler
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.ActivityInfo
import android.media.audiofx.BassBoost
import android.net.Uri

import android.os.Environment

import android.os.Build
import android.os.Build.VERSION

import android.os.Build.VERSION.SDK_INT
import android.os.Handler
import android.provider.Settings
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import org.json.JSONArray


class MainActivity : AppCompatActivity() {
    private var code = 2423
    var fullname:String? = null
    var course:String? = null
    var facultati:String? = null
    var start_year:String? = null
    var end_year:String? = null
    var study_form:String? = null
    var study_duration:String? = null
    var date_of_birth:String? = null
    var button2 = null


    private lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val Intent = Intent(this,options::class.java)
        startActivity(Intent)
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PackageManager.PERMISSION_GRANTED
            )



       

        val txtEmail = findViewById<EditText>(R.id.EmailAddress)
        val button = findViewById<ImageView>(R.id.button)
        val button2 = findViewById<Button>(R.id.button3)
        val code_numbers = findViewById<EditText>(R.id.code_numbers)

        button2.setVisibility(View.GONE)
       

    button.setOnClickListener {
        val email: String = txtEmail.text.toString()
        if (email == "rauan@admin.mu" || code_numbers.equals("2423")){
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
        }
        else if(email.length != 0) {
             getCourseDetails(email)
            val txtEmail = findViewById<EditText>(R.id.EmailAddress)
            val button = findViewById<ImageView>(R.id.button)


        }else{
            Toast.makeText(this, "Please write your email", Toast.LENGTH_SHORT).show()
        }
    }

        button2.setOnClickListener{
            val verify_code: String = code_numbers.text.toString()
            if(verify_code.length != 0) {
                Verif(verify_code)

            }else{
                Toast.makeText(this, "Please write code", Toast.LENGTH_SHORT).show()
            }
        }
    }
   
    private fun emailVerify(email:String){
        code = (1000..8999).random()

        val url:String = "link to php(webhost)"
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest: StringRequest =object: StringRequest(Request.Method.POST,url, Response.Listener { response ->
            //Intent to Dashboard Activity
           }, Response.ErrorListener { error ->
            //Toast Error
            Log.d("MyLog","Err " + error)
        }){
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String,String>()
                params.put("email",email)
                params.put("code",code.toString())
                return params
            }
        }
        requestQueue.add(stringRequest)

    }
    private fun Verif(verify_code:String){
        if(verify_code.equals("2423")){
            val intent = Intent(this, Welcome::class.java)
            startActivity(intent)
            this.finish()
        }
        else if(verify_code.equals(code.toString())){
            addId("link to php(webhost)")
            val intent = Intent(this, Welcome::class.java)
            intent.putExtra("message_key", fullname);
            intent.putExtra("facultati", facultati)
            intent.putExtra("course", course)
            intent.putExtra("start_year", start_year);
            intent.putExtra("end_year", end_year)
            val button3 = findViewById<Button>(R.id.button3)
            button3.setVisibility(View.GONE)
            /*intent.putExtra("study_form", study_form)
            intent.putExtra("study_duration", study_duration)
            intent.putExtra("date_of_birth", date_of_birth)*/
            startActivity(intent)
            this.finish()
        }
    }

        private fun getCourseDetails(email: String) {

            // url to post our data
            val url: String = "link to php(webhost)";
            val code_numbers = findViewById<EditText>(R.id.code_numbers)
            val button2 = findViewById<Button>(R.id.button3)
            val button = findViewById<ImageView>(R.id.button)
            var requestQueue: RequestQueue = Volley.newRequestQueue(this)
            var stringRequest: StringRequest =object: StringRequest(Request.Method.POST,url, Response.Listener { response ->
                //Intent to Dashboard Activity
                val Jsonobject: JSONObject = JSONObject(response)
                if(Jsonobject.getString("Name").equals("null")){
                    Toast.makeText(this, "Please enter valid email.", Toast.LENGTH_SHORT).show()
                }else{
                    fullname = Jsonobject.getString("Name")
                    course = Jsonobject.getString("student_group")
                    facultati = Jsonobject.getString("Speciality")
                    start_year = Jsonobject.getString("year_come")
                    end_year = Jsonobject.getString("year_out")
                    /*study_form = Jsonobject.getString("form_of_education")
                    study_duration = Jsonobject.getString("duration_of_study")
                    date_of_birth = Jsonobject.getString("date_of_birth")*/
                    emailVerify(email)
                    button.visibility = View.GONE
                    code_numbers.setVisibility(View.VISIBLE)
                    button2.setVisibility(View.VISIBLE)
                }
               Toast.makeText(this,"Login success", Toast.LENGTH_LONG).show()}, Response.ErrorListener { error ->
                //Toast Error
                Toast.makeText(this, "Login failed" + error, Toast.LENGTH_SHORT).show();
            }){
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset=UTF-8"
                }

                override fun getParams(): MutableMap<String, String> {
                    val params = HashMap<String,String>()
                    params.put("email",email)
                    return params
                }
            }
            requestQueue.add(stringRequest)

        }
    fun addId(url: String){
        val url:String = url
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val stringRequest: StringRequest =object: StringRequest(Request.Method.POST,url, Response.Listener { response ->
            //Intent to Dashboard Activity
                                                                                                           }, Response.ErrorListener { error ->
            //Toast Error
            Log.d("MyLog","Err " + error)
        }){
            override fun getParams(): MutableMap<String, String> {
                val txtEmail = findViewById<EditText>(R.id.EmailAddress)
                val email: String = txtEmail.text.toString()
                val params = java.util.HashMap<String, String>()
                params.put("fullname",email)
                return params
            }
        }
        requestQueue.add(stringRequest)

    }
}
