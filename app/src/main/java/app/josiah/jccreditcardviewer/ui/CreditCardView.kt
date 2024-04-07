package app.josiah.jccreditcardviewer.ui

import app.josiah.jccreditcardviewer.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CreditCardView(
    cardNumber: String,
    cardExpDate: String,
    cardType: String,
    modifier: Modifier = Modifier,
    cardImageResource: Int,
    shape: Shape = RoundedCornerShape(16.dp)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(90.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.CenterStart
    ) {
        // Card background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White, shape = shape)
        )
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Card image
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(cardImageResource),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Column(modifier = Modifier.padding(start = 16.dp)) {
                // Card number
                Text(
                    text = cardNumber,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                )

                // Card holder name
                Text(
                    text = cardExpDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
                Text(
                    text = cardType,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreditCardView() {
    CreditCardView(
        cardNumber = "1234 5678 9012 3456",
        cardExpDate = "10/24",
        cardType = "Mastercard",
        cardImageResource = R.drawable.card_logo_unknown
    )
}
