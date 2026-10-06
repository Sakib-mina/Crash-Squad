package com.arovastudio.crashsquad.billing

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.arovastudio.crashsquad.billing.ShopCatalog
import com.arovastudio.crashsquad.billing.ShopProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillingManager(
    private val context: Context,
    private val scope: CoroutineScope,
    private val onPurchaseGranted: (ShopProduct) -> Unit,
    private val onStatusMessage: (String) -> Unit
) : PurchasesUpdatedListener {
    private val TAG = "CrashSquadBilling"
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _productDetailsMap = MutableStateFlow<Map<String, ProductDetails>>(emptyMap())
    val productDetailsMap: StateFlow<Map<String, ProductDetails>> = _productDetailsMap.asStateFlow()

    private var billingClient: BillingClient? = null

    init {
        initializeBillingClient()
    }

    private fun initializeBillingClient() {
        try {
            val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()

            billingClient = BillingClient.newBuilder(context)
                .setListener(this)
                .enablePendingPurchases(pendingPurchasesParams)
                .build()

            startBillingConnection()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize BillingClient: ${e.message}")
        }
    }

    fun startBillingConnection() {
        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Google Play Billing setup successful")
                    _isConnected.value = true
                    queryAvailableProducts()
                } else {
                    Log.w(TAG, "Billing setup finished with code: ${billingResult.responseCode} - ${billingResult.debugMessage}")
                    _isConnected.value = false
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Billing service disconnected, will retry on next operation")
                _isConnected.value = false
            }
        })
    }

    fun queryAvailableProducts() {
        val client = billingClient ?: return
        if (!_isConnected.value) return

        val productList = ShopCatalog.PRODUCTS.map { product ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(product.id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        client.queryProductDetailsAsync(params) { billingResult, productDetailsResult ->
            val queryProductDetailsList = productDetailsResult.productDetailsList
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val detailsMap = queryProductDetailsList.associateBy { it.productId }
                _productDetailsMap.value = detailsMap
                Log.d(TAG, "Successfully loaded ${detailsMap.size} Play Store products")
            } else {
                Log.w(TAG, "queryProductDetailsAsync error: ${billingResult.debugMessage}")
            }
        }
    }

    fun launchPurchaseFlow(activity: Activity, product: ShopProduct) {
        val client = billingClient

        if (client == null || !_isConnected.value) {
            Log.i(TAG, "Play Store not connected. Informing user.")
            onStatusMessage("Google Play Store is currently connecting or unavailable on this device.")
            return
        }

        val productDetails = _productDetailsMap.value[product.id]
        if (productDetails == null) {
            Log.w(TAG, "ProductDetails not found for id ${product.id}. Querying again.")
            queryAvailableProducts()
            onStatusMessage("Loading product from Google Play... Please try again in a moment.")
            return
        }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val responseCode = client.launchBillingFlow(activity, billingFlowParams).responseCode
        if (responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "Failed to launch billing flow: code $responseCode")
            onStatusMessage("Purchase could not be completed.")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User cancelled purchase")
                onStatusMessage("Purchase cancelled.")
            }
            else -> {
                Log.e(TAG, "Purchase failed: code ${billingResult.responseCode}, ${billingResult.debugMessage}")
                onStatusMessage("Purchase could not be completed.")
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            for (productId in purchase.products) {
                val product = ShopCatalog.getProductById(productId)
                if (product != null) {
                    consumePurchase(purchase, product)
                }
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            onStatusMessage("Purchase pending completion.")
        }
    }

    private fun consumePurchase(purchase: Purchase, product: ShopProduct) {
        val client = billingClient ?: return
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        client.consumeAsync(consumeParams) { billingResult, _ ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Successfully consumed purchase for ${product.id}")
                scope.launch(Dispatchers.Main) {
                    onPurchaseGranted(product)
                }
            } else {
                Log.e(TAG, "Failed to consume purchase: ${billingResult.debugMessage}")
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase, product: ShopProduct) {
        val client = billingClient ?: return
        if (!purchase.isAcknowledged) {
            val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            client.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Successfully acknowledged purchase for ${product.id}")
                    scope.launch(Dispatchers.Main) {
                        onPurchaseGranted(product)
                    }
                } else {
                    Log.e(TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                }
            }
        } else {
            scope.launch(Dispatchers.Main) {
                onPurchaseGranted(product)
            }
        }
    }

    fun restorePurchases() {
        val client = billingClient
        if (client == null || !_isConnected.value) {
            onStatusMessage("Google Play Store is not connected.")
            return
        }

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        client.queryPurchasesAsync(params) { billingResult, purchasesList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                if (purchasesList.isEmpty()) {
                    onStatusMessage("No previous purchases found to restore.")
                } else {
                    var restoredCount = 0
                    for (purchase in purchasesList) {
                        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                            for (productId in purchase.products) {
                                val product = ShopCatalog.getProductById(productId)
                                if (product != null) {
                                    restoredCount++
                                    onPurchaseGranted(product)
                                }
                            }
                        }
                    }
                    onStatusMessage("Restored $restoredCount purchase(s) successfully.")
                }
            } else {
                onStatusMessage("Could not restore purchases: ${billingResult.debugMessage}")
            }
        }
    }

    fun simulateTestPurchaseForDevelopment(product: ShopProduct) {
        Log.i(TAG, "[DEV_TEST] Simulating test purchase for: ${product.id}")
        onPurchaseGranted(product)
    }

    fun destroy() {
        billingClient?.endConnection()
        billingClient = null
    }
}
