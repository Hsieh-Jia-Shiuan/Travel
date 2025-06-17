package com.example.travel

import com.example.travel.model.dataSource.ApiService
import com.example.travel.model.dataSource.RemoteDataSource
import com.example.travel.model.repositories.FlightRepository
import com.example.travel.viewModel.FlightViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://www.kia.gov.tw/API/"

val viewModelModules = module {
    viewModel { FlightViewModel(get()) }
}

val repositoryModules = module {
    single { FlightRepository(get()) }
}

val dataSourceModules = module {
    single { RemoteDataSource(get()) }
}

val apiModules = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>())
            .build()
    }

    single {
        get<Retrofit>().create(ApiService::class.java)
    }
}