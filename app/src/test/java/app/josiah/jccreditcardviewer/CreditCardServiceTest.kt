package app.josiah.jccardviewer

import app.josiah.jccreditcardviewer.models.CreditCardModel
import app.josiah.jccardviewer.service.CreditCardService
import org.junit.Test

class CreditCardServiceTest {

    @Test
    fun testSimpleAPIConnection() {
            val service = CreditCardService()

            // Define onSuccess and onFailure callbacks
            val onSuccess: (List<CreditCardModel>) -> Unit = { creditCards ->
                println("Received credit cards:")
                creditCards.forEach { card ->
                    println("ID: ${card.id}, Number: ${card.creditCardNumber}")
                }
            }

            val onFailure: (String) -> Unit = { error ->
                println("Error occurred: $error")
            }

            // Call fetchCreditCards method
            service.fetchCreditCards(onSuccess, onFailure)
    }
}
