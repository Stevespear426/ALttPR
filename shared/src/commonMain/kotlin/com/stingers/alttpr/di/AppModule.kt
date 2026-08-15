package com.stingers.alttpr.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class])
@ComponentScan("com.stingers.alttpr")
class AppModule
