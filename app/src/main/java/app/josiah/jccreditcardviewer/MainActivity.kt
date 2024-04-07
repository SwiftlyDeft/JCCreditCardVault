package app.josiah.jccreditcardviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.josiah.jccardviewer.models.CreditCardModel
import app.josiah.jccardviewer.service.CreditCardService
import app.josiah.jccreditcardviewer.ui.CreditCardView
import app.josiah.jccreditcardviewer.ui.theme.JCCreditCardViewerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JCCreditCardViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Call RefreshCreditCardsFromAPI to fetch credit card data
                    RefreshCreditCardsFromAPI()
                }
            }
        }
    }
}

@Composable
fun RefreshCreditCardsFromAPI() {
    val creditCardsState = remember { mutableStateOf<List<CreditCardModel>>(emptyList()) }

    val loadingState = remember { mutableStateOf(true) }

    val service = CreditCardService()
    val onSuccess: (List<CreditCardModel>) -> Unit = { creditCards ->
        creditCardsState.value = creditCards
        loadingState.value = false
    }

    val onFailure: (String) -> Unit = { error ->
        println("Error occurred: $error")
        // Update loading state to false in case of failure
        loadingState.value = false
    }

    // Call the service to fetch credit card data
    if (loadingState.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Render the loading indicator
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        // Render the credit card list if loading is complete
        CreditCardList(creditCardsState.value)
    }
    service.fetchCreditCards(onSuccess, onFailure)
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JCCreditCardViewerTheme {
        PreviewCreditCardList()
    }
}

@Composable
fun CreditCardItem(
    cardNumber: String,
    cardExpDate: String,
    cardType: String
) {
    // Determine the image based on the card type
    CreditCardView(
        cardNumber = cardNumber,
        cardExpDate = cardExpDate,
        cardType = cardType,
        cardImageResource = R.drawable.mastercardsimple
    )
}

@Composable
fun CreditCardList(cards: List<CreditCardModel>) {
    LazyColumn {
        items(cards) { card ->
            CreditCardItem(
                cardNumber = card.creditCardNumber,
                cardExpDate = card.creditCardExpiryDate,
                cardType = card.creditCardType)
        }
    }
}

@Preview
@Composable
fun PreviewCreditCardList() {
    val cards = listOf(
        CreditCardModel(0,"", "1234-5678-9012-3456", "04/27", "mastercard"),
        CreditCardModel(2,"", "5678-1234-9012-3456", "10/26", "visa"),
        // Add more cards as needed
    )
    CreditCardList(cards = cards)
}
