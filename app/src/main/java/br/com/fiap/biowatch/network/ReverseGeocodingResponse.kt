package br.com.fiap.biowatch.network

data class ReverseGeocodingResponse(
    val display_name: String?,
    val address: ReverseAddress?
)

data class ReverseAddress(
    val city: String?,
    val town: String?,
    val village: String?,
    val municipality: String?,
    val state: String?,
    val country: String?
)