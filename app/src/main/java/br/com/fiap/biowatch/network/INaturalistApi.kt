package br.com.fiap.biowatch.network

import java.util.Locale
import retrofit2.http.GET
import retrofit2.http.Query

interface INaturalistApi {

    @GET("taxa/autocomplete")
    suspend fun searchTaxa(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = 10,
        @Query("locale") locale: String =
            Locale.getDefault().toLanguageTag()
    ): TaxonResponse
}