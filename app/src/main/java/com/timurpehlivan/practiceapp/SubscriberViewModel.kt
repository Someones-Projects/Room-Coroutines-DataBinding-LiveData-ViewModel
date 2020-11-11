package com.timurpehlivan.practiceapp

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timurpehlivan.practiceapp.db.Subscriber
import com.timurpehlivan.practiceapp.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber
    private val statusMessage = MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>
        get() = statusMessage

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val buttonSaveOrUpdateText = MutableLiveData<String>()

    @Bindable
    val buttonClearAllOrDeleteText = MutableLiveData<String>()

    init {
        buttonSaveOrUpdateText.value = "Save"
        buttonClearAllOrDeleteText.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            statusMessage.value = Event("Please enter subscriber's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Please enter valid email")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) delete(subscriberToUpdateOrDelete)
        else clearAll()

    }

    private fun insert(subscriber: Subscriber) = viewModelScope.launch {
        repository.insert(subscriber)
        statusMessage.value = Event("Subscriber Inserted Successfully")
    }

    private fun update(subscriber: Subscriber) = viewModelScope.launch {
        repository.update(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
        buttonSaveOrUpdateText.value = "Save"
        buttonClearAllOrDeleteText.value = "Clear All"
        statusMessage.value = Event("Subscriber Updated Successfully")
    }

    private fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val numberOfRowsDeleted = repository.delete(subscriber)
        if (numberOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            buttonSaveOrUpdateText.value = "Save"
            buttonClearAllOrDeleteText.value = "Clear All"
            statusMessage.value = Event("$numberOfRowsDeleted Subscriber Deleted Successfully")
        } else {
            statusMessage.value = Event("Error Occurred")
        }
    }

    private fun clearAll() = viewModelScope.launch {
        val numberOfRowsDeleted = repository.clearAll()
        if (numberOfRowsDeleted > 0) statusMessage.value =
            Event("$numberOfRowsDeleted Subscriber Deleted Successfully")
        else statusMessage.value = Event("Error Occurred")
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        buttonSaveOrUpdateText.value = "Update"
        buttonClearAllOrDeleteText.value = "Delete"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}