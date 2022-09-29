package com.aldajo92.mardanrobot.di

import com.aldajo92.mardanrobot.framework.video_stream.VideoStream
import com.aldajo92.mardanrobot.framework.video_stream.VideoStreamImpl
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

    @Provides
    fun providesVideoStream(): VideoStream = VideoStreamImpl()

}
