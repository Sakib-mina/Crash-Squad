package com.arovastudio.crashsquad.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.arovastudio.crashsquad.game.Block
import com.arovastudio.crashsquad.game.BlockMaterial
import com.arovastudio.crashsquad.game.EnemyExpression
import com.arovastudio.crashsquad.game.EnemyTarget
import com.arovastudio.crashsquad.heroes.Hero

object OriginalGameVisuals {

    fun drawSlingshotBackFork(drawScope: DrawScope, x: Float, y: Float) {
        with(drawScope) {
            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF3E2723), Color(0xFF5D4037)),
                    startX = x - 32f,
                    endX = x - 12f
                ),
                topLeft = Offset(x - 30f, y - 60f),
                size = Size(20f, 75f),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawCircle(Color(0xFF271510), radius = 6f, center = Offset(x - 20f, y - 50f))
        }
    }

    fun drawSlingshotFrontFork(drawScope: DrawScope, x: Float, y: Float) {
        with(drawScope) {
            drawRoundRect(
                color = Color(0x33000000),
                topLeft = Offset(x - 12f, y - 10f),
                size = Size(36f, 140f),
                cornerRadius = CornerRadius(12f, 12f)
            )

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF4E342E),
                        Color(0xFF8D6E63),
                        Color(0xFFA1887F),
                        Color(0xFF4E342E)
                    ),
                    startX = x - 16f,
                    endX = x + 20f
                ),
                topLeft = Offset(x - 16f, y - 15f),
                size = Size(32f, 140f),
                cornerRadius = CornerRadius(10f, 10f)
            )
            drawLine(Color(0x33FFFFFF), Offset(x - 10f, y + 25f), Offset(x + 10f, y + 25f), strokeWidth = 2f)
            drawLine(Color(0x33000000), Offset(x - 10f, y + 60f), Offset(x + 10f, y + 60f), strokeWidth = 2f)
            drawLine(Color(0x33FFFFFF), Offset(x - 10f, y + 95f), Offset(x + 10f, y + 95f), strokeWidth = 2f)

            val rightProngPath = Path().apply {
                moveTo(x + 4f, y - 10f)
                lineTo(x + 16f, y - 60f)
                cubicTo(x + 22f, y - 66f, x + 34f, y - 64f, x + 36f, y - 56f)
                lineTo(x + 22f, y - 5f)
                close()
            }
            drawPath(
                rightProngPath,
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF5D4037), Color(0xFF8D6E63), Color(0xFF3E2723)),
                    startX = x + 4f,
                    endX = x + 36f
                )
            )
            drawCircle(Color(0xFF271510), radius = 6f, center = Offset(x + 25f, y - 50f))

            drawRoundRect(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFFFB300), Color(0xFFFFE082), Color(0xFFFF8F00)),
                    startX = x - 18f,
                    endX = x + 18f
                ),
                topLeft = Offset(x - 18f, y - 10f),
                size = Size(36f, 16f),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }

    fun drawRubberBand(drawScope: DrawScope, forkX: Float, forkY: Float, heroX: Float, heroY: Float) {
        with(drawScope) {
            drawLine(
                color = Color(0x44000000),
                start = Offset(forkX + 2f, forkY + 3f),
                end = Offset(heroX + 2f, heroY + 3f),
                strokeWidth = 10f
            )
            drawLine(
                color = Color(0xFFBF360C),
                start = Offset(forkX, forkY),
                end = Offset(heroX, heroY),
                strokeWidth = 10f
            )
            drawLine(
                color = Color(0xFFFF5722),
                start = Offset(forkX, forkY),
                end = Offset(heroX, heroY),
                strokeWidth = 5f
            )
            drawLine(
                color = Color(0xFFFFAB91),
                start = Offset(forkX, forkY),
                end = Offset(heroX, heroY),
                strokeWidth = 2f
            )
        }
    }

    fun drawHero(drawScope: DrawScope, hero: Hero, cx: Float, cy: Float, radius: Float) {
        with(drawScope) {
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0x66000000), Color(0x00000000)),
                    center = Offset(cx, cy + radius * 0.95f),
                    radius = radius * 1.1f
                ),
                radius = radius * 1.1f,
                center = Offset(cx, cy + radius * 0.95f)
            )

            drawCircle(
                color = Color(0xFF1B0000),
                radius = radius + 2.5f,
                center = Offset(cx, cy)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        hero.accentColor,
                        hero.primaryColor,
                        Color(0xFF880E4F).takeIf { hero.id == "blaze" } ?: Color(0xFF00363A).takeIf { hero.id == "aqua" } ?: Color(0xFF263238)
                    ),
                    center = Offset(cx - radius * 0.35f, cy - radius * 0.38f),
                    radius = radius * 1.25f
                ),
                radius = radius,
                center = Offset(cx, cy)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0x88FFFFFF), Color(0x00FFFFFF)),
                    center = Offset(cx - radius * 0.4f, cy - radius * 0.4f),
                    radius = radius * 0.45f
                ),
                radius = radius * 0.45f,
                center = Offset(cx - radius * 0.4f, cy - radius * 0.4f)
            )

            drawOval(
                brush = Brush.verticalGradient(
                    listOf(Color(0x66FFFFFF), Color(0x11FFFFFF)),
                    startY = cy + radius * 0.1f,
                    endY = cy + radius * 0.9f
                ),
                topLeft = Offset(cx - radius * 0.5f, cy + radius * 0.1f),
                size = Size(radius * 1.0f, radius * 0.8f)
            )

            when (hero.id) {
                "blaze" -> {
                    val featherPath = Path().apply {
                        moveTo(cx - 8f, cy - radius + 2f)
                        cubicTo(cx - 18f, cy - radius - 24f, cx + 6f, cy - radius - 36f, cx + 16f, cy - radius - 10f)
                        cubicTo(cx + 8f, cy - radius - 4f, cx + 2f, cy - radius + 1f, cx - 8f, cy - radius + 2f)
                        close()
                    }
                    drawPath(
                        featherPath,
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFFFEB3B), Color(0xFFFF5722), Color(0xFFD32F2F))
                        )
                    )
                    drawPath(featherPath, Color(0xFF3E0000), style = Stroke(width = 2.5f))
                }
                "aqua" -> {
                    val dropPath = Path().apply {
                        moveTo(cx, cy - radius + 2f)
                        cubicTo(cx - 14f, cy - radius - 18f, cx - 12f, cy - radius - 30f, cx + 2f, cy - radius - 32f)
                        cubicTo(cx + 16f, cy - radius - 30f, cx + 14f, cy - radius - 16f, cx, cy - radius + 2f)
                        close()
                    }
                    drawPath(
                        dropPath,
                        brush = Brush.radialGradient(
                            listOf(Color(0xFFE0F7FA), Color(0xFF00E5FF), Color(0xFF00838F))
                        )
                    )
                    drawPath(dropPath, Color(0xFF00363A), style = Stroke(width = 2.5f))
                }
                "rocky" -> {
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            listOf(Color(0xFFCFD8DC), Color(0xFF455A64))
                        ),
                        topLeft = Offset(cx - radius * 0.75f, cy - radius * 0.55f),
                        size = Size(radius * 1.5f, radius * 0.32f),
                        cornerRadius = CornerRadius(5f, 5f)
                    )
                    drawRoundRect(
                        color = Color(0xFF1A237E),
                        topLeft = Offset(cx - radius * 0.75f, cy - radius * 0.55f),
                        size = Size(radius * 1.5f, radius * 0.32f),
                        cornerRadius = CornerRadius(5f, 5f),
                        style = Stroke(width = 2f)
                    )
                }
                "leaf" -> {
                    val leafPath = Path().apply {
                        moveTo(cx, cy - radius)
                        cubicTo(cx - 24f, cy - radius - 26f, cx - 4f, cy - radius - 34f, cx + 4f, cy - radius - 20f)
                        close()
                    }
                    drawPath(leafPath, Color(0xFF66BB6A))
                    drawPath(leafPath, Color(0xFF1B5E20), style = Stroke(width = 2.5f))
                }
            }

            val eyeRadius = radius * 0.29f
            val eyeSpacing = radius * 0.44f
            val eyeY = cy - radius * 0.08f

            drawCircle(Color(0x33000000), radius = eyeRadius * 1.15f, center = Offset(cx - eyeSpacing / 2f + 4f, eyeY + 1f))
            drawCircle(Color(0x33000000), radius = eyeRadius * 1.15f, center = Offset(cx + eyeSpacing / 2f + 8f, eyeY + 1f))

            drawCircle(
                brush = Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))),
                radius = eyeRadius,
                center = Offset(cx - eyeSpacing / 2f + 4f, eyeY)
            )
            drawCircle(
                brush = Brush.verticalGradient(listOf(Color(0xFFFFFFFF), Color(0xFFECEFF1))),
                radius = eyeRadius,
                center = Offset(cx + eyeSpacing / 2f + 8f, eyeY)
            )
            drawCircle(Color(0xFF263238), radius = eyeRadius, center = Offset(cx - eyeSpacing / 2f + 4f, eyeY), style = Stroke(width = 2f))
            drawCircle(Color(0xFF263238), radius = eyeRadius, center = Offset(cx + eyeSpacing / 2f + 8f, eyeY), style = Stroke(width = 2f))

            val pupilRadius = eyeRadius * 0.48f
            drawCircle(Color(0xFF000000), radius = pupilRadius, center = Offset(cx - eyeSpacing / 2f + 6f, eyeY))
            drawCircle(Color(0xFF000000), radius = pupilRadius, center = Offset(cx + eyeSpacing / 2f + 10f, eyeY))

            drawCircle(Color.White, radius = pupilRadius * 0.45f, center = Offset(cx - eyeSpacing / 2f + 7f, eyeY - 2.5f))
            drawCircle(Color.White, radius = pupilRadius * 0.22f, center = Offset(cx - eyeSpacing / 2f + 4f, eyeY + 2f))
            drawCircle(Color.White, radius = pupilRadius * 0.45f, center = Offset(cx + eyeSpacing / 2f + 11f, eyeY - 2.5f))
            drawCircle(Color.White, radius = pupilRadius * 0.22f, center = Offset(cx + eyeSpacing / 2f + 8f, eyeY + 2f))

            val browY = eyeY - eyeRadius * 0.95f
            drawLine(Color(0xFF1B0000), Offset(cx - eyeSpacing / 2f - 4f, browY - 2f), Offset(cx - eyeSpacing / 2f + 14f, browY + 4f), strokeWidth = 4.5f)
            drawLine(Color(0xFF1B0000), Offset(cx + eyeSpacing / 2f - 2f, browY + 4f), Offset(cx + eyeSpacing / 2f + 16f, browY - 2f), strokeWidth = 4.5f)

            val beakPath = Path().apply {
                moveTo(cx + 2f, cy + radius * 0.04f)
                lineTo(cx + radius * 0.72f, cy + radius * 0.22f)
                lineTo(cx + 4f, cy + radius * 0.42f)
                close()
            }
            drawPath(
                beakPath,
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFFFEE58), Color(0xFFFFB300), Color(0xFFF57C00))
                )
            )
            drawPath(beakPath, Color(0xFFBF360C), style = Stroke(width = 2.8f))
            drawLine(Color(0x88FFFFFF), Offset(cx + 6f, cy + radius * 0.1f), Offset(cx + radius * 0.5f, cy + radius * 0.2f), strokeWidth = 2.5f)
        }
    }

    fun drawEnemy(drawScope: DrawScope, enemy: EnemyTarget) {
        with(drawScope) {
            val cx = enemy.x
            val cy = enemy.y
            val r = enemy.radius

            drawCircle(Color(0x38000000), radius = r * 1.1f, center = Offset(cx + 2f, cy + 4f))
            drawCircle(Color(0xFF1B5E20), radius = r + 2.5f, center = Offset(cx, cy))

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFAED581), Color(0xFF7CB342), Color(0xFF33691E)),
                    center = Offset(cx - r * 0.35f, cy - r * 0.35f),
                    radius = r * 1.3f
                ),
                radius = r,
                center = Offset(cx, cy)
            )

            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color(0x77FFFFFF), Color(0x00FFFFFF)),
                    center = Offset(cx - r * 0.4f, cy - r * 0.4f),
                    radius = r * 0.4f
                ),
                radius = r * 0.4f,
                center = Offset(cx - r * 0.4f, cy - r * 0.4f)
            )

            drawCircle(Color(0xFF558B2F), radius = r * 0.28f, center = Offset(cx - r * 0.65f, cy - r * 0.72f))
            drawCircle(Color(0xFF33691E), radius = r * 0.28f, center = Offset(cx - r * 0.65f, cy - r * 0.72f), style = Stroke(2f))
            drawCircle(Color(0xFF558B2F), radius = r * 0.28f, center = Offset(cx + r * 0.65f, cy - r * 0.72f))
            drawCircle(Color(0xFF33691E), radius = r * 0.28f, center = Offset(cx + r * 0.65f, cy - r * 0.72f), style = Stroke(2f))

            when (enemy.expression) {
                EnemyExpression.HAPPY -> {
                    drawCircle(Color.White, radius = r * 0.3f, center = Offset(cx - r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color(0xFF1B5E20), radius = r * 0.3f, center = Offset(cx - r * 0.35f, cy - r * 0.12f), style = Stroke(2f))
                    drawCircle(Color.White, radius = r * 0.3f, center = Offset(cx + r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color(0xFF1B5E20), radius = r * 0.3f, center = Offset(cx + r * 0.35f, cy - r * 0.12f), style = Stroke(2f))

                    drawCircle(Color.Black, radius = r * 0.14f, center = Offset(cx - r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color.Black, radius = r * 0.14f, center = Offset(cx + r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color.White, radius = r * 0.05f, center = Offset(cx - r * 0.32f, cy - r * 0.15f))
                    drawCircle(Color.White, radius = r * 0.05f, center = Offset(cx + r * 0.38f, cy - r * 0.15f))
                }
                EnemyExpression.SCARED, EnemyExpression.NERVOUS -> {
                    drawCircle(Color.White, radius = r * 0.36f, center = Offset(cx - r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color(0xFF1B5E20), radius = r * 0.36f, center = Offset(cx - r * 0.35f, cy - r * 0.12f), style = Stroke(2f))
                    drawCircle(Color.White, radius = r * 0.36f, center = Offset(cx + r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color(0xFF1B5E20), radius = r * 0.36f, center = Offset(cx + r * 0.35f, cy - r * 0.12f), style = Stroke(2f))

                    drawCircle(Color.Black, radius = r * 0.09f, center = Offset(cx - r * 0.35f, cy - r * 0.12f))
                    drawCircle(Color.Black, radius = r * 0.09f, center = Offset(cx + r * 0.35f, cy - r * 0.12f))
                }
                EnemyExpression.DEFEATED -> {
                    drawLine(Color(0xFF1B5E20), Offset(cx - r * 0.5f, cy - r * 0.25f), Offset(cx - r * 0.2f, cy + r * 0.02f), strokeWidth = 3.8f)
                    drawLine(Color(0xFF1B5E20), Offset(cx - r * 0.2f, cy - r * 0.25f), Offset(cx - r * 0.5f, cy + r * 0.02f), strokeWidth = 3.8f)
                    drawLine(Color(0xFF1B5E20), Offset(cx + r * 0.2f, cy - r * 0.25f), Offset(cx + r * 0.5f, cy + r * 0.02f), strokeWidth = 3.8f)
                    drawLine(Color(0xFF1B5E20), Offset(cx + r * 0.5f, cy - r * 0.25f), Offset(cx + r * 0.2f, cy + r * 0.02f), strokeWidth = 3.8f)
                }
            }

            drawOval(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF9CCC65), Color(0xFF689F38))
                ),
                topLeft = Offset(cx - r * 0.38f, cy + r * 0.10f),
                size = Size(r * 0.76f, r * 0.48f)
            )
            drawOval(
                color = Color(0xFF1B5E20),
                topLeft = Offset(cx - r * 0.38f, cy + r * 0.10f),
                size = Size(r * 0.76f, r * 0.48f),
                style = Stroke(width = 2.5f)
            )
            drawCircle(Color(0xFF2E7D32), radius = 3.5f, center = Offset(cx - 7f, cy + r * 0.32f))
            drawCircle(Color(0xFF2E7D32), radius = 3.5f, center = Offset(cx + 7f, cy + r * 0.32f))
        }
    }

    fun drawBlock(drawScope: DrawScope, block: Block) {
        with(drawScope) {
            rotate(degrees = block.rotation, pivot = Offset(block.x + block.width / 2f, block.y + block.height / 2f)) {
                when (block.material) {
                    BlockMaterial.WOOD -> {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFBCAAA4), Color(0xFF8D6E63), Color(0xFF5D4037)),
                                startY = block.y,
                                endY = block.y + block.height
                            ),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(5f, 5f)
                        )
                        drawLine(
                            color = Color(0x66FFFFFF),
                            start = Offset(block.x + 2f, block.y + 2f),
                            end = Offset(block.x + block.width - 2f, block.y + 2f),
                            strokeWidth = 2f
                        )
                        drawLine(
                            color = Color(0x66000000),
                            start = Offset(block.x + 2f, block.y + block.height - 2f),
                            end = Offset(block.x + block.width - 2f, block.y + block.height - 2f),
                            strokeWidth = 2f
                        )
                        drawRoundRect(
                            color = Color(0xFF3E2723),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(5f, 5f),
                            style = Stroke(width = 2.5f)
                        )
                        if (block.height > 25f && block.width > 25f) {
                            drawCircle(Color(0xFF3E2723), radius = 2.5f, center = Offset(block.x + 6f, block.y + 6f))
                            drawCircle(Color(0xFF3E2723), radius = 2.5f, center = Offset(block.x + block.width - 6f, block.y + 6f))
                        }
                    }
                    BlockMaterial.STONE -> {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFFCFD8DC), Color(0xFF90A4AE), Color(0xFF546E7A)),
                                startY = block.y,
                                endY = block.y + block.height
                            ),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawLine(
                            color = Color(0x88FFFFFF),
                            start = Offset(block.x + 2f, block.y + 2f),
                            end = Offset(block.x + block.width - 2f, block.y + 2f),
                            strokeWidth = 2.5f
                        )
                        drawLine(
                            color = Color(0x88FFFFFF),
                            start = Offset(block.x + 2f, block.y + 2f),
                            end = Offset(block.x + 2f, block.y + block.height - 2f),
                            strokeWidth = 2.5f
                        )
                        drawRoundRect(
                            color = Color(0xFF263238),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(6f, 6f),
                            style = Stroke(width = 2.5f)
                        )
                        drawLine(
                            color = Color(0x44263238),
                            start = Offset(block.x + block.width * 0.3f, block.y + 4f),
                            end = Offset(block.x + block.width * 0.45f, block.y + block.height * 0.6f),
                            strokeWidth = 1.5f
                        )
                    }
                    BlockMaterial.CRATE -> {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFD7CCC8), Color(0xFFA1887F))
                            ),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(5f, 5f)
                        )
                        drawRoundRect(
                            color = Color(0xFF4E342E),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(5f, 5f),
                            style = Stroke(width = 2.5f)
                        )
                        drawLine(Color(0xFF6D4C41), Offset(block.x + 4f, block.y + 4f), Offset(block.x + block.width - 4f, block.y + block.height - 4f), strokeWidth = 3f)
                        drawLine(Color(0xFF6D4C41), Offset(block.x + block.width - 4f, block.y + 4f), Offset(block.x + 4f, block.y + block.height - 4f), strokeWidth = 3f)
                    }
                    BlockMaterial.TNT -> {
                        drawRoundRect(
                            brush = Brush.verticalGradient(
                                listOf(Color(0xFFEF5350), Color(0xFFD32F2F), Color(0xFFB71C1C))
                            ),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        drawRoundRect(
                            color = Color(0xFF4A0000),
                            topLeft = Offset(block.x, block.y),
                            size = Size(block.width, block.height),
                            cornerRadius = CornerRadius(6f, 6f),
                            style = Stroke(width = 2.5f)
                        )
                        drawRect(
                            color = Color(0xFFFFEB3B),
                            topLeft = Offset(block.x + 3f, block.y + block.height * 0.32f),
                            size = Size(block.width - 6f, block.height * 0.36f)
                        )
                        drawLine(Color(0xFF212121), Offset(block.x + 8f, block.y + block.height * 0.42f), Offset(block.x + 18f, block.y + block.height * 0.42f), strokeWidth = 3f)
                        drawLine(Color(0xFF212121), Offset(block.x + 13f, block.y + block.height * 0.42f), Offset(block.x + 13f, block.y + block.height * 0.58f), strokeWidth = 3f)

                        drawLine(Color(0xFF212121), Offset(block.x + block.width - 18f, block.y + block.height * 0.42f), Offset(block.x + block.width - 8f, block.y + block.height * 0.42f), strokeWidth = 3f)
                        drawLine(Color(0xFF212121), Offset(block.x + block.width - 13f, block.y + block.height * 0.42f), Offset(block.x + block.width - 13f, block.y + block.height * 0.58f), strokeWidth = 3f)
                    }
                }
            }
        }
    }
}
