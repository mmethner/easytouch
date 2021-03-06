package de.otto.ov.brain.easytouch

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.FontAwesomeModule
import java.text.DateFormat
import java.util.*
import android.content.pm.PackageManager
import com.joanzapata.iconify.widget.IconButton

class MainActivity : AppCompatActivity() {
    private var notificationId = 0;
    private val NOTIFICATION_CHANNEL_ID = "notification_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Iconify.with(FontAwesomeModule())

        disablePicture()

        setContentView(R.layout.activity_main)
    }

    /**
     * disables the camera button if no camera is available
     */
    private fun disablePicture() {
        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            val button: IconButton = findViewById(R.id.buttonPicture)
            button.visibility = View.GONE
        }
    }

    private fun isConnected(): Boolean {
        val connMgr = getSystemService(Activity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun buttonClickScale(view: View) {
        if (isConnected()) {
            startActivity(Intent(this, ScaleActivity::class.java))
        } else {
            Toast.makeText(applicationContext, R.string.error_no_network, Toast.LENGTH_SHORT).show()
        }
    }

    fun buttonClickBasket(view: View) {
        if (isConnected()) {
            startActivity(Intent(this, BasketActivity::class.java))
        } else {
            Toast.makeText(applicationContext, R.string.error_no_network, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * triggers a notification which opens a notification activity
     */
    fun buttonClickNotification(view: View) {

        notificationId++

        val intent = Intent(this, NotificationReceiverActivity::class.java)
        val pIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt(), intent, 0)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Easytouch Notifications", NotificationManager.IMPORTANCE_DEFAULT)

            // Configure the notification channel.
            notificationChannel.description = "Channel description"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_brain)
                .setContentTitle("Awesome notification " + DateFormat.getDateInstance().format(Date()))
                .setContentText("Awesome notification #$notificationId triggered by you")
                .setContentIntent(pIntent)
                .build()

        notificationManager.notify(notificationId, builder)
    }

    /**
     * triggers the camera activity
     */
    fun buttonClickPicture(view: View) {
        startActivity(Intent(this, TakePictureActivity::class.java))
    }
}
