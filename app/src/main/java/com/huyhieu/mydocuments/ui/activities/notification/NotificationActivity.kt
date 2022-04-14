package com.huyhieu.mydocuments.ui.activities.notification

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.huyhieu.mydocuments.R
import com.huyhieu.mydocuments.base.BaseActivity
import com.huyhieu.mydocuments.databinding.ActivityNotificationBinding
import com.huyhieu.mydocuments.utils.logDebug

class NotificationActivity : BaseActivity<ActivityNotificationBinding>() {
    override fun initializeBinding() = ActivityNotificationBinding.inflate(layoutInflater)

    override fun addControls(savedInstanceState1: Bundle?) {
        val a = System.currentTimeMillis()
        for (i in 1..200) {
            if (i % 2 == 0) {
                addFragment(Blank1Fragment.newInstance(i.toString()))
            } else {
                addFragment(Blank2Fragment.newInstance(i.toString()))
            }
        }
        logDebug("Done: ${System.currentTimeMillis() - a}")
    }

    private fun addFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.container, fragment)
        //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //ft.addToBackStack(fragment.javaClass.name)
        ft.commit()
    }

    override fun addEvents(savedInstanceState1: Bundle?) {
    }

    override fun onClick(p0: View?) {
    }
}