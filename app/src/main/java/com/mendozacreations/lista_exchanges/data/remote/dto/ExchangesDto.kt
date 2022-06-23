package com.mendozacreations.lista_exchanges.data.remote.dto

data class ExchangesDto(
    val name : String = "",
    val description : String = "",
    val is_active : Boolean = false,
    val last_updated : String = "",
)

