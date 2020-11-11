package com.timurpehlivan.practiceapp.db

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscribers = dao.getAllSubscribers()

    suspend fun insert(subscriber: Subscriber) {
        dao.insert(subscriber)
    }

    suspend fun update(subscriber: Subscriber) {
        dao.update(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int {
        return dao.delete(subscriber)
    }

    suspend fun clearAll(): Int {
        return dao.clearAll()
    }
}