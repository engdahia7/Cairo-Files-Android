package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CaseStatus {
    LOCKED,
    UNLOCKED,
    IN_PROGRESS,
    RESOLVED,
    UNRESOLVED
}

enum class EvidenceCategory {
    PHYSICAL_OBJECT,
    DOCUMENT,
    PHOTO,
    DIGITAL_RECORD,
    FORENSIC_REPORT,
    AUDIO_TAPE
}

enum class EvidencePhase {
    OBSERVATION,
    SUSPICION,
    SEARCH,
    SECONDARY_CLUE,
    ANALYSIS,
    CONFIRMED
}

@Entity(tableName = "cases")
data class CaseEntity(
    @PrimaryKey val id: Int,
    val chapter: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val client: String,
    val location: String,
    val timeLabel: String,
    val coldOpenNarrative: String,
    val keyClueTitle: String,
    val mirrorClueSnippet: String,
    val status: CaseStatus = CaseStatus.LOCKED,
    val isKeyPlayableCase: Boolean = false,
    val progressPercent: Int = 0,
    val ratingStars: Int = 0,
    val outcomeSummary: String = "",
    val solvedWithJustice: Boolean = false
)

@Entity(tableName = "evidence")
data class EvidenceEntity(
    @PrimaryKey val id: String,
    val caseId: Int,
    val title: String,
    val description: String,
    val category: EvidenceCategory,
    val phase: EvidencePhase = EvidencePhase.OBSERVATION,
    val analysisInsight: String,
    val linkedPersonId: String? = null,
    val isSecretMirrorClue: Boolean = false,
    val isDiscovered: Boolean = false,
    val isConfirmedOnBoard: Boolean = false
)

@Entity(tableName = "suspects")
data class SuspectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val roleTitle: String,
    val description: String,
    val trustScore: Int = 50,     // 0 - 100
    val fearScore: Int = 20,      // 0 - 100
    val loyaltyScore: Int = 50,   // 0 - 100
    val secretsLevel: String = "متوسط",
    val statusTag: String = "محايد",
    val secretDossier: String,
    val avatarEmoji: String = "👤"
)

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Int = 1,
    val justiceScore: Int = 10,     // العدالة
    val empathyScore: Int = 10,     // التعاطف
    val cautionScore: Int = 10,     // الحذر
    val obsessionScore: Int = 10,   // الهوس
    val currentChapter: Int = 1,
    val activeCaseId: Int = 1,
    val totalCasesSolved: Int = 0,
    val mirrorNetworkKnowledge: Int = 12, // 0 - 100%
    val secretCluesFoundCount: Int = 1,
    val officeLevel: Int = 1 // 1: بدائي, 2: متطور, 3: مركز قيادة
)

@Entity(tableName = "board_links")
data class BoardLinkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceId: String,
    val targetId: String,
    val linkDescription: String,
    val isValidConnection: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "encounters")
data class EncounterEntity(
    @PrimaryKey val id: String,
    val district: String,
    val characterName: String,
    val characterRole: String,
    val dialogueText: String,
    val hiddenClueUnlocked: String,
    val relatedCaseId: Int,
    val isVisited: Boolean = false
)

@Entity(tableName = "game_endings")
data class EndingEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val subtitle: String,
    val description: String,
    val requirementText: String,
    val isUnlocked: Boolean = false,
    val isSecretEnding: Boolean = false
)
