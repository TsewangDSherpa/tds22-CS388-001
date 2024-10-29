// SettingsFragment.kt
package com.codepath.articlesearch

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var cachePreference: SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        cachePreference = findPreference<SwitchPreferenceCompat>("cache_articles")!!
        cachePreference.isChecked = getCachePreference()

        cachePreference.setOnPreferenceChangeListener { _, newValue ->
            setCachePreference(newValue as Boolean)
            true
        }
    }

    private fun getCachePreference(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("cache_articles", true)
    }

    private fun setCachePreference(isEnabled: Boolean) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("cache_articles", isEnabled).apply()
    }
}
