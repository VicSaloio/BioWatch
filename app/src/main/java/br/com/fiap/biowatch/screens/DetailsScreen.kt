package br.com.fiap.biowatch.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.biowatch.R
import br.com.fiap.biowatch.model.Observation
import br.com.fiap.biowatch.ui.theme.BioWatchTheme
import java.util.Locale

@Composable
fun DetailsScreen(
    observation: Observation,
    onBackClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val locationNotProvided =
        stringResource(R.string.details_location_not_provided)

    val observationImage = remember(
        observation.imageUri
    ) {

        observation.imageUri?.let { imageUri ->

            runCatching {

                val uri = Uri.parse(imageUri)

                if (
                    uri.scheme == "file" &&
                    uri.path != null
                ) {

                    BitmapFactory
                        .decodeFile(
                            uri.path
                        )
                        ?.asImageBitmap()

                } else {

                    context.contentResolver
                        .openInputStream(uri)
                        ?.use { inputStream ->

                            BitmapFactory
                                .decodeStream(
                                    inputStream
                                )
                                ?.asImageBitmap()
                        }
                }

            }.getOrNull()
        }
    }

    val formattedDate =
        if (
            observation.observationDate.length == 8
        ) {

            "${observation.observationDate.substring(0, 2)}/" +
                    "${observation.observationDate.substring(2, 4)}/" +
                    observation.observationDate.substring(4)

        } else {

            observation.observationDate
        }

    val displayedLocation =
        when {

            observation.location.isNotBlank() -> {

                observation.location
            }

            observation.latitude != null &&
                    observation.longitude != null -> {

                String.format(
                    Locale.US,
                    "%.5f, %.5f",
                    observation.latitude,
                    observation.longitude
                )
            }

            else -> {

                locationNotProvided
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp,
                vertical = 18.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.action_back),
                    tint =
                        MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = stringResource(R.string.details_title),
                modifier = Modifier.padding(
                    start = 6.dp
                ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = observation.speciesName,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color =
                MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        if (
            !observation.scientificName.isNullOrBlank()
        ) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = observation.scientificName,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (
            observationImage != null
        ) {

            Image(
                bitmap = observationImage,
                contentDescription =
                    observation.speciesName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(
                        RoundedCornerShape(
                            22.dp
                        )
                    ),
                contentScale =
                    ContentScale.Crop
            )

        } else {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp),
                shape =
                    RoundedCornerShape(22.dp),
                color =
                    MaterialTheme.colorScheme.tertiary.copy(
                        alpha = 0.24f
                    )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(28.dp)
                    )

                    Icon(
                        imageVector =
                            Icons.Filled.Eco,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme.tertiary,
                        modifier =
                            Modifier.size(34.dp)
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text = stringResource(R.string.no_photo),
                        fontSize = 13.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
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
                        alpha = 0.10f
                    )
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {

                Text(
                    text = stringResource(R.string.details_information),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Text(
                    text = stringResource(R.string.details_category),
                    fontSize = 12.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Surface(
                    shape =
                        RoundedCornerShape(50.dp),
                    color =
                        MaterialTheme.colorScheme.tertiary.copy(
                            alpha = 0.25f
                        )
                ) {

                    Text(
                        text = observation.category,
                        modifier = Modifier.padding(
                            horizontal = 11.dp,
                            vertical = 5.dp
                        ),
                        fontSize = 13.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text = stringResource(R.string.details_location),
                    fontSize = 12.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme.secondary,
                        modifier =
                            Modifier.size(20.dp)
                    )

                    Text(
                        text = displayedLocation,
                        modifier = Modifier.padding(
                            start = 6.dp
                        ),
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )

                Text(
                    text = stringResource(R.string.details_date),
                    fontSize = 12.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.DateRange,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme.tertiary,
                        modifier =
                            Modifier.size(19.dp)
                    )

                    Text(
                        text = formattedDate,
                        modifier = Modifier.padding(
                            start = 6.dp
                        ),
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.Medium,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }

                if (
                    observation.notes.isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(20.dp)
                    )

                    Surface(
                        modifier =
                            Modifier.fillMaxWidth(),
                        shape =
                            RoundedCornerShape(
                                18.dp
                            ),
                        color =
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.13f
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(
                                    15.dp
                                )
                        ) {

                            Text(
                                text = stringResource(R.string.details_notes),
                                fontSize = 12.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        6.dp
                                    )
                            )

                            Text(
                                text =
                                    observation.notes,
                                fontSize = 15.sp,
                                lineHeight = 20.sp,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailsScreenPreview() {

    BioWatchTheme {

        DetailsScreen(
            observation = Observation(
                id = 1,
                speciesName = "Tucano",
                scientificName =
                    "Ramphastos toco",
                category = "Ave",
                location = "Suzano",
                observationDate =
                    "01092026",
                notes =
                    "Observado próximo a uma área arborizada.",
                imageUri = null
            )
        )
    }
}
