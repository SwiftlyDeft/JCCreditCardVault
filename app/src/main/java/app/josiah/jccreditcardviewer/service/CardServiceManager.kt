package app.josiah.jccardviewer.service

import app.josiah.jccreditcardviewer.models.CreditCardModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class CreditCardService {

    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun fetchCreditCards(): Pair<List<CreditCardModel>?, String?> {
        val request = Request.Builder()
            .url("https://random-data-api.com/api/v2/credit_cards?size=100")
            .build()

        return try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            if (!response.isSuccessful) {
                null to "Failed to fetch data: ${response.code}"
            } else {
                val json = response.body?.string()
                val type = object : TypeToken<List<CreditCardModel>>() {}.type
                val creditCards = gson.fromJson<List<CreditCardModel>>(json, type)
                creditCards to null
            }
        } catch (e: IOException) {
            null to e.message
        }
    }
}
