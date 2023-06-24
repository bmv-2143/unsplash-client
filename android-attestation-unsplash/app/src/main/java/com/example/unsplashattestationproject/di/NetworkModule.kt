package com.example.unsplashattestationproject.di

import com.example.unsplashattestationproject.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Named("auth")
    @Provides
    fun provideRetrofitAuthService(moshi: Moshi): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .addInterceptor(makeAuthInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(BuildConfig.AUTH_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun makeAuthInterceptor() = { chain: Interceptor.Chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Client-ID YOUR_ACCESS_TOKEN")
            .build()
        chain.proceed(request)
    }

    @Named("api")
    @Provides
    fun provideRetrofitService(moshi: Moshi): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(BuildConfig.API_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private fun makeLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}