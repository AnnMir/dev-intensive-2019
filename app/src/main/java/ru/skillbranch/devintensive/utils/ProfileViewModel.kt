package ru.skillbranch.devintensive.utils

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    private val repository: PreferencesRepository =
        PreferencesRepository
    private val profileData = MutableLiveData<Profile>()
    private val appTheme = MutableLiveData<Int>()

    init {
        profileData.value = repository.getProfile()
        appTheme.value = repository.getAppTheme()
        Log.i("DEV-INTENSIVE123", profileData.value!!.firstName)
        Log.i("DEV-INTENSIVE123", appTheme.value.toString())
    }

    fun getProfileData(): LiveData<Profile> = profileData

    fun getAppTheme(): LiveData<Int> = appTheme

    fun saveProfileData(profile: Profile) {
        Log.i("DEV-INTENSIVE123", "saveProfileData")
        repository.saveProfile(profile)
        profileData.value = profile
    }

    fun saveAppTheme() {
        Log.i("DEV-INTENSIVE123", "saveAppTheme")
        repository.saveAppTheme(appTheme.value!!)
    }

    fun switchTheme() {
        if (appTheme.value == AppCompatDelegate.MODE_NIGHT_YES) {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_NO
        } else {
            appTheme.value = AppCompatDelegate.MODE_NIGHT_YES
        }
        repository.saveAppTheme(appTheme.value!!)
    }

    fun isValid(str: String): Boolean {
        if (str.isEmpty())
            return true
        for (prefix in githubPrefixes) {
            if (str.startsWith(prefix)) {
                val account = str.removePrefix(prefix)
                if (account.isNotBlank()) {
                    val path = account.split("/")
                    if (path.size == 1 || (path.size == 2 && path[1].isBlank())) {
                        for (e in exceptions) {
                            if (e.toRegex().matches(account)) {
                                return false
                            }
                            if(account.contains("_") || account.contains(" ")){
                                return false
                            }
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    private val githubPrefixes: List<String> = listOf(
        "github.com/",
        "https://github.com/",
        "www.github.com/",
        "https://www.github.com/"
    )

    private val exceptions: List<String> = listOf(
        "enterprise",
        "features",
        "topics",
        "collections",
        "trending",
        "events",
        "marketplace",
        "pricing",
        "nonprofit",
        "customer-stories",
        "security",
        "login",
        "join"
    )
}