package com.timurpehlivan.practiceapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase : RoomDatabase() {

    abstract val subscriberDAO: SubscriberDAO

    /*
    Singleton
    We should not let multiple instances of database opening in the same time
    */
    companion object {
        @Volatile // Thus, the writes to this field are immediately made visible to other threads.
        private var INSTANCE: SubscriberDatabase? = null

        fun getInstance(context: Context): SubscriberDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        SubscriberDatabase::class.java,
                        "subscriber_database"
                    ).build()
                }
                return instance
            }
        }
    }
}