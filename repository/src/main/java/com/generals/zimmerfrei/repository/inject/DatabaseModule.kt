package com.generals.zimmerfrei.repository.inject

import android.content.Context
import com.generals.zimmerfrei.repository.dao.ReservationDAO
import com.generals.zimmerfrei.repository.dao.ReservationDAOImpl
import com.generals.zimmerfrei.repository.database.ReservationDatabase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Binds
    @Singleton
    fun bindReservationDAO(context: Context): ReservationDAO = ReservationDAOImpl(
        ReservationDatabase.getDatabase(context).reservationDAO()
    )

}