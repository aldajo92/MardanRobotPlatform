package com.aldajo92.mardanrobot.di

import com.aldajo92.mardanrobot.repositories.robot_message.RobotMessageRepository
import com.aldajo92.mardanrobot.repositories.robot_message.RobotMessageRepositoryImpl
import com.aldajo92.mardanrobot.repositories.video_stream.VideoRepository
import com.aldajo92.mardanrobot.repositories.video_stream.VideoRepositoryImpl
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

    @Provides
    fun providesRobotMessageRepository(mjpeg: Mjpeg): RobotMessageRepository = RobotMessageRepositoryImpl()

}
