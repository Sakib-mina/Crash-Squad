package com.arovastudio.crashsquad.ui

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arovastudio.crashsquad.billing.BillingManager
import com.arovastudio.crashsquad.billing.ShopCatalog
import com.arovastudio.crashsquad.billing.ShopProduct
import com.arovastudio.crashsquad.data.AppDatabase
import com.arovastudio.crashsquad.data.DailyMissionEntity
import com.arovastudio.crashsquad.data.GameProgressEntity
import com.arovastudio.crashsquad.data.GameRepository
import com.arovastudio.crashsquad.data.HeroProgressEntity
import com.arovastudio.crashsquad.data.LevelRecordEntity
import com.arovastudio.crashsquad.game.GameEngine
import com.arovastudio.crashsquad.game.GameLevels
import com.arovastudio.crashsquad.game.GameState
import com.arovastudio.crashsquad.heroes.Hero
import com.arovastudio.crashsquad.heroes.HeroRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenState {
    SPLASH,
    HOME,
    LEVEL_SELECT,
    GAMEPLAY,
    HEROES,
    SHOP,
    SETTINGS
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = GameRepository(database.gameDao())

    val soundManager = SoundManager(application, viewModelScope)

    val gameProgress: StateFlow<GameProgressEntity?> = repository.gameProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val levelRecords: StateFlow<List<LevelRecordEntity>> = repository.levelRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val heroProgress: StateFlow<List<HeroProgressEntity>> = repository.heroProgress
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dailyMissions: StateFlow<List<DailyMissionEntity>> = repository.dailyMissions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(ScreenState.SPLASH)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _currentPlayingLevel = MutableStateFlow(1)
    val currentPlayingLevel: StateFlow<Int> = _currentPlayingLevel.asStateFlow()

    private val _activeGameEngine = MutableStateFlow<GameEngine?>(null)
    val activeGameEngine: StateFlow<GameEngine?> = _activeGameEngine.asStateFlow()

    val showResultDialog = MutableStateFlow(false)
    val showPauseDialog = MutableStateFlow(false)
    val showDailyRewardSheet = MutableStateFlow(false)
    val showMissionsSheet = MutableStateFlow(false)

    private val _purchaseSuccessProduct = MutableStateFlow<ShopProduct?>(null)
    val purchaseSuccessProduct: StateFlow<ShopProduct?> = _purchaseSuccessProduct.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    var lastWonStars: Int = 3
    var lastWonScore: Int = 0
    var lastCoinsReward: Int = 100
    var lastGemsReward: Int = 2

    val billingManager: BillingManager = BillingManager(
        context = application,
        scope = viewModelScope,
        onPurchaseGranted = { product ->
            handlePurchaseGranted(product)
        },
        onStatusMessage = { msg ->
            _statusMessage.value = msg
        }
    )

    init {
        viewModelScope.launch {
            repository.initializeDefaultsIfNeeded()
        }

        viewModelScope.launch {
            gameProgress.collect { progress ->
                if (progress != null) {
                    soundManager.soundEnabled = progress.soundEnabled
                    soundManager.musicEnabled = progress.musicEnabled
                    soundManager.vibrationEnabled = progress.vibrationEnabled
                }
            }
        }
    }

    fun navigateTo(screen: ScreenState) {
        soundManager.playSound("tap")
        _currentScreen.value = screen
    }

    fun startLevel(levelNumber: Int) {
        _currentPlayingLevel.value = levelNumber
        val levelData = GameLevels.getLevel(levelNumber)
        val selectedHero = getActiveHero()
        val heroLevel = getHeroLevel(selectedHero.id)
        val heroPower = selectedHero.calculatePower(heroLevel)

        val engine = GameEngine(
            levelData = levelData,
            selectedHero = selectedHero,
            heroPower = heroPower,
            onSoundEffect = { sound -> soundManager.playSound(sound) },
            onVibrate = { soundManager.vibrate() }
        )
        _activeGameEngine.value = engine
        showResultDialog.value = false
        showPauseDialog.value = false
        navigateTo(ScreenState.GAMEPLAY)
    }

    fun restartCurrentLevel() {
        startLevel(_currentPlayingLevel.value)
    }

    fun nextLevel() {
        val next = _currentPlayingLevel.value + 1
        if (next <= GameLevels.ALL_LEVELS.size) {
            startLevel(next)
        } else {
            navigateTo(ScreenState.LEVEL_SELECT)
        }
    }

    fun onLevelWon(engine: GameEngine) {
        val levelId = engine.levelData.levelNumber
        val stars = engine.calculateStars()
        val score = engine.score
        val coinsReward = engine.levelData.rewardCoins
        val gemsReward = engine.levelData.rewardGems

        lastWonStars = stars
        lastWonScore = score
        lastCoinsReward = coinsReward
        lastGemsReward = gemsReward

        viewModelScope.launch {
            repository.completeLevel(
                levelId = levelId,
                starsEarned = stars,
                scoreEarned = score,
                coinsReward = coinsReward,
                gemsReward = gemsReward,
                blocksDestroyedCount = engine.blocksDestroyedCount
            )
            showResultDialog.value = true
        }
    }

    fun onLevelLost(engine: GameEngine) {
        showResultDialog.value = true
    }

    fun getActiveHero(): Hero {
        val progress = gameProgress.value
        val heroId = progress?.selectedHeroId ?: "blaze"
        return HeroRegistry.getHeroById(heroId)
    }

    fun getHeroLevel(heroId: String): Int {
        val heroes = heroProgress.value
        return heroes.find { it.heroId == heroId }?.level ?: 1
    }

    fun isHeroUnlocked(heroId: String): Boolean {
        if (heroId == "blaze") return true
        val heroes = heroProgress.value
        return heroes.find { it.heroId == heroId }?.isUnlocked ?: false
    }

    fun upgradeHero(heroId: String) {
        val currentLevel = getHeroLevel(heroId)
        val hero = HeroRegistry.getHeroById(heroId)
        val cost = hero.upgradeCost(currentLevel)
        viewModelScope.launch {
            val success = repository.upgradeHero(heroId, cost)
            if (success) {
                soundManager.playSound("coin")
                soundManager.vibrate()
            } else {
                _statusMessage.value = "Not enough coins!"
            }
        }
    }

    fun unlockHero(heroId: String) {
        val hero = HeroRegistry.getHeroById(heroId)
        viewModelScope.launch {
            val success = repository.unlockHero(heroId, hero.unlockGemsCost)
            if (success) {
                soundManager.playSound("gem_pickup")
                soundManager.vibrate()
                repository.selectHero(heroId)
            } else {
                _statusMessage.value = "Not enough gems!"
            }
        }
    }

    fun selectHero(heroId: String) {
        viewModelScope.launch {
            repository.selectHero(heroId)
            soundManager.playSound("tap")
        }
    }

    fun purchaseProduct(activity: Activity, product: ShopProduct) {
        soundManager.playSound("tap")
        if (billingManager.isConnected.value) {
            billingManager.launchPurchaseFlow(activity, product)
        } else {
            billingManager.simulateTestPurchaseForDevelopment(product)
        }
    }

    private fun handlePurchaseGranted(product: ShopProduct) {
        viewModelScope.launch {
            repository.addCurrency(product.coinsAmount, product.gemsAmount)
            if (product.id == "hero_bundle") {
                repository.unlockHero("aqua", 0)
                repository.unlockHero("rocky", 0)
            } else if (product.id == "special_pack") {
                repository.unlockHero("aqua", 0)
                repository.unlockHero("rocky", 0)
                repository.unlockHero("leaf", 0)
            }
            soundManager.playSound("gem_pickup")
            soundManager.vibrate(60, 220)
            _purchaseSuccessProduct.value = product
        }
    }

    fun dismissPurchaseSuccess() {
        _purchaseSuccessProduct.value = null
    }

    fun dismissStatusMessage() {
        _statusMessage.value = null
    }

    fun claimMission(missionId: String) {
        viewModelScope.launch {
            val success = repository.claimMission(missionId)
            if (success) {
                soundManager.playSound("coin")
                soundManager.vibrate()
            }
        }
    }

    fun claimDailyReward(day: Int, coins: Int, gems: Int) {
        viewModelScope.launch {
            repository.claimDailyReward(day, coins, gems)
            soundManager.playSound("gem_pickup")
            soundManager.vibrate()
            showDailyRewardSheet.value = false
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings(sound = enabled)
        }
    }

    fun toggleMusic(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings(music = enabled)
        }
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateSettings(vibration = enabled)
        }
    }

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateSettings(language = lang)
        }
    }

    fun restorePurchases() {
        soundManager.playSound("tap")
        billingManager.restorePurchases()
    }

    fun redeemPromoCode(code: String): Boolean {
        val cleanCode = code.trim().uppercase()
        return when (cleanCode) {
            "WELCOME3", "SAVE3", "PLAY3", "DISCOUNT3" -> {
                viewModelScope.launch {
                    repository.addCurrency(coinsDelta = 1000, gemsDelta = 300)
                    soundManager.playSound("gem_pickup")
                    soundManager.vibrate()
                    _statusMessage.value = "🎁 Welcome Offer Applied! Claimed $3/£3 Bonus: 300 Gems + 1,000 Coins!"
                }
                true
            }
            "SQUAD50", "OFFER50" -> {
                viewModelScope.launch {
                    repository.addCurrency(coinsDelta = 2500, gemsDelta = 500)
                    soundManager.playSound("gem_pickup")
                    soundManager.vibrate()
                    _statusMessage.value = "🎉 50% Bonus Code Applied! Claimed 500 Gems + 2,500 Coins!"
                }
                true
            }
            else -> {
                _statusMessage.value = "Invalid or Expired Promo Code. Try WELCOME3 or SAVE3!"
                false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.destroy()
    }
}
