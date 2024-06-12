package com.huyhieu.data.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class GitHubAPI

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MapAPI

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class PokeAPI

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class ReqResAPI
