package app.josiah.jccreditcardviewer

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import app.josiah.jccreditcardviewer.models.CreditCardModel
import app.josiah.jccardviewer.service.CreditCardService
import app.josiah.jccreditcardviewer.ui.CreditCardView
import app.josiah.jccreditcardviewer.ui.theme.JCCreditCardViewerTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                    val isPermissionGranted = RequestInternetPermission()
                    // Call RefreshCreditCardsFromAPI to fetch credit card data
                    if (isPermissionGranted) {
                        RefreshCreditCardsFromAPI()
                    } else {
                        // TODO: A State that shows no internet is available
                    }
                }
            }
        }
    }
}

@Composable
fun RefreshCreditCardsFromAPI() {
    println("Refresh Credit Cards From API")
    val creditCardsState = remember { mutableStateOf<List<CreditCardModel>>(emptyList()) }
    val loadingState = remember { mutableStateOf(true) }

    val service = CreditCardService()

    println("Coroutine start...")
    service.fetchCreditCards(
        onSuccess = { creditCards ->
            println("On success")
            creditCardsState.value = creditCards
            loadingState.value = false
        },
        onFailure = { error ->
            println("Error occurred: $error")
            // Update loading state to false in case of failure
            loadingState.value = false
        }
    )

    if (loadingState.value) {
        // Render the loading indicator if data is still being fetched
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        // Render the credit card list if loading is complete
        CreditCardList(creditCardsState.value)
    }
}

@Composable
fun LaunchedEffect(unit: Unit, content: Any) {

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
    var image = R.drawable.card_logo_unknown
    if (cardType == "mastercard") {
        image = R.drawable.mastercardsimple
    } else if (cardType == "solo") {
        image = R.drawable.solo
    }
    CreditCardView(
        cardNumber = cardNumber,
        cardExpDate = cardExpDate,
        cardType = cardType,
        cardImageResource = image
    )
}

@Composable
fun CreditCardList(cards: List<CreditCardModel>) {
    LazyColumn(modifier = Modifier.fillMaxHeight().background(color = androidx.compose.ui.graphics.Color.Transparent)) {
        items(cards) { card ->
            CreditCardItem(
                cardNumber = card.getDisplayNumber(),
                cardExpDate = card.creditCardExpiryDate,
                cardType = card.creditCardType
            )
        }
    }
}

@Preview
@Composable
fun PreviewCreditCardList() {
    val cards = listOf(
        CreditCardModel(0, "", "1234-5678-9012-3456", "04/27", "mastercard"),
        CreditCardModel(2, "", "5678-1234-9012-3456", "10/26", "visa"),
        // Add more cards as needed
    )
    CreditCardList(cards = cards)
}

/*
Ask for internet permission
 */
@Composable
fun RequestInternetPermission(): Boolean {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Check if permission is already granted
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.INTERNET
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        isPermissionGranted = true
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, do your task that requires internet access here
            isPermissionGranted = true
            println("Is granted")
        } else {
            println("Is not granted")
        }
    }

    if (!isPermissionGranted) {
        Column {
            Button(onClick = {
                requestPermissionLauncher.launch(android.Manifest.permission.INTERNET)
            }) {
                Text("Allow Internet Permission")
            }
        }
    }
    return isPermissionGranted
}