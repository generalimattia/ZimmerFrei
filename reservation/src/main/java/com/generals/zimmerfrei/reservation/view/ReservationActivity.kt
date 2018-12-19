package com.generals.zimmerfrei.reservation.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.generals.zimmerfrei.model.ParcelableRoomDay
import com.generals.zimmerfrei.reservation.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ReservationActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation)

        if (savedInstanceState == null) {

            val reservation: ParcelableRoomDay? = intent.getParcelableExtra(RESERVATION_START_DATE)

            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_container,
                    ReservationFragment.newInstance(reservation)
                )
                .commitAllowingStateLoss()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
