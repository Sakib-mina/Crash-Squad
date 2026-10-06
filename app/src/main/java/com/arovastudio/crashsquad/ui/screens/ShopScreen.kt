package com.arovastudio.crashsquad.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.billingclient.api.ProductDetails
import com.arovastudio.crashsquad.R
import com.arovastudio.crashsquad.billing.ShopCatalog
import com.arovastudio.crashsquad.billing.ShopProduct
import com.arovastudio.crashsquad.components.GameHeader
import com.arovastudio.crashsquad.data.GameProgressEntity
import com.arovastudio.crashsquad.ui.theme.CoinGold
import com.arovastudio.crashsquad.ui.theme.GemPurple

@Composable
fun ShopScreen(
    progress: GameProgressEntity?,
    isGooglePlayConnected: Boolean,
    productDetailsMap: Map<String, ProductDetails> = emptyMap(),
    onBackClick: () -> Unit,
    onProductClick: (Activity, ShopProduct) -> Unit,
    onRedeemPromoCode: (String) -> Unit = {}
) {
    BackHandler { onBackClick() }

    val context = LocalContext.current
    val activity = context as? Activity
    val keyboardController = LocalSoftwareKeyboardController.current

    val coins = progress?.coins ?: 1250
    val gems = progress?.gems ?: 50

    var promoCodeInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E7))
            .testTag("screen_shop")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            GameHeader(
                coins = coins,
                gems = gems,
                onBackClick = onBackClick
            )

            Text(
                text = "GEM SHOP",
                color = Color(0xFF4E342E),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 4.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    PromoCodeCard(
                        code = promoCodeInput,
                        onCodeChange = { promoCodeInput = it },
                        onRedeem = {
                            if (promoCodeInput.isNotBlank()) {
                                onRedeemPromoCode(promoCodeInput)
                                promoCodeInput = ""
                                keyboardController?.hide()
                            }
                        }
                    )
                }

                items(ShopCatalog.PRODUCTS) { product ->
                    val playDetails = productDetailsMap[product.id]
                    val dynamicPrice = playDetails?.oneTimePurchaseOfferDetails?.formattedPrice
                    
                    ProductCard(
                        product = product,
                        overridePriceFormatted = dynamicPrice,
                        onBuyClick = {
                            if (activity != null) {
                                onProductClick(activity, product)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PromoCodeCard(
    code: String,
    onCodeChange: (String) -> Unit,
    onRedeem: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(18.dp))
            .border(1.5.dp, Color(0xFFFFCC80), RoundedCornerShape(18.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CardGiftcard,
                    contentDescription = null,
                    tint = Color(0xFFE65100),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PROMO CODE / DISCOUNT",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp,
                    color = Color(0xFF4E342E)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    placeholder = {
                        Text(
                            text = "Enter promo code",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onRedeem() }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF9800),
                        unfocusedBorderColor = Color(0xFFFFCC80)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = onRedeem,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = "APPLY",
                        fontWeight = FontWeight.Black,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: ShopProduct,
    overridePriceFormatted: String? = null,
    onBuyClick: () -> Unit
) {
    val displayPrice = overridePriceFormatted ?: product.priceFormatted

    Surface(
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(22.dp))
            .border(2.dp, Color(0xFFFFE082), RoundedCornerShape(22.dp))
            .testTag("card_product_${product.id}")
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
                        )
                    )
                    .border(2.dp, Color.White, RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (product.id == "gems_1200" || product.id == "special_pack") {
                    Image(
                        painter = painterResource(id = R.drawable.img_gem_chest),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = GemPurple,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp,
                    color = Color(0xFF263238)
                )

                Text(
                    text = product.description,
                    fontSize = 11.sp,
                    color = Color(0xFF78909C),
                    maxLines = 2
                )

                if (product.coinsAmount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = CoinGold,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+${product.coinsAmount} Coins",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = onBuyClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                border = BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(18.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .shadow(4.dp, RoundedCornerShape(18.dp))
                    .testTag("btn_buy_${product.id}")
            ) {
                Text(
                    text = displayPrice,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    color = Color.White
                )
            }
        }
    }
}
