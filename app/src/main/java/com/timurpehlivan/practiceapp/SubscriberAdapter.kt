package com.timurpehlivan.practiceapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.timurpehlivan.practiceapp.databinding.ListItemBinding
import com.timurpehlivan.practiceapp.db.Subscriber

class SubscriberAdapter(private val clickedListener: (Subscriber)-> Unit) : RecyclerView.Adapter<SubscriberAdapter.ViewHolder>() {
    private val subscribersList = ArrayList<Subscriber>()

    inner class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber,  clickedListener: (Subscriber)-> Unit) {
            binding.itemNameTextView.text = subscriber.name
            binding.itemEmailTextView.text = subscriber.email
            binding.listItemCardView.setOnClickListener {
                clickedListener(subscriber)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = subscribersList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subscribersList[position], clickedListener)
    }

    fun setList(subscribers: List<Subscriber>) {
        subscribersList.clear()
        subscribersList.addAll(subscribers)
    }
}