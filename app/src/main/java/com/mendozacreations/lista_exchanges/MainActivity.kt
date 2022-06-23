package com.mendozacreations.lista_exchanges


import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.unit.dp
import com.mendozacreations.lista_exchanges.data.remote.ExchangesApi
import com.mendozacreations.lista_exchanges.data.remote.dto.ExchangesDto
import com.mendozacreations.lista_exchanges.ui.theme.Lista_exchangesTheme
import com.mendozacreations.lista_exchanges.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.internal.connection.Exchange
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*val retrofit = Retrofit.Builder()
            .baseUrl("")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()*/
        setContent {
            Lista_exchangesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ExchangesScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangesScreen(viewModel: ExchangesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val ScaffoldState = rememberScaffoldState()

    Scaffold(
        topBar ={
            TopAppBar(title = { Text(text = "Exchanges ") })
        },
        scaffoldState = ScaffoldState
    ){
        Column(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.exchange) { exchange ->
                    ExchangeSItem(exchange = exchange, {})
                }
            }
            if (state.isLoading)
                CircularProgressIndicator()
        }
    }
}

@Composable
fun ExchangeSItem(
    exchange: ExchangesDto, onClick : (ExchangesDto) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick(exchange) }
        .padding(16.dp)
    ) {
        Text(
            "${exchange.name}",
            style = MaterialTheme.typography.h5,
            overflow = TextOverflow.Ellipsis)

        Text("${exchange.description}" ,
            style = MaterialTheme.typography.body2,)

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
            .padding(2.dp),
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Text(
                if(exchange.is_active) "Activa" else "Inactiva",
                color = if(exchange.is_active) Color.Green else Color.Red ,
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.body2,
            )
            Text ("${exchange.last_updated}")
        }
    }
}

class ExchangesRepository @Inject constructor(
    private val api: ExchangesApi
) {
    fun getExchanges(): Flow<Resource<List<ExchangesDto>>> = flow {
        try {
            emit(Resource.Loading()) //indicar que estamos cargando

            val coins = api.getExchanges() //descarga las monedas de internet, se supone quedemora algo

            emit(Resource.Success(coins)) //indicar que se cargo correctamente y pasarle las monedas
        } catch (e: HttpException) {
            //error general HTTP
            emit(Resource.Error(e.message ?: "Error HTTP GENERAL"))
        } catch (e: IOException) {
            //debe verificar tu conexion a internet
            emit(Resource.Error(e.message ?: "verificar tu conexion a internet"))
        }
    }
}



 data class ExchangeListState(
     val isLoading : Boolean = false,
     val exchange : List<ExchangesDto> = emptyList(),
     val error : String = ""
 )

