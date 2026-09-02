package br.com.fiap.biowatch.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NominatimClient {

    private const val BASE_URL =
        "https://nominatim.openstreetmap.org/"

    val api: NominatimApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(NominatimApi::class.java)
    }
}