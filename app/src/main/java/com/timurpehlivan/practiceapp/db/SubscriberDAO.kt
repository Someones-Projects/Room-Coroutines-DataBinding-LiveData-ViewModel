package com.timurpehlivan.practiceapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscriber: Subscriber)

    @Update
    suspend fun update(subscriber: Subscriber)

    @Delete
    suspend fun delete(subscriber: Subscriber): Int

    @Query("DELETE FROM subscriber_table")
    suspend fun clearAll(): Int

    /*
    Asynchronous Query
    Room returns to get data from a database table as a LiveData of list of entities
    Room always run them on the background thread
    No need coroutines, no need suspend modifier!
    */
    @Query("SELECT * FROM subscriber_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}