package app.josiah.jccardviewer.service

import app.josiah.jccreditcardviewer.models.CreditCardModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class CreditCardService {

    private val client = OkHttpClient()
    private val gson = Gson()

    fun fetchCreditCards(
        onSuccess: (List<CreditCardModel>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val request = Request.Builder()
            .url("https://random-data-api.com/api/v2/credit_cards?size=100")
            .build()

        println("Fetch Credit cards...")
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message ?: "Unknown error occurred")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    try {
                        val json = body.string()
                        println("Success... $json")
                        val type = object : TypeToken<List<CreditCardModel>>() {}.type
                        val creditCards = gson.fromJson<List<CreditCardModel>>(json, type)

                        creditCards?.let {
                            println("Got it")
                            onSuccess(it)
                        } ?: run {
                            println("Some failure with the json parsing")
                            onFailure("Failed to parse JSON data")
                        }
                    } catch (e: Exception) {
                        println("Exception: $e")
                        onFailure(e.message ?: "Unknown error occurred")
                    } finally {
                        println("Closing")
                        body.close()
                    }
                } ?: run {
                    println("No response body")
                    onFailure("Response body is null")
                }
            }
        })
    }
}
