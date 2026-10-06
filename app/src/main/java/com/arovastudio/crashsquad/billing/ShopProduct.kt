package com.arovastudio.crashsquad.billing

data class ShopProduct(
    val id: String,
    val title: String,
    val gemsAmount: Int,
    val coinsAmount: Int = 0,
    val priceFormatted: String,
    val originalPriceFormatted: String? = null,
    val discountPercent: Int = 0,
    val isConsumable: Boolean = true,
    val description: String,
    val badge: String? = null
)

object ShopCatalog {
    val PRODUCTS = listOf(
        ShopProduct(
            id = "gems_100",
            title = "100 Gems",
            gemsAmount = 100,
            coinsAmount = 0,
            priceFormatted = "$1.99",
            isConsumable = true,
            description = "Pouch of shimmering tropical gems."
        ),
        ShopProduct(
            id = "gems_500",
            title = "500 Gems",
            gemsAmount = 500,
            coinsAmount = 250,
            priceFormatted = "$3.03",
            isConsumable = true,
            description = "Pile of radiant gems with 250 bonus coins!"
        ),
        ShopProduct(
            id = "gems_1200",
            title = "1,200 Gems",
            gemsAmount = 1200,
            coinsAmount = 1000,
            priceFormatted = "$6.99",
            isConsumable = true,
            description = "Treasure chest packed with gems and 1,000 coins!"
        ),
        ShopProduct(
            id = "hero_bundle",
            title = "Hero Bundle",
            gemsAmount = 300,
            coinsAmount = 2500,
            priceFormatted = "$4.99",
            isConsumable = true,
            description = "Unlocks Aqua & Rocky heroes plus 300 gems & 2,500 coins!"
        ),
        ShopProduct(
            id = "special_pack",
            title = "Special Pack",
            gemsAmount = 1000,
            coinsAmount = 10000,
            priceFormatted = "$9.99",
            isConsumable = true,
            description = "All heroes unlocked + 1,000 gems & 10,000 coins!"
        )
    )

    fun getProductById(id: String): ShopProduct? = PRODUCTS.find { it.id == id }
}
