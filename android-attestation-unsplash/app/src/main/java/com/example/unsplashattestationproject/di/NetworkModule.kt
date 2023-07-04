package com.example.unsplashattestationproject.di

import android.content.Context
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.data.PAGE_SIZE
import com.example.unsplashattestationproject.data.PREFETCH_DISTANCE
import com.example.unsplashattestationproject.data.PhotoRemoteMediator
import com.example.unsplashattestationproject.data.pagingsource.GetPhotosPagingSource
import com.example.unsplashattestationproject.data.UnsplashNetworkDataSource
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.utils.NetworkStateChecker
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

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
    fun provideRetrofitService(moshi: Moshi, loggingInterceptor: HttpLoggingInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
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
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): Retrofit {

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
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
                if (UnsplashRepository.unsplashAccessToken.isEmpty()) {
                    Log.e(TAG, "getToken: ${UnsplashRepository.unsplashAccessToken}")
                }
                // TODO: tight coupling with Repository, create TokenManager? refactor
                return UnsplashRepository.unsplashAccessToken
            }
        }
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


    @Singleton
    @Provides
    fun provideNetworkStateChecker(@ApplicationContext context: Context): NetworkStateChecker =
        NetworkStateChecker(context)

    @OptIn(ExperimentalPagingApi::class)
    @Named("mediatorPager")
    @Provides
    fun providePhotosPagerWithMediator(
        photoRemoteMediator: PhotoRemoteMediator,
        photoDatabase: PhotoDatabase
    ): Pager<Int, Photo> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            remoteMediator = photoRemoteMediator,
            pagingSourceFactory = { photoDatabase.photoDao().getPhotos() }
        )
    }

    @Named("getPhotosSimplePager")
    @Provides
    fun provideGetPhotosSimplePager(
        unsplashNetworkDataSource: UnsplashNetworkDataSource,
    ): Pager<Int, Photo> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = {
                GetPhotosPagingSource(unsplashNetworkDataSource = unsplashNetworkDataSource)
            }
        )
    }

}