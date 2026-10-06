package com.arovastudio.crashsquad.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arovastudio.crashsquad.data.GameProgressEntity

@Composable
fun SettingsScreen(
    progress: GameProgressEntity?,
    onBackClick: () -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleVibration: (Boolean) -> Unit,
    onChangeLanguage: (String) -> Unit,
    onRestorePurchases: () -> Unit
) {
    BackHandler { onBackClick() }

    val sound = progress?.soundEnabled ?: true
    val music = progress?.musicEnabled ?: true
    val vibration = progress?.vibrationEnabled ?: true
    val language = progress?.selectedLanguage ?: "English"

    val context = LocalContext.current
    var showInfoDialog by remember { mutableStateOf<Pair<String, String>?>(null) }
    var showLanguagePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF8E7))
            .testTag("screen_settings")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF29B6F6), Color(0xFF0288D1), Color(0xFF01579B))
                            )
                        )
                        .border(2.dp, Color.White, CircleShape)
                        .clickable { onBackClick() }
                        .testTag("btn_settings_back"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Settings",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF263238)
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(26.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(26.dp))
                    .border(2.dp, Color(0xFFFFE082), RoundedCornerShape(26.dp))
            ) {
                Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
                    SettingsToggleRow(
                        icon = Icons.Default.VolumeUp,
                        title = "Sound",
                        isChecked = sound,
                        onCheckedChange = onToggleSound,
                        testTag = "switch_sound"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsToggleRow(
                        icon = Icons.Default.MusicNote,
                        title = "Music",
                        isChecked = music,
                        onCheckedChange = onToggleMusic,
                        testTag = "switch_music"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsToggleRow(
                        icon = Icons.Default.Gamepad,
                        title = "Vibration",
                        isChecked = vibration,
                        onCheckedChange = onToggleVibration,
                        testTag = "switch_vibration"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsNavRow(
                        icon = Icons.Default.Language,
                        title = "Language",
                        value = "$language >",
                        onClick = { showLanguagePicker = true },
                        testTag = "row_language"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsNavRow(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Help & Support",
                        onClick = {
                            showInfoDialog = Pair(
                                "Help & Support",
                                "How to Play Crash Squad:\n\n1. Aim: Drag your hero backward from the slingshot launcher.\n2. Trajectory: Follow the red dotted guide arc.\n3. Release: Launch your hero to demolish wooden beams, stone pillars, and TNT caches!\n4. Win: Eliminate all Grumpies with fewer shots to earn 3 stars and big rewards!\n\nUpgrade your heroes in the Heroes tab to increase power."
                            )
                        },
                        testTag = "row_help"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsNavRow(
                        icon = Icons.Default.Policy,
                        title = "Privacy Policy",
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/crashsquad-privacy-policy/home"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                showInfoDialog = Pair(
                                    "Privacy Policy",
                                    "https://sites.google.com/view/crashsquad-privacy-policy/home"
                                )
                            }
                        },
                        testTag = "row_privacy"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsNavRow(
                        icon = Icons.Default.Description,
                        title = "Terms of Service",
                        onClick = {
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/crashsquad-terms-condition/home"))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                showInfoDialog = Pair(
                                    "Terms of Service",
                                    "https://sites.google.com/view/crashsquad-terms-condition/home"
                                )
                            }
                        },
                        testTag = "row_terms"
                    )

                    HorizontalDivider(color = Color(0xFFF0F0F0), modifier = Modifier.padding(vertical = 6.dp))

                    SettingsNavRow(
                        icon = Icons.Default.Restore,
                        title = "Restore Purchases",
                        onClick = onRestorePurchases,
                        testTag = "row_restore"
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Crash Squad • Island Strike v1.0",
                color = Color(0xFF90A4AE),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        showInfoDialog?.let { (title, body) ->
            AlertDialog(
                onDismissRequest = { showInfoDialog = null },
                title = { Text(text = title, fontWeight = FontWeight.Bold, color = Color(0xFF263238)) },
                text = { Text(text = body, fontSize = 14.sp, color = Color(0xFF455A64)) },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = null }) {
                        Text("OK", fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                    }
                }
            )
        }

        if (showLanguagePicker) {
            val languages = listOf("English", "বাংলা", "Español", "Português", "Français", "Deutsch", "日本語")
            AlertDialog(
                onDismissRequest = { showLanguagePicker = false },
                title = { Text("Select Language", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        languages.forEach { lang ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onChangeLanguage(lang)
                                        showLanguagePicker = false
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = lang,
                                    fontSize = 16.sp,
                                    fontWeight = if (lang == language) FontWeight.Bold else FontWeight.Normal,
                                    color = if (lang == language) Color(0xFF2E7D32) else Color(0xFF263238)
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showLanguagePicker = false }) {
                        Text("Close", color = Color(0xFF0288D1))
                    }
                }
            )
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF263238),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4CAF50),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCFD8DC)
            ),
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
fun SettingsNavRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF263238),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF263238)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value != null) {
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color(0xFF78909C),
                    fontWeight = FontWeight.Medium
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFF90A4AE),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
