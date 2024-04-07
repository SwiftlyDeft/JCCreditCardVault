package app.josiah.jccardviewer

import app.josiah.jccardviewer.models.CreditCardModel
import app.josiah.jccardviewer.service.CreditCardService
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlinx.coroutines.delay

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
