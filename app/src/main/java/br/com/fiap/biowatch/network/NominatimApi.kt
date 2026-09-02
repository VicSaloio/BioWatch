package br.com.fiap.biowatch.network

import java.util.Locale
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NominatimApi {

    @Headers(
        "User-Agent: BioWatch/1.0"
    )
    @GET("reverse")
    suspend fun reverseGeocode(
        @Query("format") format: String = "jsonv2",
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Header("Accept-Language")
        acceptLanguage: String =
            Locale.getDefault().toLanguageTag()
    ): ReverseGeocodingResponse

    @Headers(
        "User-Agent: BioWatch/1.0"
    )
    @GET("search")
    suspend fun searchPlace(
        @Query("q") query: String,
        @Query("format") format: String = "jsonv2",
        @Query("limit") limit: Int = 5,
        @Query("countrycodes") countryCodes: String = "br",
        @Header("Accept-Language")
        acceptLanguage: String =
            Locale.getDefault().toLanguageTag()
    ): List<NominatimSearchResult>
}

data class NominatimSearchResult(
    val display_name: String,
    val lat: String,
    val lon: String
)