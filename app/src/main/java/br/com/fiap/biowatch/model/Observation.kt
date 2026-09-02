package br.com.fiap.biowatch.model

data class Observation(
    val id: Int = 0,
    val speciesName: String,
    val category: String,
    val location: String,
    val observationDate: String,
    val notes: String,
    val imageUri: String?,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val scientificName: String? = null
)