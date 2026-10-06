package com.arovastudio.crashsquad.game

import androidx.compose.ui.graphics.Color
import com.arovastudio.crashsquad.heroes.Hero
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

class GameEngine(
    val levelData: LevelData,
    val selectedHero: Hero,
    val heroPower: Int,
    val onSoundEffect: (String) -> Unit = {},
    val onVibrate: () -> Unit = {}
) {
    val slingshotX = 180f
    val slingshotY = 1000f
    val groundY = 1100f
    val maxDragRadius = 140f

    var gameState: GameState = GameState.READY
    var currentShotIndex: Int = 1
    var remainingShots: Int = levelData.totalShots
    var score: Int = 0
    var blocksDestroyedCount: Int = 0

    var dragX: Float = slingshotX
    var dragY: Float = slingshotY
    var isDragging: Boolean = false

    var activeHero: ProjectileHero = createNewHeroProjectile()
    val blocks: MutableList<Block> = mutableListOf()
    val enemies: MutableList<EnemyTarget> = mutableListOf()
    val particles: MutableList<Particle> = mutableListOf()

    private val gravity = 0.55f
    private val worldFriction = 0.985f
    private var settleTimer = 0

    init {
        resetLevel()
    }

    fun resetLevel() {
        gameState = GameState.READY
        remainingShots = levelData.totalShots
        score = 0
        blocksDestroyedCount = 0
        blocks.clear()
        enemies.clear()
        particles.clear()
        dragX = slingshotX
        dragY = slingshotY
        isDragging = false

        var blockIdCounter = 1
        levelData.blocks.forEach { template ->
            blocks.add(
                Block(
                    id = blockIdCounter++,
                    x = template.x,
                    y = template.y,
                    width = template.width,
                    height = template.height,
                    material = template.material
                )
            )
        }

        var enemyIdCounter = 1
        levelData.enemies.forEach { template ->
            enemies.add(
                EnemyTarget(
                    id = enemyIdCounter++,
                    x = template.x,
                    y = template.y
                )
            )
        }

        activeHero = createNewHeroProjectile()
    }

    private fun createNewHeroProjectile(): ProjectileHero {
        return ProjectileHero(
            x = slingshotX,
            y = slingshotY,
            radius = 28f,
            hero = selectedHero,
            heroPower = heroPower
        )
    }

    fun onTouchDown(touchX: Float, touchY: Float) {
        if (gameState != GameState.READY) return
        val dx = touchX - slingshotX
        val dy = touchY - slingshotY
        val dist = sqrt(dx * dx + dy * dy)
        if (dist <= maxDragRadius * 1.6f) {
            isDragging = true
            gameState = GameState.AIMING
            updateDragPosition(touchX, touchY)
            onSoundEffect("slingshot_pull")
        }
    }

    fun onTouchMove(touchX: Float, touchY: Float) {
        if (isDragging) {
            updateDragPosition(touchX, touchY)
        }
    }

    private fun updateDragPosition(touchX: Float, touchY: Float) {
        val dx = touchX - slingshotX
        val dy = touchY - slingshotY
        val dist = sqrt(dx * dx + dy * dy)
        if (dist > maxDragRadius) {
            val angle = atan2(dy, dx)
            dragX = slingshotX + cos(angle) * maxDragRadius
            dragY = slingshotY + sin(angle) * maxDragRadius
        } else {
            dragX = touchX
            dragY = touchY
        }
        activeHero.x = dragX
        activeHero.y = dragY
    }

    fun onTouchUp() {
        if (!isDragging) return
        isDragging = false

        val dx = slingshotX - dragX
        val dy = slingshotY - dragY
        val dist = sqrt(dx * dx + dy * dy)

        if (dist > 25f) {
            val speedFactor = 0.28f
            activeHero.vx = dx * speedFactor
            activeHero.vy = dy * speedFactor
            activeHero.isLaunched = true
            gameState = GameState.FLYING
            remainingShots--
            onSoundEffect("launch")
            onVibrate()
        } else {
            dragX = slingshotX
            dragY = slingshotY
            activeHero.x = slingshotX
            activeHero.y = slingshotY
            gameState = GameState.READY
        }
    }

    fun getTrajectoryPoints(): List<Vector2D> {
        if (gameState != GameState.AIMING) return emptyList()
        val points = mutableListOf<Vector2D>()
        val dx = slingshotX - dragX
        val dy = slingshotY - dragY
        var simX = dragX
        var simY = dragY
        var simVx = dx * 0.28f
        var simVy = dy * 0.28f

        for (step in 0..16) {
            points.add(Vector2D(simX, simY))
            simX += simVx
            simY += simVy
            simVy += gravity
            if (simY > groundY) break
        }
        return points
    }

    fun update(dt: Float) {
        val pIter = particles.iterator()
        while (pIter.hasNext()) {
            val p = pIter.next()
            p.x += p.vx
            p.y += p.vy
            p.vy += 0.2f
            p.life -= 0.035f
            if (p.life <= 0f) {
                pIter.remove()
            }
        }

        if (gameState == GameState.FLYING) {
            activeHero.x += activeHero.vx
            activeHero.y += activeHero.vy
            activeHero.vy += gravity * activeHero.hero.weightMultiplier
            activeHero.vx *= worldFriction

            if (Random.nextFloat() > 0.4f) {
                activeHero.trail.add(Vector2D(activeHero.x, activeHero.y))
                if (activeHero.trail.size > 22) activeHero.trail.removeAt(0)
            }

            if (activeHero.y + activeHero.radius >= groundY) {
                activeHero.y = groundY - activeHero.radius
                activeHero.vy = -activeHero.vy * activeHero.hero.bounceMultiplier
                activeHero.vx *= 0.85f
                if (kotlin.math.abs(activeHero.vy) > 2f) {
                    onSoundEffect("hit_thud")
                }
            }

            if (activeHero.x < -100f || activeHero.x > 1200f || activeHero.y > 1500f) {
                activeHero.isSettled = true
            }

            for (block in blocks) {
                if (!block.isDestroyed && checkCircleBoxCollision(activeHero.x, activeHero.y, activeHero.radius, block)) {
                    handleHeroBlockCollision(activeHero, block)
                }
            }

            for (enemy in enemies) {
                if (!enemy.isDestroyed && checkCircleCircleCollision(activeHero.x, activeHero.y, activeHero.radius, enemy.x, enemy.y, enemy.radius)) {
                    handleHeroEnemyCollision(activeHero, enemy)
                }
            }

            val heroSpeed = sqrt(activeHero.vx * activeHero.vx + activeHero.vy * activeHero.vy)
            if (heroSpeed < 1.0f || activeHero.isSettled) {
                settleTimer++
                if (settleTimer > 50) {
                    onHeroSettled()
                }
            }
        }

        for (block in blocks) {
            if (block.isDestroyed) continue
            if (kotlin.math.abs(block.vx) > 0.1f || kotlin.math.abs(block.vy) > 0.1f || block.y + block.height < groundY) {
                block.x += block.vx
                block.y += block.vy
                block.vy += gravity * block.material.density
                block.vx *= 0.96f
                block.rotation += block.vRot
                block.vRot *= 0.95f

                if (block.y + block.height >= groundY) {
                    block.y = groundY - block.height
                    block.vy = -block.vy * 0.25f
                    block.vx *= 0.75f
                    block.vRot *= 0.5f
                }
            }
        }

        for (enemy in enemies) {
            if (enemy.isDestroyed) continue
            if (kotlin.math.abs(enemy.vx) > 0.1f || kotlin.math.abs(enemy.vy) > 0.1f || enemy.y + enemy.radius < groundY) {
                enemy.x += enemy.vx
                enemy.y += enemy.vy
                enemy.vy += gravity
                enemy.vx *= 0.95f

                if (enemy.y + enemy.radius >= groundY) {
                    enemy.y = groundY - enemy.radius
                    val impactSpeed = kotlin.math.abs(enemy.vy)
                    if (impactSpeed > 7f) {
                        enemy.takeDamage(impactSpeed * 5f)
                        if (enemy.isDestroyed) {
                            spawnEnemyDefeatParticles(enemy.x, enemy.y)
                            score += 1000
                            onSoundEffect("enemy_pop")
                        }
                    }
                    enemy.vy = -enemy.vy * 0.3f
                }
            }
        }

        val allEnemiesDefeated = enemies.all { it.isDestroyed }
        if (allEnemiesDefeated && gameState != GameState.WON) {
            gameState = GameState.WON
            score += remainingShots * 2000
            onSoundEffect("victory")
            onVibrate()
            return
        }

        if (gameState == GameState.SETTLING) {
            settleTimer++
            if (settleTimer > 40) {
                if (allEnemiesDefeated) {
                    gameState = GameState.WON
                    score += remainingShots * 2000
                    onSoundEffect("victory")
                    onVibrate()
                } else if (remainingShots > 0) {
                    activeHero = createNewHeroProjectile()
                    gameState = GameState.READY
                    settleTimer = 0
                } else {
                    gameState = GameState.LOST
                    onSoundEffect("defeat")
                }
            }
        }
    }

    private fun onHeroSettled() {
        activeHero.isSettled = true
        gameState = GameState.SETTLING
        settleTimer = 0
    }

    private fun handleHeroBlockCollision(hero: ProjectileHero, block: Block) {
        val impactSpeed = sqrt(hero.vx * hero.vx + hero.vy * hero.vy)
        val damage = impactSpeed * (hero.heroPower / 40f)

        block.vx += hero.vx * 0.35f
        block.vy += hero.vy * 0.25f - 2f
        block.vRot += (Random.nextFloat() - 0.5f) * 4f

        hero.vx *= 0.5f
        hero.vy *= 0.5f

        val destroyed = block.takeDamage(damage)
        if (destroyed) {
            blocksDestroyedCount++
            score += block.material.scoreValue
            spawnBlockDestructionParticles(block)
            onSoundEffect(if (block.material == BlockMaterial.STONE) "stone_crack" else "wood_crack")
            onVibrate()

            if (block.material == BlockMaterial.TNT) {
                triggerTntExplosion(block.x + block.width / 2f, block.y + block.height / 2f)
            }
        } else {
            onSoundEffect("hit_thud")
        }
    }

    private fun handleHeroEnemyCollision(hero: ProjectileHero, enemy: EnemyTarget) {
        val impactSpeed = sqrt(hero.vx * hero.vx + hero.vy * hero.vy)
        val damage = impactSpeed * (hero.heroPower / 30f) + 20f

        enemy.vx += hero.vx * 0.6f
        enemy.vy += hero.vy * 0.5f - 4f

        val defeated = enemy.takeDamage(damage)
        if (defeated) {
            score += 1500
            spawnEnemyDefeatParticles(enemy.x, enemy.y)
            onSoundEffect("enemy_pop")
            onVibrate()
        } else {
            onSoundEffect("hit_thud")
        }
    }

    private fun triggerTntExplosion(centerX: Float, centerY: Float) {
        onSoundEffect("explosion")
        onVibrate()

        for (i in 0..30) {
            val angle = Random.nextFloat() * 2f * Math.PI.toFloat()
            val speed = Random.nextFloat() * 14f + 3f
            particles.add(
                Particle(
                    x = centerX,
                    y = centerY,
                    vx = cos(angle) * speed,
                    vy = sin(angle) * speed,
                    color = if (i % 2 == 0) Color(0xFFFF5722) else Color(0xFFFFEB3B),
                    size = Random.nextFloat() * 14f + 8f,
                    life = 1.0f,
                    maxLife = 1.0f
                )
            )
        }

        val blastRadius = 220f
        for (block in blocks) {
            if (block.isDestroyed) continue
            val bx = block.x + block.width / 2f
            val by = block.y + block.height / 2f
            val dist = sqrt((bx - centerX) * (bx - centerX) + (by - centerY) * (by - centerY))
            if (dist < blastRadius) {
                val force = (1f - dist / blastRadius) * 20f
                val angle = atan2(by - centerY, bx - centerX)
                block.vx += cos(angle) * force
                block.vy += sin(angle) * force - 5f
                if (block.takeDamage(120f)) {
                    blocksDestroyedCount++
                    score += block.material.scoreValue
                    spawnBlockDestructionParticles(block)
                }
            }
        }

        for (enemy in enemies) {
            if (enemy.isDestroyed) continue
            val dist = sqrt((enemy.x - centerX) * (enemy.x - centerX) + (enemy.y - centerY) * (enemy.y - centerY))
            if (dist < blastRadius) {
                val force = (1f - dist / blastRadius) * 25f
                val angle = atan2(enemy.y - centerY, enemy.x - centerX)
                enemy.vx += cos(angle) * force
                enemy.vy += sin(angle) * force - 8f
                if (enemy.takeDamage(100f)) {
                    score += 1500
                    spawnEnemyDefeatParticles(enemy.x, enemy.y)
                }
            }
        }
    }

    private fun spawnBlockDestructionParticles(block: Block) {
        val color = when (block.material) {
            BlockMaterial.WOOD -> Color(0xFF8D6E63)
            BlockMaterial.STONE -> Color(0xFFB0BEC5)
            BlockMaterial.CRATE -> Color(0xFFD7CCC8)
            BlockMaterial.TNT -> Color(0xFFE53935)
        }
        for (i in 0..14) {
            particles.add(
                Particle(
                    x = block.x + Random.nextFloat() * block.width,
                    y = block.y + Random.nextFloat() * block.height,
                    vx = (Random.nextFloat() - 0.5f) * 10f,
                    vy = (Random.nextFloat() - 0.7f) * 10f,
                    color = color,
                    size = Random.nextFloat() * 10f + 4f
                )
            )
        }
    }

    private fun spawnEnemyDefeatParticles(x: Float, y: Float) {
        for (i in 0..18) {
            particles.add(
                Particle(
                    x = x,
                    y = y,
                    vx = (Random.nextFloat() - 0.5f) * 12f,
                    vy = (Random.nextFloat() - 0.7f) * 12f,
                    color = if (i % 2 == 0) Color(0xFF7CB342) else Color(0xFFFFEB3B),
                    size = Random.nextFloat() * 12f + 6f
                )
            )
        }
    }

    private fun checkCircleBoxCollision(cx: Float, cy: Float, cr: Float, block: Block): Boolean {
        val closestX = cx.coerceIn(block.x, block.x + block.width)
        val closestY = cy.coerceIn(block.y, block.y + block.height)
        val distanceX = cx - closestX
        val distanceY = cy - closestY
        return (distanceX * distanceX + distanceY * distanceY) < (cr * cr)
    }

    private fun checkCircleCircleCollision(
        x1: Float, y1: Float, r1: Float,
        x2: Float, y2: Float, r2: Float
    ): Boolean {
        val dx = x1 - x2
        val dy = y1 - y2
        val rSum = r1 + r2
        return (dx * dx + dy * dy) < (rSum * rSum)
    }

    fun calculateStars(): Int {
        return when {
            remainingShots >= 2 -> 3
            remainingShots == 1 -> 2
            else -> 1
        }
    }
}
