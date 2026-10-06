package com.arovastudio.crashsquad.heroes

import androidx.compose.ui.graphics.Color
import com.arovastudio.crashsquad.ui.theme.HeroAquaColor
import com.arovastudio.crashsquad.ui.theme.HeroBlazeColor
import com.arovastudio.crashsquad.ui.theme.HeroLeafColor
import com.arovastudio.crashsquad.ui.theme.HeroRockyColor

data class Hero(
    val id: String,
    val name: String,
    val title: String,
    val description: String,
    val primaryColor: Color,
    val accentColor: Color,
    val basePower: Int,
    val weightMultiplier: Float,
    val bounceMultiplier: Float,
    val unlockGemsCost: Int = 0,
    val abilityDescription: String
) {
    fun calculatePower(level: Int): Int = basePower + (level - 1) * 25
    fun upgradeCost(level: Int): Int = level * 500
}

object HeroRegistry {
    val ALL_HEROES = listOf(
        Hero(
            id = "blaze",
            name = "Blaze",
            title = "Flame Striker",
            description = "Spunky leader with high-energy explosive impact.",
            primaryColor = HeroBlazeColor,
            accentColor = Color(0xFFFFEB3B),
            basePower = 100,
            weightMultiplier = 1.0f,
            bounceMultiplier = 0.5f,
            unlockGemsCost = 0,
            abilityDescription = "Heavy kinetic shockwave on impact"
        ),
        Hero(
            id = "aqua",
            name = "Aqua",
            title = "Tide Breaker",
            description = "Swift hydro-diver that splashes through wooden beams.",
            primaryColor = HeroAquaColor,
            accentColor = Color(0xFFE0F7FA),
            basePower = 115,
            weightMultiplier = 0.9f,
            bounceMultiplier = 0.65f,
            unlockGemsCost = 100,
            abilityDescription = "Water slice with extra wooden structure penetration"
        ),
        Hero(
            id = "rocky",
            name = "Rocky",
            title = "Boulder Crusher",
            description = "Massive heavyweight titan built to shatter stone blocks.",
            primaryColor = HeroRockyColor,
            accentColor = Color(0xFFCFD8DC),
            basePower = 150,
            weightMultiplier = 1.6f,
            bounceMultiplier = 0.3f,
            unlockGemsCost = 250,
            abilityDescription = "Crushing density that topples heavy stone pillars"
        ),
        Hero(
            id = "leaf",
            name = "Leaf",
            title = "Gale Hopper",
            description = "Agile featherweight acrobat with extraordinary bounce velocity.",
            primaryColor = HeroLeafColor,
            accentColor = Color(0xFFC8E6C9),
            basePower = 95,
            weightMultiplier = 0.8f,
            bounceMultiplier = 0.85f,
            unlockGemsCost = 400,
            abilityDescription = "Multi-ricochet agility hitting hard-to-reach targets"
        )
    )

    fun getHeroById(id: String): Hero {
        return ALL_HEROES.find { it.id == id } ?: ALL_HEROES.first()
    }
}
