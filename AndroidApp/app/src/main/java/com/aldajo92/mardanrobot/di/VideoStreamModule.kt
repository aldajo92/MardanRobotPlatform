package com.aldajo92.mardanrobot.di

import com.github.niqdev.mjpeg.Mjpeg
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class VideoStreamModule {

    @Provides
    fun providesVideoMjpeg(): Mjpeg = Mjpeg.newInstance()

}
