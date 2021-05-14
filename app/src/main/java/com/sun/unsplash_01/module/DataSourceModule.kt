package com.sun.unsplash_01.module

import com.sun.unsplash_01.data.source.PhotoDataSource
import com.sun.unsplash_01.data.source.remote.PhotoRemoteDataSource
import com.sun.unsplash_01.data.source.remote.SearchDataSource
import com.sun.unsplash_01.data.source.remote.SearchRemoteDataSource
import org.koin.dsl.bind
import org.koin.dsl.module

val dataSourceModule = module {

    single { PhotoRemoteDataSource(get()) } bind (PhotoDataSource.Remote::class)
    single { SearchRemoteDataSource(get()) } bind (SearchDataSource.Remote::class)
}
