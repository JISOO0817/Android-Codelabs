package com.jisoo.alarmcodelab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.jisoo.alarmcodelab.databinding.ActivityMainBinding
import com.jisoo.alarmcodelab.ui.EggTimerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpView()
        setUpFragment()
    }

    private fun setUpView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setUpFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, EggTimerFragment.newInstance())
            .commitNow()
    }
}