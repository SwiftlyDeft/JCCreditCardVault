package app.josiah.jccreditcardviewer.models

import com.google.gson.annotations.SerializedName

data class CreditCardModel(
    val id: Int,
    val uid: String,

    @SerializedName("credit_card_number")
    val creditCardNumber: String,

    @SerializedName("credit_card_expiry_date")
    val creditCardExpiryDate: String,

    @SerializedName("credit_card_type")
    val creditCardType: String
) {
    /*
        This is for aesthetics, majority of credit card representations show credit card numbers in logical blocks, the data separates these blocks using "-"
     */
    fun getDisplayNumber(): String {
        // Replace "-" in credit card number with space
        return creditCardNumber.replace("-", " ")
    }
}
