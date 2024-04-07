package app.josiah.jccreditcardviewer

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import app.josiah.jccreditcardviewer.models.CreditCardModel
import app.josiah.jccardviewer.service.CreditCardService
import app.josiah.jccreditcardviewer.ui.CreditCardList
import app.josiah.jccreditcardviewer.ui.CreditCardView
import app.josiah.jccreditcardviewer.ui.theme.JCCreditCardViewerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JCCreditCardViewerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val isPermissionGranted = requestInternetPermission()
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
    val creditCardsState = remember { mutableStateOf<List<CreditCardModel>>(emptyList()) }
    val loadingState = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val service = CreditCardService()
            try {
                val (creditCards, error) = CreditCardService().fetchCreditCards()
                if (creditCards != null) {
                    withContext(Dispatchers.Main) {
                        creditCardsState.value = creditCards
                        loadingState.value = false
                    }
                } else {
                    // Handle error case
                    println("Error: $error")
                    loadingState.value = false
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loadingState.value = false
                }
            }
        }
    }

    if (loadingState.value) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        CreditCardList(creditCardsState.value)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JCCreditCardViewerTheme {
        PreviewCreditCardList()
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
fun requestInternetPermission(): Boolean {
    val context = LocalContext.current
    var isPermissionGranted by remember { mutableStateOf(false) }

    // Check if permission is already granted
    if (ContextCompat.checkSelfPermission(
            context, android.Manifest.permission.INTERNET
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
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                requestPermissionLauncher.launch(android.Manifest.permission.INTERNET)
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Allow Internet Permission")
            }
        }
    }
    return isPermissionGranted
}