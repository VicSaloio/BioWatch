package br.com.fiap.biowatch.local

import br.com.fiap.biowatch.model.Observation

fun Observation.toEntity(): ObservationEntity {
    return ObservationEntity(
        id = id,
        speciesName = speciesName,
        category = category,
        location = location,
        observationDate = observationDate,
        notes = notes,
        imageUri = imageUri,
        latitude = latitude,
        longitude = longitude,
        scientificName = scientificName
    )
}

fun ObservationEntity.toObservation(): Observation {
    return Observation(
        id = id,
        speciesName = speciesName,
        category = category,
        location = location,
        observationDate = observationDate,
        notes = notes,
        imageUri = imageUri,
        latitude = latitude,
        longitude = longitude,
        scientificName = scientificName
    )
}