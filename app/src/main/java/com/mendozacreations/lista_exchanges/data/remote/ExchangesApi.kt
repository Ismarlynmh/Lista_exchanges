package com.mendozacreations.lista_exchanges.data.remote

import com.mendozacreations.lista_exchanges.data.remote.dto.ExchangesDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangesApi {
    @GET("/v1/exchanges")
    suspend fun getExchanges(): List<ExchangesDto>

    @GET("/v1/exchanges/{exchangeId}")
    suspend fun getExchanges( @Path ("exchangeId") exchangeId : String): ExchangesDto
}