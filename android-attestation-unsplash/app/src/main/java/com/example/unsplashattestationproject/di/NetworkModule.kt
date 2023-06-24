package com.example.unsplashattestationproject.di

import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
    fun provideRetrofitService(moshi: Moshi): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
            .build()

        return Retrofit.Builder().baseUrl(BuildConfig.AUTH_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Named("api")
    @Provides
    fun provideRetrofitAuthService(
        moshi: Moshi,
        authInterceptor: AuthInterceptor
    ): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(makeLoggingInterceptor())
//            .addInterceptor(makeAuthInterceptor())
            .addInterceptor(authInterceptor) // ???
            .build()

        return Retrofit.Builder().baseUrl(BuildConfig.API_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Provides
    fun provideTokenProvider(): TokenProvider {
        return object : TokenProvider {
            override fun getToken(): String {

                // todo: add check for empty string, use shared prefs?
                return UnsplashRepository.unsplashAccessToken
            }
        }
    }

    // todo: add Provides
    private fun makeLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}

interface TokenProvider {
    fun getToken(): String
}

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${tokenProvider.getToken()}")
            .build()
        return chain.proceed(request)
    }
}