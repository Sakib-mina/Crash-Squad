package com.arovastudio.crashsquad.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arovastudio.crashsquad.R
import com.arovastudio.crashsquad.billing.ShopProduct
import com.arovastudio.crashsquad.ui.theme.GemPurple

@Composable
fun PurchaseSuccessDialog(
    product: ShopProduct,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .shadow(28.dp, RoundedCornerShape(32.dp))
                .border(3.5.dp, Color(0xFFCE93D8), RoundedCornerShape(32.dp))
                .testTag("dialog_purchase_success")
        ) {
            Column(
                modifier = Modifier.padding(26.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color(0xFFFFCA28)))
                    Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFE91E63)))
                    Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(0xFF00E676)))
                    Box(modifier = Modifier.size(14.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFF29B6F6)))
                }

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .shadow(16.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFFF3E5F5), Color(0xFFAB47BC), Color(0xFF6A1B9A))
                            )
                        )
                        .border(3.5.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (product.id == "gems_1200" || product.id == "special_pack") {
                        Image(
                            painter = painterResource(id = R.drawable.img_gem_chest),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Diamond,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Purchase Successful!",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF263238),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF7B1FA2)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "have been added to your account.",
                    fontSize = 14.sp,
                    color = Color(0xFF78909C),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth(0.88f)
                        .height(60.dp)
                        .shadow(8.dp, RoundedCornerShape(30.dp))
                        .testTag("btn_purchase_ok"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                    border = androidx.compose.foundation.BorderStroke(2.5.dp, Color.White),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "OK",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }
        }
    }
}
