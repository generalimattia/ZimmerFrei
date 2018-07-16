package com.generals.zimmerfrei.overview.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.overview.R

class OverviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container, OverviewFragment.newInstance()
            )
            .commitAllowingStateLoss()
    }
}
