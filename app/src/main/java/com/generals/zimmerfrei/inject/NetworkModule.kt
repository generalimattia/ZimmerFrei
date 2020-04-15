package com.generals.zimmerfrei.inject

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.generals.network.adapter.LocalDateAdapter
import com.generals.network.api.CustomersAPI
import com.generals.network.api.ReservationsAPI
import com.generals.network.api.RoomsAPI
import com.generals.network.model.APIResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
            .add(LocalDateAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideOkHTTPClient(context: Context): OkHttpClient {
        val cacheSize: Long = 10 * 1024 * 1024
        return OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, cacheSize))
                .addInterceptor(ChuckerInterceptor(context))
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
            moshi: Moshi,
            okHttpClient: OkHttpClient
    ): Retrofit =
            Retrofit.Builder()
                    .baseUrl("http://192.168.1.18:8080/")
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(APIResponseAdapterFactory())
                    .client(okHttpClient).build()

    @Provides
    @Singleton
    fun provideRoomsAPI(retrofit: Retrofit): RoomsAPI = retrofit.create(RoomsAPI::class.java)

    @Provides
    @Singleton
    fun provideReservationsAPI(retrofit: Retrofit): ReservationsAPI = retrofit.create(ReservationsAPI::class.java)

    @Provides
    @Singleton
    fun provideCustomersAPI(retrofit: Retrofit): CustomersAPI = retrofit.create(CustomersAPI::class.java)
}