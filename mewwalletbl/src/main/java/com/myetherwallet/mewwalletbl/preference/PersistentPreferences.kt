package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletbl.data.AppLanguage
import com.myetherwallet.mewwalletbl.data.AppTheme
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.util.ApplicationUtils
import java.util.*

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val PREFERENCES_NAME = "persistent"
private const val WHATS_NEW_DIALOG_VERSION = "whats_new_dialog_version"
private const val SHOULD_FAIL_SWAPS = "should_fail_swaps"
private const val DISABLE_SWAP_CHECK_BALANCE = "disable_swap_check_balance"
private const val DEBUG_BINANCE_BRIDGE = "debug_binance_bridge"
private const val API_TOTAL_COUNT_PREFIX = "api_total_count_"
private const val API_ERROR_COUNT_PREFIX = "api_error_count_"
private const val APP_LANGUAGE = "app_language"
private const val APP_THEME = "app_theme"
private const val APP_CURRENCY = "app_currency"
private const val IS_MANUAL_GAS_PRICE_ENABLED = "is_manual_gas_price_enabled"
private const val BLOCKCHAIN = "blockchain"
private const val IS_SURVEY_FINISHED = "is_survey_finished"
private const val SURVEY_DATE = "survey_date"
private const val WAS_YEARN_BADGE_SHOWN = "was_yearn_badge_shown"
private const val WAS_LIDO_BADGE_SHOWN = "was_lido_badge_shown"
private const val RATER_DATA = "rater_data"

class PersistentPreferences internal constructor(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun shouldShowWhatsNewDialog(versionCode: Int): Boolean {
        val current = preferences.getInt(WHATS_NEW_DIALOG_VERSION, 0)
        preferences.edit().putInt(WHATS_NEW_DIALOG_VERSION, versionCode).apply()
        return current > 0 && current != versionCode
    }

    fun updateTotalRequestCount(apiName: String) = incrementIntValue(API_TOTAL_COUNT_PREFIX + "_" + apiName)

    fun getTotalRequestCount(apiName: String) = preferences.getInt(API_TOTAL_COUNT_PREFIX + "_" + apiName, 0)

    fun updateErrorRequestCount(apiName: String) = incrementIntValue(API_ERROR_COUNT_PREFIX + "_" + apiName)

    fun getErrorRequestCount(apiName: String) = preferences.getInt(API_ERROR_COUNT_PREFIX + "_" + apiName, 0)

    fun deleteRequestCounts() {
        for ((key, _) in preferences.all) {
            if (key.startsWith(API_TOTAL_COUNT_PREFIX) || key.startsWith(API_ERROR_COUNT_PREFIX)) {
                preferences.edit()
                    .remove(key)
                    .apply()
            }
        }
    }

    fun setFailSwaps(isEnable: Boolean) = preferences.edit().putBoolean(SHOULD_FAIL_SWAPS, isEnable).apply()

    fun shouldFailSwaps() = preferences.getBoolean(SHOULD_FAIL_SWAPS, false)

    fun setSwapCheckBalanceDisabled(isEnable: Boolean) = preferences.edit().putBoolean(DISABLE_SWAP_CHECK_BALANCE, isEnable).apply()

    fun isSwapCheckBalanceDisabled() = preferences.getBoolean(DISABLE_SWAP_CHECK_BALANCE, false)

    fun setDebugBinanceBridgeEnabled(isEnable: Boolean) = preferences.edit().putBoolean(DEBUG_BINANCE_BRIDGE, isEnable).apply()

    fun isDebugBinanceBridgeEnabled() = preferences.getBoolean(DEBUG_BINANCE_BRIDGE, false)

    fun setManualGasPriceEnabled(isEnabled: Boolean) = preferences.edit().putBoolean(IS_MANUAL_GAS_PRICE_ENABLED, isEnabled).apply()

    fun isManualGasPriceEnabled() = preferences.getBoolean(IS_MANUAL_GAS_PRICE_ENABLED, false)

    private fun incrementIntValue(key: String) {
        val current = preferences.getInt(key, 0)
        preferences.edit().putInt(key, current + 1).apply()
    }

    fun setAppLanguage(appLanguage: AppLanguage) = preferences.edit().putString(APP_LANGUAGE, appLanguage.name).apply()

    fun getAppLanguage() = preferences.getString(APP_LANGUAGE, null)?.let { AppLanguage.valueOf(it) } ?: ApplicationUtils.getSystemLanguage()

    fun setAppTheme(appTheme: AppTheme) = preferences.edit().putString(APP_THEME, appTheme.name).apply()

    fun getAppTheme() = preferences.getString(APP_THEME, null)?.let { AppTheme.valueOf(it) } ?: AppTheme.DEFAULT

    fun setAppCurrency(currency: AppCurrency) = preferences.edit().putString(APP_CURRENCY, currency.name).apply()

    fun getAppCurrency() = preferences.getString(APP_CURRENCY, null)?.let { AppCurrency.valueOf(it) } ?: ApplicationUtils.getCurrency()

    fun setBlockchain(type: Blockchain) = preferences.edit().putString(BLOCKCHAIN, type.name).apply()

    fun getBlockchain() = preferences.getString(BLOCKCHAIN, null)?.let { Blockchain.valueOf(it) } ?: Blockchain.ETHEREUM

    fun setSurveyFinished() = preferences.edit().putBoolean(IS_SURVEY_FINISHED, true).apply()

    fun isSurveyFinished() = preferences.getBoolean(IS_SURVEY_FINISHED, false)

    fun saveSurveyDate() {
        preferences.edit().putInt(SURVEY_DATE, getDayOfYear()).apply()
    }

    fun isSurveyShownToday() = preferences.getInt(SURVEY_DATE, 0) == getDayOfYear()

    private fun getDayOfYear() = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    fun wasYearnBadgeShown() = preferences.getBoolean(WAS_YEARN_BADGE_SHOWN, false)

    fun setYearnBadgeShown() = preferences.edit().putBoolean(WAS_YEARN_BADGE_SHOWN, true).apply()

    fun wasLidoBadgeShown() = preferences.getBoolean(WAS_LIDO_BADGE_SHOWN, false)

    fun setLidoBadgeShown() = preferences.edit().putBoolean(WAS_LIDO_BADGE_SHOWN, true).apply()

    fun setRaterData(json: String) = preferences.edit().putString(RATER_DATA, json).apply()

    fun getRaterData() = preferences.getString(RATER_DATA, "{}")!!
}
