package com.timurpehlivan.practiceapp

import android.R
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        setCustomAnimations(
            R.anim.fade_in,
            R.anim.fade_in,
            R.anim.fade_out,
            R.anim.fade_out
        ).add(frameId, fragment)
        addToBackStack(fragment.javaClass.simpleName)
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}