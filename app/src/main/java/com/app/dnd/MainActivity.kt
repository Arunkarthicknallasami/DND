package com.app.dnd

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/*
Ref url : https://android--examples.blogspot.com/2017/08/android-turn-on-off-do-not-disturb-mode.html
*/
class MainActivity : AppCompatActivity() {
    var ENABLE_DND = 1001
    var DISABLE_DND = 1002
    lateinit var mNotificationManager: NotificationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNotificationManager =
            this@MainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        btn_enableDNDPermission.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !mNotificationManager.isNotificationPolicyAccessGranted()) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivityForResult(intent, ENABLE_DND)
            } else {
                Toast.makeText(this@MainActivity, "DND access is granted", Toast.LENGTH_LONG).show()
            }
        }

        btn_disableDNDPermission.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !mNotificationManager.isNotificationPolicyAccessGranted()) {
                Toast.makeText(this@MainActivity, "DND access is not granted", Toast.LENGTH_LONG)
                    .show()
            } else {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivityForResult(intent, DISABLE_DND)
            }
        }
        btn_enableDND.setOnClickListener {
            /*
                      int INTERRUPTION_FILTER_ALARMS
                          Interruption filter constant - Alarms only interruption filter - all
                          notifications except those of category CATEGORY_ALARM are suppressed.
                          Some audio streams are muted.
                  */
            changeInterruptionFiler(
                mNotificationManager,
                NotificationManager.INTERRUPTION_FILTER_ALARMS
            );

        }
        btn_disableDND.setOnClickListener {
            /*
                               int INTERRUPTION_FILTER_ALL
                                   Interruption filter constant - Normal interruption
                                   filter - no notifications are suppressed.
                           */
            changeInterruptionFiler(
                mNotificationManager,
                NotificationManager.INTERRUPTION_FILTER_ALL
            );

        }
    }

    protected fun changeInterruptionFiler(
        mNotificationManager: NotificationManager,
        interruptionFilter: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // If api level minimum 23
            /*
                boolean isNotificationPolicyAccessGranted ()
                    Checks the ability to read/modify notification policy for the calling package.
                    Returns true if the calling package can read/modify notification policy.
                    Request policy access by sending the user to the activity that matches the
                    system intent action ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS.

                    Use ACTION_NOTIFICATION_POLICY_ACCESS_GRANTED_CHANGED to listen for
                    user grant or denial of this access.

                Returns
                    boolean

            */
            // If notification policy access granted for this package
            if (mNotificationManager.isNotificationPolicyAccessGranted()) {
                /*
                    void setInterruptionFilter (int interruptionFilter)
                        Sets the current notification interruption filter.

                        The interruption filter defines which notifications are allowed to interrupt
                        the user (e.g. via sound & vibration) and is applied globally.

                        Only available if policy access is granted to this package.

                    Parameters
                        interruptionFilter : int
                        Value is INTERRUPTION_FILTER_NONE, INTERRUPTION_FILTER_PRIORITY,
                        INTERRUPTION_FILTER_ALARMS, INTERRUPTION_FILTER_ALL
                        or INTERRUPTION_FILTER_UNKNOWN.
                */

                // Set the interruption filter
                mNotificationManager.setInterruptionFilter(interruptionFilter)
            } else {
                /*
                    String ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                        Activity Action : Show Do Not Disturb access settings.
                        Users can grant and deny access to Do Not Disturb configuration from here.

                    Input : Nothing.
                    Output : Nothing.
                    Constant Value : "android.settings.NOTIFICATION_POLICY_ACCESS_SETTINGS"
                */
                // If notification policy access not granted for this package
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                startActivity(intent)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ENABLE_DND) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !mNotificationManager.isNotificationPolicyAccessGranted()) {
                Toast.makeText(this@MainActivity, "DND access is Denied", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity, "DND access is granted", Toast.LENGTH_LONG).show()
            }
        } else if (requestCode == DISABLE_DND) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && !mNotificationManager.isNotificationPolicyAccessGranted()) {
                Toast.makeText(this@MainActivity, "DND access is disabled", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}
