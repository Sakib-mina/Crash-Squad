package com.arovastudio.crashsquad.game

object GameLevels {

    fun getLevel(levelNumber: Int): LevelData {
        return ALL_LEVELS.find { it.levelNumber == levelNumber } ?: ALL_LEVELS.first()
    }

    private val HANDCRAFTED_LEVELS_1_TO_10 = listOf(
        LevelData(
            levelNumber = 1,
            title = "First Strike",
            blocks = listOf(
                BlockTemplate(680f, 1030f, 32f, 110f, BlockMaterial.WOOD),
                BlockTemplate(820f, 1030f, 32f, 110f, BlockMaterial.WOOD),
                BlockTemplate(660f, 950f, 200f, 28f, BlockMaterial.WOOD),
                BlockTemplate(735f, 890f, 50f, 50f, BlockMaterial.CRATE)
            ),
            enemies = listOf(
                EnemyTemplate(750f, 1030f),
                EnemyTemplate(750f, 830f)
            ),
            totalShots = 3,
            rewardCoins = 100,
            rewardGems = 2
        ),

        LevelData(
            levelNumber = 2,
            title = "Twin Pillars",
            blocks = listOf(
                BlockTemplate(650f, 1040f, 32f, 100f, BlockMaterial.WOOD),
                BlockTemplate(720f, 1040f, 32f, 100f, BlockMaterial.WOOD),
                BlockTemplate(640f, 970f, 120f, 26f, BlockMaterial.WOOD),
                BlockTemplate(800f, 1040f, 32f, 100f, BlockMaterial.WOOD),
                BlockTemplate(870f, 1040f, 32f, 100f, BlockMaterial.WOOD),
                BlockTemplate(790f, 970f, 120f, 26f, BlockMaterial.WOOD),
                BlockTemplate(710f, 920f, 150f, 26f, BlockMaterial.WOOD)
            ),
            enemies = listOf(
                EnemyTemplate(685f, 1040f),
                EnemyTemplate(835f, 1040f),
                EnemyTemplate(760f, 880f)
            ),
            totalShots = 3,
            rewardCoins = 120,
            rewardGems = 2
        ),

        LevelData(
            levelNumber = 3,
            title = "Stone Fortress",
            blocks = listOf(
                BlockTemplate(670f, 1040f, 55f, 65f, BlockMaterial.STONE),
                BlockTemplate(810f, 1040f, 55f, 65f, BlockMaterial.STONE),
                BlockTemplate(650f, 980f, 230f, 28f, BlockMaterial.WOOD),
                BlockTemplate(700f, 900f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(780f, 900f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(740f, 920f, 40f, 40f, BlockMaterial.TNT),
                BlockTemplate(680f, 840f, 150f, 24f, BlockMaterial.WOOD)
            ),
            enemies = listOf(
                EnemyTemplate(740f, 1040f),
                EnemyTemplate(740f, 800f)
            ),
            totalShots = 3,
            rewardCoins = 150,
            rewardGems = 3
        ),

        LevelData(
            levelNumber = 4,
            title = "Crate Mountain",
            blocks = listOf(
                BlockTemplate(660f, 1040f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(720f, 1040f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(780f, 1040f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(840f, 1040f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(690f, 980f, 50f, 50f, BlockMaterial.WOOD),
                BlockTemplate(750f, 980f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(810f, 980f, 50f, 50f, BlockMaterial.WOOD),
                BlockTemplate(720f, 920f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(780f, 920f, 50f, 50f, BlockMaterial.CRATE),
                BlockTemplate(750f, 860f, 50f, 50f, BlockMaterial.TNT)
            ),
            enemies = listOf(
                EnemyTemplate(750f, 1040f),
                EnemyTemplate(690f, 920f),
                EnemyTemplate(810f, 920f)
            ),
            totalShots = 3,
            rewardCoins = 180,
            rewardGems = 3
        ),

        LevelData(
            levelNumber = 5,
            title = "Sky Sentry",
            blocks = listOf(
                BlockTemplate(720f, 1040f, 50f, 60f, BlockMaterial.STONE),
                BlockTemplate(800f, 1040f, 50f, 60f, BlockMaterial.STONE),
                BlockTemplate(700f, 990f, 170f, 25f, BlockMaterial.WOOD),
                BlockTemplate(730f, 900f, 25f, 90f, BlockMaterial.WOOD),
                BlockTemplate(790f, 900f, 25f, 90f, BlockMaterial.WOOD),
                BlockTemplate(710f, 860f, 130f, 25f, BlockMaterial.WOOD),
                BlockTemplate(740f, 790f, 25f, 70f, BlockMaterial.WOOD),
                BlockTemplate(780f, 790f, 25f, 70f, BlockMaterial.WOOD),
                BlockTemplate(720f, 750f, 100f, 20f, BlockMaterial.WOOD)
            ),
            enemies = listOf(
                EnemyTemplate(760f, 1040f),
                EnemyTemplate(760f, 930f),
                EnemyTemplate(760f, 715f)
            ),
            totalShots = 3,
            rewardCoins = 200,
            rewardGems = 4
        ),

        LevelData(
            levelNumber = 6,
            title = "Blast Bunker",
            blocks = listOf(
                BlockTemplate(650f, 1040f, 50f, 70f, BlockMaterial.STONE),
                BlockTemplate(740f, 1040f, 45f, 45f, BlockMaterial.TNT),
                BlockTemplate(820f, 1040f, 50f, 70f, BlockMaterial.STONE),
                BlockTemplate(640f, 980f, 240f, 28f, BlockMaterial.STONE),
                BlockTemplate(670f, 900f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(790f, 900f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(730f, 920f, 45f, 45f, BlockMaterial.TNT),
                BlockTemplate(660f, 840f, 170f, 24f, BlockMaterial.WOOD)
            ),
            enemies = listOf(
                EnemyTemplate(680f, 1040f),
                EnemyTemplate(800f, 1040f),
                EnemyTemplate(740f, 800f)
            ),
            totalShots = 3,
            rewardCoins = 220,
            rewardGems = 4
        ),

        LevelData(
            levelNumber = 7,
            title = "Castle Gate",
            blocks = listOf(
                BlockTemplate(640f, 1030f, 60f, 80f, BlockMaterial.STONE),
                BlockTemplate(840f, 1030f, 60f, 80f, BlockMaterial.STONE),
                BlockTemplate(630f, 960f, 280f, 30f, BlockMaterial.STONE),
                BlockTemplate(690f, 880f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(780f, 880f, 30f, 80f, BlockMaterial.WOOD),
                BlockTemplate(670f, 820f, 160f, 25f, BlockMaterial.WOOD),
                BlockTemplate(730f, 770f, 45f, 45f, BlockMaterial.CRATE)
            ),
            enemies = listOf(
                EnemyTemplate(710f, 1040f),
                EnemyTemplate(770f, 1040f),
                EnemyTemplate(735f, 840f)
            ),
            totalShots = 3,
            rewardCoins = 250,
            rewardGems = 5
        ),

        LevelData(
            levelNumber = 8,
            title = "Suspension Ridge",
            blocks = listOf(
                BlockTemplate(630f, 1040f, 40f, 90f, BlockMaterial.STONE),
                BlockTemplate(730f, 1040f, 40f, 90f, BlockMaterial.WOOD),
                BlockTemplate(830f, 1040f, 40f, 90f, BlockMaterial.STONE),
                BlockTemplate(610f, 960f, 270f, 26f, BlockMaterial.WOOD),
                BlockTemplate(660f, 900f, 45f, 45f, BlockMaterial.CRATE),
                BlockTemplate(730f, 900f, 45f, 45f, BlockMaterial.TNT),
                BlockTemplate(790f, 900f, 45f, 45f, BlockMaterial.CRATE)
            ),
            enemies = listOf(
                EnemyTemplate(680f, 1040f),
                EnemyTemplate(780f, 1040f),
                EnemyTemplate(660f, 840f),
                EnemyTemplate(790f, 840f)
            ),
            totalShots = 4,
            rewardCoins = 280,
            rewardGems = 5
        ),

        LevelData(
            levelNumber = 9,
            title = "Double Bastion",
            blocks = listOf(
                BlockTemplate(620f, 1040f, 45f, 70f, BlockMaterial.STONE),
                BlockTemplate(690f, 1040f, 45f, 70f, BlockMaterial.STONE),
                BlockTemplate(610f, 980f, 135f, 25f, BlockMaterial.WOOD),
                BlockTemplate(645f, 910f, 45f, 45f, BlockMaterial.CRATE),
                BlockTemplate(780f, 1040f, 45f, 70f, BlockMaterial.STONE),
                BlockTemplate(850f, 1040f, 45f, 70f, BlockMaterial.STONE),
                BlockTemplate(770f, 980f, 135f, 25f, BlockMaterial.WOOD),
                BlockTemplate(805f, 910f, 45f, 45f, BlockMaterial.TNT)
            ),
            enemies = listOf(
                EnemyTemplate(655f, 1040f),
                EnemyTemplate(815f, 1040f),
                EnemyTemplate(645f, 850f),
                EnemyTemplate(805f, 850f)
            ),
            totalShots = 4,
            rewardCoins = 300,
            rewardGems = 6
        ),

        LevelData(
            levelNumber = 10,
            title = "Citadel Core",
            blocks = listOf(
                BlockTemplate(630f, 1040f, 55f, 80f, BlockMaterial.STONE),
                BlockTemplate(730f, 1040f, 45f, 45f, BlockMaterial.TNT),
                BlockTemplate(830f, 1040f, 55f, 80f, BlockMaterial.STONE),
                BlockTemplate(610f, 970f, 290f, 30f, BlockMaterial.STONE),
                BlockTemplate(660f, 890f, 35f, 80f, BlockMaterial.WOOD),
                BlockTemplate(780f, 890f, 35f, 80f, BlockMaterial.WOOD),
                BlockTemplate(640f, 830f, 200f, 25f, BlockMaterial.WOOD),
                BlockTemplate(700f, 760f, 40f, 40f, BlockMaterial.CRATE),
                BlockTemplate(750f, 760f, 40f, 40f, BlockMaterial.TNT)
            ),
            enemies = listOf(
                EnemyTemplate(680f, 1040f),
                EnemyTemplate(790f, 1040f),
                EnemyTemplate(720f, 890f),
                EnemyTemplate(725f, 700f)
            ),
            totalShots = 4,
            rewardCoins = 500,
            rewardGems = 10
        )
    )

    val ALL_LEVELS: List<LevelData> = buildList {
        addAll(HANDCRAFTED_LEVELS_1_TO_10)
        for (lvl in 11..50) {
            add(generateLevel(lvl))
        }
    }

    private fun generateLevel(levelNumber: Int): LevelData {
        val archetype = (levelNumber - 11) % 5
        val isBoss = (levelNumber % 5 == 0)

        val totalShots = if (isBoss) 5 else if (levelNumber > 30) 4 else 3
        val rewardCoins = 300 + (levelNumber - 10) * 20
        val rewardGems = 5 + (levelNumber / 5)

        val title = when {
            isBoss -> "Citadel Overlord $levelNumber"
            archetype == 0 -> "Fortress Rampart $levelNumber"
            archetype == 1 -> "Explosive Ridge $levelNumber"
            archetype == 2 -> "Stone Stronghold $levelNumber"
            archetype == 3 -> "High Watch $levelNumber"
            else -> "Bastion Outpost $levelNumber"
        }

        val blocks = mutableListOf<BlockTemplate>()
        val enemies = mutableListOf<EnemyTemplate>()

        val baseX = 620f + (levelNumber % 3) * 20f

        when (archetype) {
            0 -> {
                blocks.add(BlockTemplate(baseX, 1040f, 40f, 90f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 70f, 1040f, 40f, 90f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX - 10f, 960f, 130f, 25f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 150f, 1040f, 40f, 90f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 220f, 1040f, 40f, 90f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 140f, 960f, 130f, 25f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 105f, 960f, 40f, 40f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 10f, 890f, 250f, 25f, BlockMaterial.STONE))
                if (isBoss) {
                    blocks.add(BlockTemplate(baseX + 110f, 830f, 50f, 50f, BlockMaterial.CRATE))
                }

                enemies.add(EnemyTemplate(baseX + 35f, 1040f))
                enemies.add(EnemyTemplate(baseX + 185f, 1040f))
                enemies.add(EnemyTemplate(baseX + 110f, 860f))
                if (isBoss) {
                    enemies.add(EnemyTemplate(baseX + 110f, 770f))
                }
            }
            1 -> {
                blocks.add(BlockTemplate(baseX - 20f, 1040f, 50f, 50f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 40f, 1040f, 50f, 50f, BlockMaterial.CRATE))
                blocks.add(BlockTemplate(baseX + 100f, 1040f, 50f, 50f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 160f, 1040f, 50f, 50f, BlockMaterial.CRATE))
                blocks.add(BlockTemplate(baseX + 220f, 1040f, 50f, 50f, BlockMaterial.STONE))

                blocks.add(BlockTemplate(baseX + 10f, 980f, 50f, 50f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 70f, 980f, 50f, 50f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 130f, 980f, 50f, 50f, BlockMaterial.CRATE))
                blocks.add(BlockTemplate(baseX + 190f, 980f, 50f, 50f, BlockMaterial.WOOD))

                blocks.add(BlockTemplate(baseX + 40f, 920f, 50f, 50f, BlockMaterial.CRATE))
                blocks.add(BlockTemplate(baseX + 100f, 920f, 50f, 50f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 160f, 920f, 50f, 50f, BlockMaterial.CRATE))

                blocks.add(BlockTemplate(baseX + 100f, 860f, 50f, 50f, BlockMaterial.WOOD))

                enemies.add(EnemyTemplate(baseX + 40f, 980f))
                enemies.add(EnemyTemplate(baseX + 160f, 980f))
                enemies.add(EnemyTemplate(baseX + 100f, 800f))
                if (isBoss || levelNumber > 25) {
                    enemies.add(EnemyTemplate(baseX + 100f, 1040f))
                }
            }
            2 -> {
                blocks.add(BlockTemplate(baseX, 1040f, 60f, 90f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 200f, 1040f, 60f, 90f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX - 10f, 960f, 280f, 30f, BlockMaterial.STONE))

                blocks.add(BlockTemplate(baseX + 40f, 900f, 40f, 80f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 160f, 900f, 40f, 80f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 100f, 915f, 45f, 45f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 20f, 830f, 220f, 25f, BlockMaterial.WOOD))

                enemies.add(EnemyTemplate(baseX + 100f, 1040f))
                enemies.add(EnemyTemplate(baseX + 50f, 830f))
                enemies.add(EnemyTemplate(baseX + 150f, 830f))
                if (isBoss) {
                    blocks.add(BlockTemplate(baseX + 100f, 770f, 50f, 50f, BlockMaterial.CRATE))
                    enemies.add(EnemyTemplate(baseX + 100f, 710f))
                }
            }
            3 -> {
                blocks.add(BlockTemplate(baseX + 80f, 1040f, 50f, 70f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 160f, 1040f, 50f, 70f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 60f, 980f, 170f, 25f, BlockMaterial.WOOD))

                blocks.add(BlockTemplate(baseX + 90f, 890f, 30f, 90f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 150f, 890f, 30f, 90f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 70f, 850f, 150f, 25f, BlockMaterial.WOOD))

                blocks.add(BlockTemplate(baseX + 100f, 770f, 30f, 80f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 140f, 770f, 30f, 80f, BlockMaterial.WOOD))
                blocks.add(BlockTemplate(baseX + 80f, 730f, 130f, 20f, BlockMaterial.WOOD))

                enemies.add(EnemyTemplate(baseX + 120f, 1040f))
                enemies.add(EnemyTemplate(baseX + 120f, 920f))
                enemies.add(EnemyTemplate(baseX + 120f, 690f))
                if (isBoss) {
                    enemies.add(EnemyTemplate(baseX + 40f, 1040f))
                }
            }
            else -> {
                blocks.add(BlockTemplate(baseX, 1040f, 50f, 80f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 70f, 1040f, 50f, 80f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX - 10f, 970f, 150f, 25f, BlockMaterial.WOOD))

                blocks.add(BlockTemplate(baseX + 160f, 1040f, 50f, 80f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 230f, 1040f, 50f, 80f, BlockMaterial.STONE))
                blocks.add(BlockTemplate(baseX + 150f, 970f, 150f, 25f, BlockMaterial.WOOD))

                blocks.add(BlockTemplate(baseX + 35f, 910f, 45f, 45f, BlockMaterial.TNT))
                blocks.add(BlockTemplate(baseX + 195f, 910f, 45f, 45f, BlockMaterial.CRATE))

                blocks.add(BlockTemplate(baseX + 60f, 890f, 170f, 25f, BlockMaterial.WOOD))

                enemies.add(EnemyTemplate(baseX + 35f, 1040f))
                enemies.add(EnemyTemplate(baseX + 195f, 1040f))
                enemies.add(EnemyTemplate(baseX + 125f, 850f))
                if (isBoss) {
                    enemies.add(EnemyTemplate(baseX + 125f, 1040f))
                }
            }
        }

        return LevelData(
            levelNumber = levelNumber,
            title = title,
            blocks = blocks,
            enemies = enemies,
            totalShots = totalShots,
            rewardCoins = rewardCoins,
            rewardGems = rewardGems
        )
    }
}
