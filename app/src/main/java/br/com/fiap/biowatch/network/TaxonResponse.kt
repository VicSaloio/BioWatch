package br.com.fiap.biowatch.network

data class TaxonResponse(
    val total_results: Int,
    val results: List<Taxon>
)

data class Taxon(
    val id: Int,
    val name: String,
    val preferred_common_name: String?,
    val wikipedia_url: String?,
    val default_photo: TaxonPhoto?
)

data class TaxonPhoto(
    val medium_url: String?
)