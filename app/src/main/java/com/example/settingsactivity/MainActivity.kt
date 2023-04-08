package com.example.settingsactivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.settingsactivity.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    companion object {
        const val VOLUME_LVL = "volume_lvl"
        const val KEY_BLUETOOTH = "key_bluetooth"
        const val KEY_VIBRATION = "key_vibration"
        const val KEY_DARK_MODE = "key_dark_mode"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSettings()
        initUI()
    }

    private fun initUI() {
        binding.sliderVolume.addOnChangeListener { _, value, _ ->

            CoroutineScope(Dispatchers.IO).launch {
                saveVolume(value.toInt())
            }
        }
        binding.switchBluetooth.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_BLUETOOTH, isChecked)

            }
        }
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_DARK_MODE, isChecked)

            }
        }
        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            CoroutineScope(Dispatchers.IO).launch {
                saveOptions(KEY_VIBRATION, isChecked)

            }
        }
    }

    private suspend fun saveVolume(value: Int) {
        dataStore.edit { preference ->
            preference[intPreferencesKey(VOLUME_LVL)] = value
        }
    }

    private suspend fun saveOptions(key: String, value: Boolean) {
        dataStore.edit { preference ->
            preference[booleanPreferencesKey(key)] = value
        }
    }

    private fun getSettings(): Flow<SettingsModel> {
        return dataStore.data.map { preferences ->
            SettingsModel(
                volume = preferences[intPreferencesKey(VOLUME_LVL)] ?: 50,
                bluetooth = preferences[booleanPreferencesKey(KEY_BLUETOOTH)] ?: true,
                vibration = preferences[booleanPreferencesKey(KEY_VIBRATION)] ?: false,
                darkMode = preferences[booleanPreferencesKey(KEY_DARK_MODE)] ?: true
            )

        }
    }
}




