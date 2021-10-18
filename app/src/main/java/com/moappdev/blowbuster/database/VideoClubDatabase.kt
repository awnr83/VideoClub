package com.moappdev.blowbuster.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moappdev.blowbuster.database.dao.EjemplaresDatabaseDao
import com.moappdev.blowbuster.database.dao.SociosDatabaseDao
import com.moappdev.blowbuster.database.entidades.EjemplarDb
import com.moappdev.blowbuster.database.entidades.SocioDb

@Database(entities = [EjemplarDb::class, SocioDb::class], version=5, exportSchema = false)
abstract class VideoClubDatabase:RoomDatabase() {
    abstract val ejemplaresDataBaseDao: EjemplaresDatabaseDao
    abstract val sociosDataBaseDao: SociosDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: VideoClubDatabase? = null
        fun getInstance(context: Context): VideoClubDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        VideoClubDatabase::class.java,
                        "video_club"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}