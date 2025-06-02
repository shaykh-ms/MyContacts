package com.example.mycontacts.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mycontacts.data.remote.ContactService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



    @Singleton
    @Provides
    fun provideChucker(@ApplicationContext appContext: Context) =
        ChuckerInterceptor.Builder(appContext)
            .collector(ChuckerCollector(appContext))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()

    @Provides
    @Singleton
    fun provideClient(
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {

                addInterceptor(chuckerInterceptor)
            callTimeout(1, TimeUnit.MINUTES)
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }.build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()
    @Singleton
    @Provides
    fun provideRetrofit(
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(ContactService.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideContactService(
        builder: Retrofit.Builder,
        okHttpClient: OkHttpClient
    ): ContactService {
        return builder.client(okHttpClient).build().create(ContactService::class.java)
    }


}