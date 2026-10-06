package com.arovastudio.crashsquad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arovastudio.crashsquad.ui.GameViewModel
import com.arovastudio.crashsquad.ui.ScreenState
import com.arovastudio.crashsquad.ui.screens.DailyRewardDialog
import com.arovastudio.crashsquad.ui.screens.GameplayScreen
import com.arovastudio.crashsquad.ui.screens.HeroesScreen
import com.arovastudio.crashsquad.ui.screens.HomeScreen
import com.arovastudio.crashsquad.ui.screens.LevelResultDialog
import com.arovastudio.crashsquad.ui.screens.LevelSelectScreen
import com.arovastudio.crashsquad.ui.screens.MissionsDialog
import com.arovastudio.crashsquad.ui.screens.PurchaseSuccessDialog
import com.arovastudio.crashsquad.ui.screens.SettingsScreen
import com.arovastudio.crashsquad.ui.screens.ShopScreen
import com.arovastudio.crashsquad.ui.screens.SplashScreen
import com.arovastudio.crashsquad.ui.theme.CrashSquadTheme

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CrashSquadTheme {
                CrashSquadApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun CrashSquadApp(viewModel: GameViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val progress by viewModel.gameProgress.collectAsStateWithLifecycle()
    val levelRecords by viewModel.levelRecords.collectAsStateWithLifecycle()
    val heroProgress by viewModel.heroProgress.collectAsStateWithLifecycle()
    val dailyMissions by viewModel.dailyMissions.collectAsStateWithLifecycle()

    val activeEngine by viewModel.activeGameEngine.collectAsStateWithLifecycle()
    val showResultDialog by viewModel.showResultDialog.collectAsStateWithLifecycle()
    val showMissionsSheet by viewModel.showMissionsSheet.collectAsStateWithLifecycle()
    val showDailyRewardSheet by viewModel.showDailyRewardSheet.collectAsStateWithLifecycle()
    val purchaseSuccessProduct by viewModel.purchaseSuccessProduct.collectAsStateWithLifecycle()
    val statusMessage by viewModel.statusMessage.collectAsStateWithLifecycle()

    val isGooglePlayConnected by viewModel.billingManager.isConnected.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(statusMessage) {
        statusMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.dismissStatusMessage()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "screen_crossfade") { screen ->
                when (screen) {
                    ScreenState.SPLASH -> {
                        SplashScreen(
                            onSplashFinished = { viewModel.navigateTo(ScreenState.HOME) }
                        )
                    }

                    ScreenState.HOME -> {
                        HomeScreen(
                            progress = progress,
                            onPlayClick = { viewModel.navigateTo(ScreenState.LEVEL_SELECT) },
                            onHeroesClick = { viewModel.navigateTo(ScreenState.HEROES) },
                            onShopClick = { viewModel.navigateTo(ScreenState.SHOP) },
                            onSettingsClick = { viewModel.navigateTo(ScreenState.SETTINGS) },
                            onDailyRewardClick = { viewModel.showDailyRewardSheet.value = true },
                            onMissionsClick = { viewModel.showMissionsSheet.value = true }
                        )
                    }

                    ScreenState.LEVEL_SELECT -> {
                        LevelSelectScreen(
                            progress = progress,
                            levelRecords = levelRecords,
                            onBackClick = { viewModel.navigateTo(ScreenState.HOME) },
                            onSelectLevel = { lvl -> viewModel.startLevel(lvl) },
                            onShopClick = { viewModel.navigateTo(ScreenState.SHOP) }
                        )
                    }

                    ScreenState.GAMEPLAY -> {
                        activeEngine?.let { engine ->
                            GameplayScreen(
                                engine = engine,
                                soundEnabled = progress?.soundEnabled ?: true,
                                onLevelWon = { eng -> viewModel.onLevelWon(eng) },
                                onLevelLost = { eng -> viewModel.onLevelLost(eng) },
                                onHomeClick = { viewModel.navigateTo(ScreenState.HOME) },
                                onRestartClick = { viewModel.restartCurrentLevel() },
                                onToggleSound = { enabled -> viewModel.toggleSound(enabled) }
                            )
                        }
                    }

                    ScreenState.HEROES -> {
                        HeroesScreen(
                            progress = progress,
                            heroProgressList = heroProgress,
                            onBackClick = { viewModel.navigateTo(ScreenState.HOME) },
                            onUpgradeHero = { heroId -> viewModel.upgradeHero(heroId) },
                            onUnlockHero = { heroId -> viewModel.unlockHero(heroId) },
                            onSelectHero = { heroId -> viewModel.selectHero(heroId) },
                            onShopClick = { viewModel.navigateTo(ScreenState.SHOP) }
                        )
                    }

                    ScreenState.SHOP -> {
                        val productDetailsMap by viewModel.billingManager.productDetailsMap.collectAsStateWithLifecycle()
                        ShopScreen(
                            progress = progress,
                            isGooglePlayConnected = isGooglePlayConnected,
                            productDetailsMap = productDetailsMap,
                            onBackClick = { viewModel.navigateTo(ScreenState.HOME) },
                            onProductClick = { act, product -> viewModel.purchaseProduct(act, product) },
                            onRedeemPromoCode = { code -> viewModel.redeemPromoCode(code) }
                        )
                    }

                    ScreenState.SETTINGS -> {
                        SettingsScreen(
                            progress = progress,
                            onBackClick = { viewModel.navigateTo(ScreenState.HOME) },
                            onToggleSound = { viewModel.toggleSound(it) },
                            onToggleMusic = { viewModel.toggleMusic(it) },
                            onToggleVibration = { viewModel.toggleVibration(it) },
                            onChangeLanguage = { viewModel.changeLanguage(it) },
                            onRestorePurchases = { viewModel.restorePurchases() }
                        )
                    }
                }
            }

            // Level Result Dialog
            if (showResultDialog && activeEngine != null) {
                val engine = activeEngine!!
                LevelResultDialog(
                    isVictory = engine.gameState == com.arovastudio.crashsquad.game.GameState.WON,
                    levelNumber = engine.levelData.levelNumber,
                    stars = viewModel.lastWonStars,
                    score = viewModel.lastWonScore,
                    coinsReward = viewModel.lastCoinsReward,
                    gemsReward = viewModel.lastGemsReward,
                    onNextLevel = {
                        viewModel.showResultDialog.value = false
                        viewModel.nextLevel()
                    },
                    onRetry = {
                        viewModel.showResultDialog.value = false
                        viewModel.restartCurrentLevel()
                    },
                    onHome = {
                        viewModel.showResultDialog.value = false
                        viewModel.navigateTo(ScreenState.HOME)
                    }
                )
            }

            // Purchase Successful Dialog
            purchaseSuccessProduct?.let { product ->
                PurchaseSuccessDialog(
                    product = product,
                    onDismiss = { viewModel.dismissPurchaseSuccess() }
                )
            }

            // Daily Missions Dialog
            if (showMissionsSheet) {
                MissionsDialog(
                    missions = dailyMissions,
                    onClaimMission = { missionId -> viewModel.claimMission(missionId) },
                    onDismiss = { viewModel.showMissionsSheet.value = false }
                )
            }

            // 7-Day Island Reward Dialog
            if (showDailyRewardSheet) {
                DailyRewardDialog(
                    currentStreak = progress?.dailyRewardDayStreak ?: 1,
                    onClaimReward = { day, coins, gems ->
                        viewModel.claimDailyReward(day, coins, gems)
                    },
                    onDismiss = { viewModel.showDailyRewardSheet.value = false }
                )
            }
        }
    }
}
