package com.tatar.repofinder.di.app.module

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.tatar.repofinder.BuildConfig
import com.tatar.repofinder.di.app.scope.PerApp
import com.tatar.repofinder.util.ConnectionManager
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

@Module(includes = [AppModule::class])
class NetworkModule {

    @PerApp
    @Provides
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @PerApp
    @Provides
    fun okHttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor { chain ->
                val original = chain.request()
                val builder = original.newBuilder().method(
                    original.method(),
                    original.body()
                )
                builder.addHeader(
                    "Authorization"
                    , "Bearer " + BuildConfig.GITHUB_AUTH_TOKEN
                )
                chain.proceed(builder.build())
            }
            .cache(cache)
            .build()
    }

    @PerApp
    @Provides
    fun cache(cacheFile: File): Cache {
        return Cache(cacheFile, 10 * 1000 * 1000) // 10 MB
    }

    @PerApp
    @Provides
    fun cacheFile(context: Context): File {
        return File(context.cacheDir, "okhttp_cache")
    }

    @PerApp
    @Provides
    fun connectionManager(context: Context): ConnectionManager {
        return ConnectionManager(context)
    }
}

