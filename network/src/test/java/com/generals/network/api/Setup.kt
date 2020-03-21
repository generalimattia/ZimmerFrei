package com.generals.network.api

import com.generals.network.adapter.LocalDateAdapter
import com.generals.network.model.APIResponseAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi: Moshi = Moshi.Builder()
        .add(LocalDateAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.18:8080/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(APIResponseAdapterFactory())
        .client(okHttpClient)
        .build()