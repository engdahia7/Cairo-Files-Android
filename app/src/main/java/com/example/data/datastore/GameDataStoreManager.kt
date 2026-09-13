package com.example.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.gameDataStore: DataStore<Preferences> by preferencesDataStore(name = "cairo_noir_game_session")

data class SavedGameSession(
    val selectedTabName: String = "OFFICE",
    val selectedChapter: Int = 1,
    val activeCaseId: Int = 1,
    val timeOfDayName: String = "MIDNIGHT",
    val weatherConditionName: String = "FOGGY_RAIN",
    val decryptedWiretapIds: Set<String> = emptySet(),
    val selectedWiretapId: String? = null,
    val activeTuningFrequency: Float = 100.0f,
    val completedColdCaseIds: Set<String> = emptySet(),
    val selectedColdCaseId: String? = null,
    val playerEnergyPoints: Int = 100,
    val maxEnergyPoints: Int = 100,
    val isAutoSaveEnabled: Boolean = true,
    val soundEffectsEnabled: Boolean = true,
    val atmosphericMusicEnabled: Boolean = true,
    val vibrationHapticsEnabled: Boolean = true,
    val textSpeed: String = "عادي",
    val lastSaveTimestamp: String = "محفوظ تلقائياً في DataStore",
    val lastSavedEpoch: Long = 0L
)

class GameDataStoreManager(private val context: Context) {

    private object PreferencesKeys {
        val SELECTED_TAB = stringPreferencesKey("selected_tab")
        val SELECTED_CHAPTER = intPreferencesKey("selected_chapter")
        val ACTIVE_CASE_ID = intPreferencesKey("active_case_id")
        val TIME_OF_DAY = stringPreferencesKey("time_of_day")
        val WEATHER_CONDITION = stringPreferencesKey("weather_condition")
        val DECRYPTED_WIRETAP_IDS = stringSetPreferencesKey("decrypted_wiretap_ids")
        val SELECTED_WIRETAP_ID = stringPreferencesKey("selected_wiretap_id")
        val ACTIVE_TUNING_FREQ = floatPreferencesKey("active_tuning_freq")
        val COMPLETED_COLD_CASE_IDS = stringSetPreferencesKey("completed_cold_case_ids")
        val SELECTED_COLD_CASE_ID = stringPreferencesKey("selected_cold_case_id")
        val PLAYER_ENERGY_POINTS = intPreferencesKey("player_energy_points")
        val MAX_ENERGY_POINTS = intPreferencesKey("max_energy_points")
        val IS_AUTO_SAVE_ENABLED = booleanPreferencesKey("is_auto_save_enabled")
        val SOUND_EFFECTS_ENABLED = booleanPreferencesKey("sound_effects_enabled")
        val ATMOSPHERIC_MUSIC_ENABLED = booleanPreferencesKey("atmospheric_music_enabled")
        val VIBRATION_HAPTICS_ENABLED = booleanPreferencesKey("vibration_haptics_enabled")
        val TEXT_SPEED = stringPreferencesKey("text_speed")
        val LAST_SAVE_TIMESTAMP = stringPreferencesKey("last_save_timestamp")
        val LAST_SAVED_EPOCH = longPreferencesKey("last_saved_epoch")
    }

