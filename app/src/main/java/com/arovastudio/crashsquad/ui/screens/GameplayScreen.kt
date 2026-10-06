package com.arovastudio.crashsquad.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arovastudio.crashsquad.R
import com.arovastudio.crashsquad.components.OriginalGameVisuals
import com.arovastudio.crashsquad.game.GameEngine
import com.arovastudio.crashsquad.game.GameState
import kotlinx.coroutines.isActive

@Composable
fun GameplayScreen(
    engine: GameEngine,
    soundEnabled: Boolean,
    onLevelWon: (GameEngine) -> Unit,
    onLevelLost: (GameEngine) -> Unit,
    onHomeClick: () -> Unit,
    onRestartClick: () -> Unit,
    onToggleSound: (Boolean) -> Unit
) {
    var isPaused by remember { mutableStateOf(false) }
    var frameTick by remember { mutableIntStateOf(0) }

    BackHandler {
        isPaused = true
    }

    LaunchedEffect(engine, isPaused) {
        var lastTime = System.nanoTime()
        while (isActive && !isPaused) {
            withFrameNanos { now ->
                val dt = ((now - lastTime) / 1_000_000_000f).coerceIn(0.01f, 0.033f)
                lastTime = now
                engine.update(dt)
                frameTick++

                if (engine.gameState == GameState.WON) {
                    onLevelWon(engine)
                } else if (engine.gameState == GameState.LOST) {
                    onLevelLost(engine)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("screen_gameplay")
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_tropical_island_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(engine, isPaused) {
                    if (isPaused) return@pointerInput
                    detectDragGestures(
                        onDragStart = { offset ->
                            val scaleX = 1000f / size.width
                            val scaleY = 1600f / size.height
                            engine.onTouchDown(offset.x * scaleX, offset.y * scaleY)
                        },
                        onDrag = { change, _ ->
                            val scaleX = 1000f / size.width
                            val scaleY = 1600f / size.height
                            engine.onTouchMove(change.position.x * scaleX, change.position.y * scaleY)
                        },
                        onDragEnd = {
                            engine.onTouchUp()
                        },
                        onDragCancel = {
                            engine.onTouchUp()
                        }
                    )
                }
        ) {
            @Suppress("UNUSED_VARIABLE")
            val tick = frameTick

            val canvasW = size.width
            val canvasH = size.height
            val scaleX = canvasW / 1000f
            val scaleY = canvasH / 1600f

            scale(scaleX = scaleX, scaleY = scaleY, pivot = Offset.Zero) {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0x3300838F), Color(0xCC006064), Color(0xFF004D40)),
                        startY = 1040f,
                        endY = 1600f
                    ),
                    topLeft = Offset(0f, 1040f),
                    size = Size(1000f, 560f)
                )

                val leftCliff = Path().apply {
                    moveTo(0f, 1050f)
                    lineTo(280f, 1050f)
                    cubicTo(270f, 1200f, 220f, 1400f, 180f, 1600f)
                    lineTo(0f, 1600f)
                    close()
                }
                drawPath(leftCliff, Color(0xFF2E7D32))
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFF81C784), Color(0xFF388E3C))),
                    topLeft = Offset(0f, 1044f),
                    size = Size(285f, 18f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                )

                val rightCliff = Path().apply {
                    moveTo(560f, 1100f)
                    lineTo(1000f, 1100f)
                    lineTo(1000f, 1600f)
                    lineTo(620f, 1600f)
                    close()
                }
                drawPath(rightCliff, Color(0xFF1B5E20))
                drawRoundRect(
                    brush = Brush.verticalGradient(listOf(Color(0xFF66BB6A), Color(0xFF2E7D32))),
                    topLeft = Offset(555f, 1094f),
                    size = Size(450f, 18f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
                )

                OriginalGameVisuals.drawSlingshotBackFork(this, engine.slingshotX, engine.slingshotY)

                if (engine.gameState == GameState.AIMING) {
                    OriginalGameVisuals.drawRubberBand(
                        this,
                        engine.slingshotX - 20f,
                        engine.slingshotY - 50f,
                        engine.dragX,
                        engine.dragY
                    )
                }

                if (engine.gameState == GameState.AIMING) {
                    val points = engine.getTrajectoryPoints()
                    points.forEachIndexed { idx, pt ->
                        val alpha = (1f - idx.toFloat() / points.size).coerceIn(0.25f, 1f)
                        val dotRadius = 7f - (idx * 0.25f).coerceAtLeast(0f)

                        drawCircle(
                            color = Color(1f, 0.2f, 0.2f, alpha * 0.5f),
                            radius = dotRadius * 1.5f,
                            center = Offset(pt.x, pt.y)
                        )
                        drawCircle(
                            color = Color(1f, 0.9f, 0.9f, alpha),
                            radius = dotRadius,
                            center = Offset(pt.x, pt.y)
                        )
                    }
                }

                if (engine.gameState == GameState.READY || engine.gameState == GameState.AIMING || engine.gameState == GameState.FLYING) {
                    if (engine.gameState == GameState.FLYING) {
                        engine.activeHero.trail.forEachIndexed { i, pt ->
                            val r = 6f + i * 0.5f
                            drawCircle(Color(0x66FFFFFF), radius = r, center = Offset(pt.x, pt.y))
                            drawCircle(Color(0x33FFD54F), radius = r * 1.4f, center = Offset(pt.x, pt.y))
                        }
                    }

                    OriginalGameVisuals.drawHero(
                        this,
                        engine.activeHero.hero,
                        engine.activeHero.x,
                        engine.activeHero.y,
                        engine.activeHero.radius
                    )
                }

                if (engine.gameState == GameState.AIMING) {
                    OriginalGameVisuals.drawRubberBand(
                        this,
                        engine.slingshotX + 22f,
                        engine.slingshotY - 50f,
                        engine.dragX,
                        engine.dragY
                    )
                }

                OriginalGameVisuals.drawSlingshotFrontFork(this, engine.slingshotX, engine.slingshotY)

                for (block in engine.blocks) {
                    if (!block.isDestroyed) {
                        OriginalGameVisuals.drawBlock(this, block)
                    }
                }

                for (enemy in engine.enemies) {
                    if (!enemy.isDestroyed) {
                        OriginalGameVisuals.drawEnemy(this, enemy)
                    }
                }

                for (p in engine.particles) {
                    drawCircle(
                        color = p.color.copy(alpha = p.life.coerceIn(0f, 1f)),
                        radius = p.size * p.life.coerceIn(0.2f, 1.2f),
                        center = Offset(p.x, p.y)
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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
                    .border(2.5.dp, Color.White, CircleShape)
                    .clickable { isPaused = true }
                    .testTag("btn_pause"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Pause,
                    contentDescription = "Pause",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Box(
                modifier = Modifier
                    .shadow(6.dp, RoundedCornerShape(22.dp))
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xCC000000))
                    .border(2.dp, Color(0x66FFFFFF), RoundedCornerShape(22.dp))
                    .padding(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Level ${engine.levelData.levelNumber} • ${engine.levelData.title}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Score: ${engine.score}",
                        color = Color(0xFFFFCA28),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val stars = engine.calculateStars()
                repeat(3) { i ->
                    val active = i < stars
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (active) Color(0xFFFFCA28) else Color(0x889E9E9E),
                        modifier = Modifier
                            .size(26.dp)
                            .shadow(if (active) 4.dp else 0.dp, CircleShape)
                    )
                }
            }
        }

        if (isPaused) {
            Dialog(onDismissRequest = { isPaused = false }) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = Color(0xFFFFF8E1),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .border(3.5.dp, Color(0xFFFFB300), RoundedCornerShape(28.dp))
                        .shadow(20.dp, RoundedCornerShape(28.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(26.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "PAUSED",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFBF360C)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { isPaused = false },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(6.dp, RoundedCornerShape(18.dp))
                                .testTag("btn_resume")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Resume", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                isPaused = false
                                onRestartClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(6.dp, RoundedCornerShape(18.dp))
                                .testTag("btn_restart")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Restart Level", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Black)
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onToggleSound(!soundEnabled) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(6.dp, RoundedCornerShape(18.dp))
                                .testTag("btn_toggle_sound")
                        ) {
                            Text(
                                text = if (soundEnabled) "Sound: ON" else "Sound: OFF",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                isPaused = false
                                onHomeClick()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF546E7A)),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(6.dp, RoundedCornerShape(18.dp))
                                .testTag("btn_pause_home")
                        ) {
                            Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Exit to Home", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}
