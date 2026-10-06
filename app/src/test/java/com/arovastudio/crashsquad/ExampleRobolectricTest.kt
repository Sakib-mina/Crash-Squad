package com.arovastudio.crashsquad

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.arovastudio.crashsquad.game.GameEngine
import com.arovastudio.crashsquad.game.GameLevels
import com.arovastudio.crashsquad.heroes.HeroRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Crash Squad", appName)
    }

    @Test
    fun `test hero registry contains 4 original heroes`() {
        val heroes = HeroRegistry.ALL_HEROES
        assertEquals(4, heroes.size)
        assertNotNull(HeroRegistry.getHeroById("blaze"))
        assertNotNull(HeroRegistry.getHeroById("aqua"))
        assertNotNull(HeroRegistry.getHeroById("rocky"))
        assertNotNull(HeroRegistry.getHeroById("leaf"))
    }

    @Test
    fun `test 50 game levels defined`() {
        assertEquals(50, GameLevels.ALL_LEVELS.size)
        val level1 = GameLevels.getLevel(1)
        assertTrue(level1.blocks.isNotEmpty())
        assertTrue(level1.enemies.isNotEmpty())
    }

    @Test
    fun `test game engine initialization`() {
        val level1 = GameLevels.getLevel(1)
        val hero = HeroRegistry.getHeroById("blaze")
        val engine = GameEngine(
            levelData = level1,
            selectedHero = hero,
            heroPower = 100
        )
        assertEquals(3, engine.remainingShots)
        assertEquals(com.arovastudio.crashsquad.game.GameState.READY, engine.gameState)
        assertEquals(2, engine.enemies.size)
    }
}