    val sessionFlow: Flow<SavedGameSession> = context.gameDataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            SavedGameSession(
                selectedTabName = preferences[PreferencesKeys.SELECTED_TAB] ?: "OFFICE",
                selectedChapter = preferences[PreferencesKeys.SELECTED_CHAPTER] ?: 1,
                activeCaseId = preferences[PreferencesKeys.ACTIVE_CASE_ID] ?: 1,
                timeOfDayName = preferences[PreferencesKeys.TIME_OF_DAY] ?: "MIDNIGHT",
                weatherConditionName = preferences[PreferencesKeys.WEATHER_CONDITION] ?: "FOGGY_RAIN",
                decryptedWiretapIds = preferences[PreferencesKeys.DECRYPTED_WIRETAP_IDS] ?: emptySet(),
                selectedWiretapId = preferences[PreferencesKeys.SELECTED_WIRETAP_ID],
                activeTuningFrequency = preferences[PreferencesKeys.ACTIVE_TUNING_FREQ] ?: 100.0f,
                completedColdCaseIds = preferences[PreferencesKeys.COMPLETED_COLD_CASE_IDS] ?: emptySet(),
                selectedColdCaseId = preferences[PreferencesKeys.SELECTED_COLD_CASE_ID],
                playerEnergyPoints = preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] ?: 100,
                maxEnergyPoints = preferences[PreferencesKeys.MAX_ENERGY_POINTS] ?: 100,
                isAutoSaveEnabled = preferences[PreferencesKeys.IS_AUTO_SAVE_ENABLED] ?: true,
                soundEffectsEnabled = preferences[PreferencesKeys.SOUND_EFFECTS_ENABLED] ?: true,
                atmosphericMusicEnabled = preferences[PreferencesKeys.ATMOSPHERIC_MUSIC_ENABLED] ?: true,
                vibrationHapticsEnabled = preferences[PreferencesKeys.VIBRATION_HAPTICS_ENABLED] ?: true,
                textSpeed = preferences[PreferencesKeys.TEXT_SPEED] ?: "عادي",
                lastSaveTimestamp = preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] ?: "محفوظ تلقائياً في DataStore",
                lastSavedEpoch = preferences[PreferencesKeys.LAST_SAVED_EPOCH] ?: 0L
            )
        }

    val energyFlow: Flow<Int> = context.gameDataStore.data
        .catch { exception ->
            if (exception is IOException) emit(emptyPreferences()) else throw exception
        }
        .map { preferences ->
            preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] ?: 100
        }

    suspend fun saveEnergyPoints(energy: Int, maxEnergy: Int = 100) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] = energy.coerceIn(0, maxEnergy)
            preferences[PreferencesKeys.MAX_ENERGY_POINTS] = maxEnergy
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = "طاقة المحقق: $energy/$maxEnergy ⚡"
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun consumeEnergyPoints(amount: Int): Boolean {
        var success = false
        context.gameDataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] ?: 100
            val max = preferences[PreferencesKeys.MAX_ENERGY_POINTS] ?: 100
            if (current >= amount) {
                val updated = (current - amount).coerceAtLeast(0)
                preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] = updated
                preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = "استهلاك طاقة (-$amount): $updated/$max ⚡"
                preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
                success = true
            } else {
                success = false
            }
        }
        return success
    }

    suspend fun restoreEnergyPoints(amount: Int, maxEnergy: Int = 100): Int {
        var newEnergy = 100
        context.gameDataStore.edit { preferences ->
            val current = preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] ?: 100
            val max = preferences[PreferencesKeys.MAX_ENERGY_POINTS] ?: maxEnergy
            newEnergy = (current + amount).coerceAtMost(max)
            preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] = newEnergy
            preferences[PreferencesKeys.MAX_ENERGY_POINTS] = max
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = "استعادة طاقة (+$amount): $newEnergy/$max ⚡"
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
        return newEnergy
    }

    suspend fun saveFullSession(session: SavedGameSession) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_TAB] = session.selectedTabName
            preferences[PreferencesKeys.SELECTED_CHAPTER] = session.selectedChapter
            preferences[PreferencesKeys.ACTIVE_CASE_ID] = session.activeCaseId
            preferences[PreferencesKeys.TIME_OF_DAY] = session.timeOfDayName
            preferences[PreferencesKeys.WEATHER_CONDITION] = session.weatherConditionName
            preferences[PreferencesKeys.DECRYPTED_WIRETAP_IDS] = session.decryptedWiretapIds
            session.selectedWiretapId?.let {
                preferences[PreferencesKeys.SELECTED_WIRETAP_ID] = it
            } ?: preferences.remove(PreferencesKeys.SELECTED_WIRETAP_ID)
            preferences[PreferencesKeys.ACTIVE_TUNING_FREQ] = session.activeTuningFrequency
            preferences[PreferencesKeys.COMPLETED_COLD_CASE_IDS] = session.completedColdCaseIds
            session.selectedColdCaseId?.let {
                preferences[PreferencesKeys.SELECTED_COLD_CASE_ID] = it
            } ?: preferences.remove(PreferencesKeys.SELECTED_COLD_CASE_ID)
            preferences[PreferencesKeys.PLAYER_ENERGY_POINTS] = session.playerEnergyPoints
            preferences[PreferencesKeys.MAX_ENERGY_POINTS] = session.maxEnergyPoints
            preferences[PreferencesKeys.IS_AUTO_SAVE_ENABLED] = session.isAutoSaveEnabled
            preferences[PreferencesKeys.SOUND_EFFECTS_ENABLED] = session.soundEffectsEnabled
            preferences[PreferencesKeys.ATMOSPHERIC_MUSIC_ENABLED] = session.atmosphericMusicEnabled
            preferences[PreferencesKeys.VIBRATION_HAPTICS_ENABLED] = session.vibrationHapticsEnabled
            preferences[PreferencesKeys.TEXT_SPEED] = session.textSpeed
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = session.lastSaveTimestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun updateNavigationState(tabName: String, chapter: Int, caseId: Int, autoSaveTimestamp: String) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_TAB] = tabName
            preferences[PreferencesKeys.SELECTED_CHAPTER] = chapter
            preferences[PreferencesKeys.ACTIVE_CASE_ID] = caseId
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = autoSaveTimestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun updateEnvironmentState(timeOfDayName: String, weatherName: String, autoSaveTimestamp: String) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.TIME_OF_DAY] = timeOfDayName
            preferences[PreferencesKeys.WEATHER_CONDITION] = weatherName
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = autoSaveTimestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun updateWiretapsState(decryptedIds: Set<String>, selectedId: String?, freq: Float, autoSaveTimestamp: String) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.DECRYPTED_WIRETAP_IDS] = decryptedIds
            selectedId?.let { preferences[PreferencesKeys.SELECTED_WIRETAP_ID] = it }
            preferences[PreferencesKeys.ACTIVE_TUNING_FREQ] = freq
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = autoSaveTimestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun updateColdCasesState(completedIds: Set<String>, selectedId: String?, autoSaveTimestamp: String) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.COMPLETED_COLD_CASE_IDS] = completedIds
            selectedId?.let { preferences[PreferencesKeys.SELECTED_COLD_CASE_ID] = it }
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = autoSaveTimestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun updateSettings(
        autoSave: Boolean,
        sound: Boolean,
        music: Boolean,
        vibration: Boolean,
        speed: String,
        timestamp: String
    ) {
        context.gameDataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_AUTO_SAVE_ENABLED] = autoSave
            preferences[PreferencesKeys.SOUND_EFFECTS_ENABLED] = sound
            preferences[PreferencesKeys.ATMOSPHERIC_MUSIC_ENABLED] = music
            preferences[PreferencesKeys.VIBRATION_HAPTICS_ENABLED] = vibration
            preferences[PreferencesKeys.TEXT_SPEED] = speed
            preferences[PreferencesKeys.LAST_SAVE_TIMESTAMP] = timestamp
            preferences[PreferencesKeys.LAST_SAVED_EPOCH] = System.currentTimeMillis()
        }
    }

    suspend fun clearSession() {
        context.gameDataStore.edit { it.clear() }
    }
}
