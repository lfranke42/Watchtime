package de.htwk.watchtime.network.ranking

import android.content.Context
import android.content.SharedPreferences
import de.htwk.watchtime.R
import java.util.*

class DeviceIdManager (private val context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val DEVICE_ID = "device_id"
    }


    fun getDeviceId(): String {
        val deviceId = prefs.getString(DEVICE_ID, null)

        if (deviceId == null) {
            val editor = prefs.edit()
            val newDeviceId = UUID.randomUUID().toString()
            editor.putString(DEVICE_ID, newDeviceId)
            editor.apply()
            return newDeviceId
        }

        return deviceId
    }
}