package br.com.fiap.biowatch.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ObservationEntity::class],
    version = 3,
    exportSchema = false
)
abstract class BioWatchDatabase : RoomDatabase() {

    abstract fun observationDao(): ObservationDao

    companion object {

        @Volatile
        private var INSTANCE: BioWatchDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE observations ADD COLUMN latitude REAL"
                )

                db.execSQL(
                    "ALTER TABLE observations ADD COLUMN longitude REAL"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {

            override fun migrate(db: SupportSQLiteDatabase) {

                db.execSQL(
                    "ALTER TABLE observations ADD COLUMN scientificName TEXT"
                )
            }
        }

        fun getDatabase(context: Context): BioWatchDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BioWatchDatabase::class.java,
                    "biowatch_database"
                )
                    .addMigrations(
                        MIGRATION_1_2,
                        MIGRATION_2_3
                    )
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}