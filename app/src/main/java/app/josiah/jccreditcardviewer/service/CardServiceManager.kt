package app.josiah.jccardviewer.service
/*
    A service to manage the fetching of Credit Card data from Random Data API
 */
import app.josiah.jccardviewer.models.CreditCardModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.*
import java.io.IOException

class CreditCardService {

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()

    fun fetchCreditCards(
        onSuccess: (List<CreditCardModel>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val request = Request.Builder()
            .url("https://random-data-api.com/api/v2/credit_cards?size=100")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e.message ?: "Unknown error occurred")
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.let { body ->
                    try {
                        val creditCards = moshi.adapter<List<CreditCardModel>>(
                            Types.newParameterizedType(List::class.java, CreditCardModel::class.java)
                        ).fromJson(body.source())

                        creditCards?.let {
                            onSuccess(it)
                        } ?: run {
                            onFailure("Failed to parse JSON data")
                        }
                    } catch (e: Exception) {
                        onFailure(e.message ?: "Unknown error occurred")
                    } finally {
                        body.close()
                    }
                } ?: run {
                    onFailure("Response body is null")
                }
            }
        })
    }
}