package com.sy007.calendar.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sy007.calendar.R
import com.sy007.calendar.databinding.ActivityBaseBinding


/**
 * Created by sy007 on 5/3/22.
 */
open class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(LayoutInflater.from(this))
    }

    override fun setContentView(layoutResID: Int) {
        val view = LayoutInflater.from(this).inflate(layoutResID, binding.flContent, false)
        setContentView(view)
    }

    override fun setContentView(view: View) {
        val flContent = binding.flContent
        flContent.addView(view)
        super.setContentView(binding.root)
    }


    fun setTitle(title: String) {
        binding.toolbar.apply {
            setTitle(title)
            setSupportActionBar(this)
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener {
                finish()
            }
        }
    }
}


