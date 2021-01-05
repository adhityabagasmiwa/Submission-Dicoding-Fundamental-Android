/*
 * Created by Adhitya Bagas on 29/12/2020
 * Copyright (c) 2020 . All rights reserved.
 */

package com.adhityabagasmiwa.consumerapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.adhityabagasmiwa.consumerapp.R
import com.adhityabagasmiwa.consumerapp.utils.AlarmReceiver

class PreferenceFragment : PreferenceFragmentCompat() {

    private lateinit var switchPreference: SwitchPreference
    private lateinit var language: Preference

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)

        setRepeatingAlarm()
        changeLanguage()
    }

    private fun setRepeatingAlarm() {
        val alarmReceiver = AlarmReceiver()

        switchPreference = findPreference(resources.getString(R.string.key_notifications))!!
        switchPreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            if (switchPreference.isChecked) {
                activity?.let { alarmReceiver.cancelAlarm(it) }
                Toast.makeText(activity, "Cancel Repeating Alarm", Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = false
            } else {
                activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                Toast.makeText(activity, "Repeating Alarm Set Up", Toast.LENGTH_SHORT).show()
                switchPreference.isChecked = true
            }
            false
        }
    }

    private fun changeLanguage() {
        language = findPreference(resources.getString(R.string.key_languages))!!
        language.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            false
        }
    }

}
