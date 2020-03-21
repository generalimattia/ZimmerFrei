package com.generals.zimmerfrei.inject

import com.generals.network.adapter.LocalDateAdapter
import com.generals.network.model.APIResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
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
    fun provideOkHTTPClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
            .build()

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
}