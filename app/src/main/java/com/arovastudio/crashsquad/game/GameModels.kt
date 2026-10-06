package com.arovastudio.crashsquad.game

import androidx.compose.ui.graphics.Color
import com.arovastudio.crashsquad.heroes.Hero
import kotlin.math.sqrt

data class Vector2D(var x: Float = 0f, var y: Float = 0f) {
    fun length(): Float = sqrt(x * x + y * y)
    fun normalize(): Vector2D {
        val len = length()
        return if (len > 0.0001f) Vector2D(x / len, y / len) else Vector2D(0f, 0f)
    }
    operator fun plus(other: Vector2D) = Vector2D(x + other.x, y + other.y)
    operator fun minus(other: Vector2D) = Vector2D(x - other.x, y - other.y)
    operator fun times(scalar: Float) = Vector2D(x * scalar, y * scalar)
}

enum class BlockMaterial(
    val baseHp: Float,
    val scoreValue: Int,
    val density: Float
) {
    WOOD(40f, 150, 1.0f),
    STONE(90f, 250, 2.0f),
    CRATE(25f, 100, 0.8f),
    TNT(15f, 500, 0.7f)
}

enum class EnemyExpression {
    HAPPY,
    NERVOUS,
    SCARED,
    DEFEATED
}

data class Block(
    val id: Int,
    var x: Float,
    var y: Float,
    val width: Float,
    val height: Float,
    val material: BlockMaterial,
    var hp: Float = material.baseHp,
    val maxHp: Float = material.baseHp,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var rotation: Float = 0f,
    var vRot: Float = 0f,
    var isDestroyed: Boolean = false
) {
    fun takeDamage(amount: Float): Boolean {
        hp -= amount
        if (hp <= 0) {
            isDestroyed = true
            return true
        }
        return false
    }
}

data class EnemyTarget(
    val id: Int,
    var x: Float,
    var y: Float,
    val radius: Float = 26f,
    var hp: Float = 30f,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var expression: EnemyExpression = EnemyExpression.HAPPY,
    var isDestroyed: Boolean = false
) {
    fun takeDamage(amount: Float): Boolean {
        hp -= amount
        if (hp <= 0) {
            isDestroyed = true
            expression = EnemyExpression.DEFEATED
            return true
        } else {
            expression = EnemyExpression.SCARED
        }
        return false
    }
}

data class ProjectileHero(
    var x: Float,
    var y: Float,
    val radius: Float = 26f,
    var vx: Float = 0f,
    var vy: Float = 0f,
    val hero: Hero,
    val heroPower: Int,
    var isLaunched: Boolean = false,
    var isSettled: Boolean = false,
    val trail: MutableList<Vector2D> = mutableListOf(),
    var lifetimeAfterImpact: Float = 0f
)

data class Particle(
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Color,
    val size: Float,
    var life: Float = 1.0f,
    val maxLife: Float = 1.0f
)

data class BlockTemplate(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val material: BlockMaterial
)

data class EnemyTemplate(
    val x: Float,
    val y: Float
)

data class LevelData(
    val levelNumber: Int,
    val title: String,
    val blocks: List<BlockTemplate>,
    val enemies: List<EnemyTemplate>,
    val totalShots: Int = 3,
    val rewardCoins: Int = 100,
    val rewardGems: Int = 2
)

enum class GameState {
    READY,
    AIMING,
    FLYING,
    SETTLING,
    WON,
    LOST
}
