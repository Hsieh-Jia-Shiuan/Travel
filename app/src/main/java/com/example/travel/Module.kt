package com.example.travel

import com.example.travel.model.dataSource.currency.CurrencyApiService
import com.example.travel.model.dataSource.currency.CurrencyRemoteDataSource
import com.example.travel.model.dataSource.flight.FlightApiService
import com.example.travel.model.dataSource.flight.FlightRemoteDataSource
import com.example.travel.model.repositories.CurrencyRepository
import com.example.travel.model.repositories.FlightRepository
import com.example.travel.viewModel.FlightViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val FLIGHT_BASE_URL = "https://www.kia.gov.tw/API/"
private const val CURRENCY_BASE_URL = "https://api.freecurrencyapi.com/"
private val FREE_CURRENCY_API_KEY = BuildConfig.FREE_CURRENCY_API_KEY

val viewModelModules = module {
    viewModel { FlightViewModel(get()) }
}

val repositoryModules = module {
    single { FlightRepository(get()) }
    single { CurrencyRepository(get()) }
}

val dataSourceModules = module {
    single { FlightRemoteDataSource(get()) }
    single { CurrencyRemoteDataSource(FREE_CURRENCY_API_KEY, get()) }
}

val apiModules = module {
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single(named("sharedOkHttpClient")) {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single(named("flightRetrofit")) {
        Retrofit.Builder()
            .baseUrl(FLIGHT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>(named("sharedOkHttpClient")))
            .build()
    }

    single(named("currencyRetrofit")) {
        Retrofit.Builder()
            .baseUrl(CURRENCY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get<OkHttpClient>(named("sharedOkHttpClient")))
            .build()
    }

    single {
        get<Retrofit>(named("flightRetrofit")).create(FlightApiService::class.java)
    }

    single {
        get<Retrofit>(named("currencyRetrofit")).create(CurrencyApiService::class.java)
    }
}