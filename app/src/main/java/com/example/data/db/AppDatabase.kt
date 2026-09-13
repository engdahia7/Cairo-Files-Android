package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BoardLinkEntity
import com.example.data.model.CaseEntity
import com.example.data.model.EndingEntity
import com.example.data.model.EncounterEntity
import com.example.data.model.EvidenceEntity
import com.example.data.model.PlayerStatsEntity
import com.example.data.model.SuspectEntity

@Database(
    entities = [
        CaseEntity::class,
        EvidenceEntity::class,
        SuspectEntity::class,
        PlayerStatsEntity::class,
        BoardLinkEntity::class,
        EncounterEntity::class,
        EndingEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun investigationDao(): InvestigationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cairo_files_database.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
