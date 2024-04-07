package app.josiah.jccardviewer.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreditCardModel(
    val id: Int,
    val uid: String,
    val creditCardNumber: String,
    val creditCardExpiryDate: String,
    val creditCardType: String
) {
    /*
        This is for astecits, majority of credit card representations show credit card numbers in logical blocks, the data separates these blocks using -
     */
    fun getDisplayNumber(): String {
        // Replace "-" in credit card number with space
        return creditCardNumber.replace("-", " ")
    }
}