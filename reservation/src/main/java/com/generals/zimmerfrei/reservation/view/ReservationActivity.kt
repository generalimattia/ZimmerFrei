package com.generals.zimmerfrei.reservation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.generals.zimmerfrei.model.ParcelableDay
import com.generals.zimmerfrei.model.Room
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

            val selectedDay: ParcelableDay? = intent.getParcelableExtra(SELECTED_DAY_KEY)
            val selectedRoom: Room? = intent.getParcelableExtra(SELECTED_ROOM_KEY)
            val reservationURL: String? = intent.getStringExtra(RESERVATION_URL_KEY)

            supportFragmentManager.beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            ReservationFragment.newInstance(
                                    selectedDay = selectedDay,
                                    selectedRoom = selectedRoom,
                                    reservationURL = reservationURL)
                    )
                    .commitAllowingStateLoss()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
