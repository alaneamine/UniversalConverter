import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

//faire appel à l'API de conversion de monnaie

const val API_KEY = "312213d99f3bbb1308f38125"
const val BASE_URL = "https://v6.exchangerate-api.com/v6/$API_KEY/"

interface CurrencyApiService {
    @GET("latest/USD")  // Convertit les valeurs en USD par défaut
    suspend fun getExchangeRates(): ExchangeRatesResponse
}

object RetrofitInstance {
    val api: CurrencyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApiService::class.java)
    }
}

data class ExchangeRatesResponse(val conversion_rates: Map<String, Double>)