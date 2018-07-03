package de.otto.ov.brain.easytouch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import android.widget.Toast
import android.os.AsyncTask
import android.util.Log


class ScaleActivity : AppCompatActivity() {
    private fun getValueFromJson(url: String): String {
        val jsonObject = JSONObject(URL(url).readText());
        val dataObject = (jsonObject.get("data") as JSONArray).get(0);

        return (dataObject as JSONObject).get("Gewicht_kg").toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        val task = object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String {
                val textView = findViewById<TextView>(R.id.lightest).apply {
                    text = getValueFromJson("http://tavira-net.servebeer.com/storage/E2C_24.json") + " kg"
                }
                val textView2 = findViewById<TextView>(R.id.heaviest).apply {
                    text = getValueFromJson("http://tavira-net.servebeer.com/storage/E2C_23.json") + " kg"
                }
                return "test"
            }

            override fun onPostExecute(s: String) {
                if (!isFinishing && !isCancelled) {
                    Log.d("Request", s)
                    Toast.makeText(this@ScaleActivity, "Request performed", Toast.LENGTH_LONG).show()
                }
            }
        }
//        val textView = findViewById<TextView>(R.id.lightest).apply {
//            text = getValueFromJson("http://tavira-net.servebeer.com/storage/E2C_24.json") + " kg"
//        }
//        val textView2 = findViewById<TextView>(R.id.heaviest).apply {
//            text = getValueFromJson("http://tavira-net.servebeer.com/storage/E2C_23.json") + " kg"
//        }
    }
}
