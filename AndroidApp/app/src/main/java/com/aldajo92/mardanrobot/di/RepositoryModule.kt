package com.aldajo92.mardanrobot.di

import com.aldajo92.mardanrobot.data_sources.image_stream.VideoRepository
import com.aldajo92.mardanrobot.data_sources.image_stream.VideoRepositoryImpl
import com.github.niqdev.mjpeg.Mjpeg
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun providesVideoStreamRepository(mjpeg: Mjpeg): VideoRepository = VideoRepositoryImpl(mjpeg)

}
