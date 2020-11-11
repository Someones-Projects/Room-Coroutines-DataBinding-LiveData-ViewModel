package com.timurpehlivan.practiceapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.timurpehlivan.practiceapp.databinding.FragmentHomeBinding
import com.timurpehlivan.practiceapp.db.Subscriber
import com.timurpehlivan.practiceapp.db.SubscriberDatabase
import com.timurpehlivan.practiceapp.db.SubscriberRepository

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: SubscriberViewModel
    private lateinit var adapter: SubscriberAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dao = SubscriberDatabase.getInstance(requireActivity()).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding = DataBindingUtil.setContentView(requireActivity(), R.layout.fragment_home)
        binding.subscriberViewModel = viewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        viewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SubscriberAdapter({ selectedItem: Subscriber -> listItemClicked(selectedItem) })
        binding.subscriberRecyclerView.adapter = adapter
        displaySubscribersList()
    }

    private fun displaySubscribersList() {
        viewModel.subscribers.observe(this, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
        viewModel.initUpdateAndDelete(subscriber)
    }
}