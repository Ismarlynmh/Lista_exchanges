package com.mendozacreations.lista_exchanges.di


import com.mendozacreations.lista_exchanges.ExchangesRepository
import com.mendozacreations.lista_exchanges.data.remote.ExchangesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


    @Singleton
    @Provides
    fun provideCoinApi(moshi: Moshi): ExchangesApi {
        return Retrofit.Builder()
            .baseUrl("https://api.coinpaprika.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ExchangesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinRepository(ExchangeApi: ExchangesApi) : ExchangesRepository{
        return ExchangesRepository((ExchangeApi))
    }
}

