package br.com.fiap.biowatch.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.biowatch.R
import br.com.fiap.biowatch.model.Observation
import br.com.fiap.biowatch.ui.theme.BioWatchTheme
import io.github.dellisd.spatialk.geojson.Position
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.layers.CircleLayer
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.util.ClickResult

@Composable
fun MapScreen(
    observations: List<Observation> = emptyList(),
    onBackClick: () -> Unit = {},
    onObservationClick: (Observation) -> Unit = {}
) {

    val mappedObservations =
        observations.filter { observation ->

            observation.latitude != null &&
                    observation.longitude != null
        }

    val observationsByPosition =
        mappedObservations.groupBy { observation ->

            Pair(
                observation.latitude!!,
                observation.longitude!!
            )
        }

    val lastObservation =
        mappedObservations.lastOrNull()

    val initialLongitude =
        lastObservation?.longitude ?: -46.63331

    val initialLatitude =
        lastObservation?.latitude ?: -23.55052

    val cameraState =
        rememberCameraState(
            firstPosition = CameraPosition(
                target = Position(
                    latitude = initialLatitude,
                    longitude = initialLongitude
                ),
                zoom = 15.0
            )
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            )
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription =
                        stringResource(R.string.action_back),
                    tint =
                        MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier =
                    Modifier.padding(
                        start = 6.dp
                    )
            ) {

                Text(
                    text = stringResource(R.string.map_title),
                    fontSize = 28.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Text(
                    text =
                        stringResource(R.string.map_subtitle),
                    fontSize = 14.sp,
                    color =
                        MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Surface(
            shape =
                RoundedCornerShape(50.dp),
            color =
                MaterialTheme.colorScheme.tertiary.copy(
                    alpha = 0.22f
                )
        ) {

            Text(
                text =
                    pluralStringResource(
                        R.plurals.map_observation_count,
                        mappedObservations.size,
                        mappedObservations.size
                    ),
                modifier =
                    Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 7.dp
                    ),
                fontSize = 12.sp,
                fontWeight =
                    FontWeight.SemiBold,
                color =
                    MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            Card(
                modifier =
                    Modifier.fillMaxSize(),
                shape =
                    RoundedCornerShape(22.dp),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    ),
                border =
                    BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f
                        )
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
            ) {

                MaplibreMap(
                    modifier =
                        Modifier.fillMaxSize(),
                    baseStyle =
                        BaseStyle.Uri(
                            "https://tiles.openfreemap.org/styles/liberty"
                        ),
                    cameraState =
                        cameraState
                ) {

                    for (
                    (index, observation) in
                    mappedObservations.withIndex()
                    ) {

                        val originalLatitude =
                            observation.latitude!!

                        val originalLongitude =
                            observation.longitude!!

                        val positionKey =
                            Pair(
                                originalLatitude,
                                originalLongitude
                            )

                        val observationsAtSamePosition =
                            observationsByPosition[
                                positionKey
                            ].orEmpty()

                        val totalAtSamePosition =
                            observationsAtSamePosition.size

                        val duplicateIndex =
                            mappedObservations
                                .take(index)
                                .count { previousObservation ->

                                    previousObservation.latitude ==
                                            originalLatitude &&
                                            previousObservation.longitude ==
                                            originalLongitude
                                }

                        val visualOffset =
                            if (
                                totalAtSamePosition > 1
                            ) {
                                0.0013
                            } else {
                                0.0
                            }

                        val angle =
                            if (
                                totalAtSamePosition > 1
                            ) {

                                2.0 *
                                        PI *
                                        duplicateIndex /
                                        totalAtSamePosition

                            } else {

                                0.0
                            }

                        val longitudeCorrection =
                            cos(
                                Math.toRadians(
                                    originalLatitude
                                )
                            )
                                .coerceAtLeast(
                                    0.2
                                )

                        val visualLatitude =
                            originalLatitude +
                                    cos(angle) *
                                    visualOffset

                        val visualLongitude =
                            originalLongitude +
                                    sin(angle) *
                                    (
                                            visualOffset /
                                                    longitudeCorrection
                                            )

                        val observationGeoJson =
                            """
                            {
                                "type": "FeatureCollection",
                                "features": [
                                    {
                                        "type": "Feature",
                                        "geometry": {
                                            "type": "Point",
                                            "coordinates": [
                                                $visualLongitude,
                                                $visualLatitude
                                            ]
                                        }
                                    }
                                ]
                            }
                            """.trimIndent()

                        val observationSource =
                            rememberGeoJsonSource(
                                data =
                                    GeoJsonData.JsonString(
                                        observationGeoJson
                                    )
                            )

                        CircleLayer(
                            id =
                                "observation-point-" +
                                        "${observation.id}-$index",
                            source =
                                observationSource,
                            onClick = {

                                onObservationClick(
                                    observation
                                )

                                ClickResult.Consume
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(14.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Surface(
                    shape =
                        CircleShape,
                    color =
                        MaterialTheme.colorScheme.primary,
                    shadowElevation = 3.dp
                ) {

                    IconButton(
                        onClick = {

                            cameraState.position =
                                cameraState.position.copy(
                                    zoom =
                                        (
                                                cameraState.position.zoom +
                                                        1.0
                                                ).coerceAtMost(
                                                20.0
                                            )
                                )
                        },
                        modifier =
                            Modifier.size(48.dp)
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Add,
                            contentDescription =
                                stringResource(R.string.action_increase_zoom),
                            tint =
                                MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Surface(
                    shape =
                        CircleShape,
                    color =
                        MaterialTheme.colorScheme.surface,
                    shadowElevation = 3.dp,
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.16f
                            )
                        )
                ) {

                    IconButton(
                        onClick = {

                            cameraState.position =
                                cameraState.position.copy(
                                    zoom =
                                        (
                                                cameraState.position.zoom -
                                                        1.0
                                                ).coerceAtLeast(
                                                1.0
                                            )
                                )
                        },
                        modifier =
                            Modifier.size(48.dp)
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Remove,
                            contentDescription =
                                stringResource(R.string.action_decrease_zoom),
                            tint =
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MapScreenPreview() {

    BioWatchTheme {
        MapScreen()
    }
}
