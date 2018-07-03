package de.otto.ov.brain.easytouch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL
import android.widget.Toast
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.util.Log
import java.util.concurrent.TimeUnit


class ScaleActivity : AppCompatActivity() {
    private fun getValueFromJson(json: String): String {
        val jsonObject = JSONObject(json);
        val dataObject = (jsonObject.get("data") as JSONArray).get(0);

        return (dataObject as JSONObject).get("Gewicht_kg").toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)

        try {
            val task = HttpAsyncTask(R.id.lightest, "http://tavira-net.servebeer.com/storage/E2C_24.json")
            task.execute()

            val task2 = HttpAsyncTask(R.id.heaviest, "http://tavira-net.servebeer.com/storage/E2C_23.json")
            task2.execute()
        } catch(e: Exception) {
            Toast.makeText(baseContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private inner class HttpAsyncTask(val id: Int, val url: String) : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg url: String): String {
            return URL(this.url).readText()
        }

        // onPostExecute displays the results of the AsyncTask.
        override fun onPostExecute(result: String) {
            findViewById<TextView>(id).apply {
                text = getValueFromJson(result)
            }
        }
    }
}
