package com.example.vaccination.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class VaccinationDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: VaccinationDatabase? = null

        @Synchronized
        fun getInstance(context: Context): VaccinationDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(context.applicationContext, VaccinationDatabase::class.java, "vaccination_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }

        /*private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: Database) {
            val noteDao = db.noteDao()
            subscribeOnBackground {
                noteDao.insert(Note("title 1", "desc 1", 1))
                noteDao.insert(Note("title 2", "desc 2", 2))
                noteDao.insert(Note("title 3", "desc 3", 3))

            }
        }*/

    }
}