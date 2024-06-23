package lnbti.gtp01.droidai.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import lnbti.gtp01.droidai.constants.PreferenceKeys
import lnbti.gtp01.droidai.constants.StringConstants
import lnbti.gtp01.droidai.constants.StringConstants.TAG
import lnbti.gtp01.droidai.models.CalendarEventObject
import lnbti.gtp01.droidai.models.CategoryModel
import lnbti.gtp01.droidai.models.Dealer
import lnbti.gtp01.droidai.models.ErrorBody
import lnbti.gtp01.droidai.models.User
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.reflect.Type

class Utils() {

    companion object {
        //Get Logged In User from Shared Preferences
        fun getLoggedInUser(): User? {
            SharedPreferencesManager.getPreference(PreferenceKeys.LOGGED_IN_USER)?.let {
                return Gson().fromJson(
                    it, User::class.java
                )
            }
            return null
        }

        fun getCategoryList(context: Context): List<CategoryModel> {
            Gson()

            SharedPreferencesManager.getSelectedLanguage().let {
                when (it) {
                    StringConstants.SINHALA -> {
                        return loadJSONFromAsset(context, "si_contents.json")
                    }

                    StringConstants.JAPANESE -> {
                        return loadJSONFromAsset(context, "ja_contents.json")
                    }

                    else -> {
                        return loadJSONFromAsset(context, "en_contents.json")
                    }
                }
            }

        }

        fun getDealerList(context: Context): List<Dealer> {
            Gson()

            SharedPreferencesManager.getSelectedLanguage().let {
                when (it) {
                    StringConstants.SINHALA -> {
                        return loadDealerFromAsset(context, "si_dealers.json")
                    }

                    StringConstants.JAPANESE -> {
                        return loadDealerFromAsset(context, "ja_dealers.json")
                    }

                    else -> {
                        return loadDealerFromAsset(context, "en_dealers.json")
                    }
                }
            }

        }

        private fun loadDealerFromAsset(context: Context, fileName: String): List<Dealer> {
            val json: String?
            try {
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return emptyList() // Return empty list if there's an error
            }

            val gson = Gson()
            val type = object : TypeToken<List<Dealer>>() {}.type
            return gson.fromJson(json, type)
        }

        // Function to read JSON file from assets and return object array
        private fun loadJSONFromAsset(context: Context, fileName: String): List<CategoryModel> {
            val json: String?
            try {
                val inputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                json = String(buffer, Charsets.UTF_8)
            } catch (ex: IOException) {
                ex.printStackTrace()
                return emptyList() // Return empty list if there's an error
            }

            val gson = Gson()
            val type = object : TypeToken<List<CategoryModel>>() {}.type
            return gson.fromJson(json, type)
        }

        fun getEvents(): MutableList<CalendarEventObject> {
            var events = mutableListOf<CalendarEventObject>()
            val serializedObject: String? =
                SharedPreferencesManager.getPreference(PreferenceKeys.EVENTS)
            if (serializedObject != null)
                if (serializedObject != "") {
                    val gson = Gson()
                    val type: Type = object : TypeToken<List<CalendarEventObject?>?>() {}.type
                    events = gson.fromJson(serializedObject, type)
                }
            return events
        }

        /**
         * Deserialize error response.body
         * @param errorBody Error Response
         */
        fun getErrorBodyFromResponse(errorBody: ResponseBody?): ErrorBody {
            val gson = Gson()
            val type = object : TypeToken<ErrorBody>() {}.type
            return gson.fromJson(errorBody?.charStream(), type)
        }
    }

}