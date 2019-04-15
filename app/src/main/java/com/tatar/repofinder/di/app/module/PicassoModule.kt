package com.tatar.repofinder.di.app.module

import android.content.Context
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.tatar.repofinder.di.app.scope.PerApp
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class])
class PicassoModule {

    @Module
    companion object {

        @JvmStatic
        @PerApp
        @Provides
        fun picassoPicasso(context: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
            return Picasso.Builder(context)
                .downloader(okHttp3Downloader)
                .build()
        }
    }
}