package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BoardLinkEntity
import com.example.data.model.CaseEntity
import com.example.data.model.CaseStatus
import com.example.data.model.EndingEntity
import com.example.data.model.EncounterEntity
import com.example.data.model.EvidenceEntity
import com.example.data.model.PlayerStatsEntity
import com.example.data.model.SuspectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestigationDao {

    // Cases
    @Query("SELECT * FROM cases ORDER BY id ASC")
    fun getAllCases(): Flow<List<CaseEntity>>

    @Query("SELECT * FROM cases WHERE chapter = :chapter ORDER BY id ASC")
    fun getCasesByChapter(chapter: Int): Flow<List<CaseEntity>>

    @Query("SELECT * FROM cases WHERE id = :caseId LIMIT 1")
    fun getCaseById(caseId: Int): Flow<CaseEntity?>

    @Query("SELECT * FROM cases WHERE id = :caseId LIMIT 1")
    suspend fun getCaseByIdDirect(caseId: Int): CaseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCases(cases: List<CaseEntity>)

    @Update
    suspend fun updateCase(caseEntity: CaseEntity)

    @Query("UPDATE cases SET status = :status, progressPercent = :progress WHERE id = :caseId")
    suspend fun updateCaseStatus(caseId: Int, status: CaseStatus, progress: Int)

    // Evidence
    @Query("SELECT * FROM evidence WHERE caseId = :caseId ORDER BY isDiscovered DESC, id ASC")
    fun getEvidenceForCase(caseId: Int): Flow<List<EvidenceEntity>>

    @Query("SELECT * FROM evidence WHERE isDiscovered = 1 ORDER BY caseId ASC")
    fun getAllDiscoveredEvidence(): Flow<List<EvidenceEntity>>

    @Query("SELECT * FROM evidence WHERE isSecretMirrorClue = 1")
    fun getSecretMirrorClues(): Flow<List<EvidenceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvidence(evidence: List<EvidenceEntity>)

    @Update
    suspend fun updateEvidence(evidence: EvidenceEntity)

    @Query("UPDATE evidence SET isDiscovered = 1 WHERE id = :evidenceId")
    suspend fun markEvidenceDiscovered(evidenceId: String)

    @Query("UPDATE evidence SET isConfirmedOnBoard = 1 WHERE id = :evidenceId")
    suspend fun markEvidenceConfirmed(evidenceId: String)

    // Suspects / NPCs
    @Query("SELECT * FROM suspects ORDER BY id ASC")
    fun getAllSuspects(): Flow<List<SuspectEntity>>

    @Query("SELECT * FROM suspects WHERE id = :id LIMIT 1")
    fun getSuspectById(id: String): Flow<SuspectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuspects(suspects: List<SuspectEntity>)

    @Update
    suspend fun updateSuspect(suspect: SuspectEntity)

    @Query("UPDATE suspects SET trustScore = :trust, fearScore = :fear, loyaltyScore = :loyalty WHERE id = :id")
    suspend fun updateSuspectMeters(id: String, trust: Int, fear: Int, loyalty: Int)

    // Player Stats
    @Query("SELECT * FROM player_stats WHERE id = 1 LIMIT 1")
    fun getPlayerStats(): Flow<PlayerStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayerStats(stats: PlayerStatsEntity)

    @Update
    suspend fun updatePlayerStats(stats: PlayerStatsEntity)

    // Board Links
    @Query("SELECT * FROM board_links ORDER BY timestamp DESC")
    fun getAllBoardLinks(): Flow<List<BoardLinkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoardLink(link: BoardLinkEntity)

    @Query("DELETE FROM board_links WHERE id = :id")
    suspend fun deleteBoardLink(id: Long)

    // Cairo Encounters
    @Query("SELECT * FROM encounters ORDER BY relatedCaseId ASC")
    fun getAllEncounters(): Flow<List<EncounterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEncounters(encounters: List<EncounterEntity>)

    @Query("UPDATE encounters SET isVisited = 1 WHERE id = :id")
    suspend fun markEncounterVisited(id: String)

    // Endings
    @Query("SELECT * FROM game_endings ORDER BY isSecretEnding ASC, id ASC")
    fun getAllEndings(): Flow<List<EndingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEndings(endings: List<EndingEntity>)

    @Query("UPDATE game_endings SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlockEnding(id: Int)

    // Reset & Wipe Tables for New Game
    @Query("DELETE FROM player_stats")
    suspend fun clearPlayerStats()

    @Query("DELETE FROM cases")
    suspend fun clearCases()

    @Query("DELETE FROM evidence")
    suspend fun clearEvidence()

    @Query("DELETE FROM suspects")
    suspend fun clearSuspects()

    @Query("DELETE FROM encounters")
    suspend fun clearEncounters()

    @Query("DELETE FROM board_links")
    suspend fun clearBoardLinks()

    @Query("DELETE FROM game_endings")
    suspend fun clearEndings()
}
