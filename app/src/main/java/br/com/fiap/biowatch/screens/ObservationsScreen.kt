package br.com.fiap.biowatch.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.biowatch.R
import br.com.fiap.biowatch.model.Observation
import br.com.fiap.biowatch.ui.theme.BioWatchTheme

@Composable
fun ObservationsScreen(
    observations: List<Observation> = emptyList(),
    onBackClick: () -> Unit = {},
    onDeleteObservation: (Observation) -> Unit = {},
    onObservationClick: (Observation) -> Unit = {}
) {

    val context = LocalContext.current

    val observationToDelete = remember {
        mutableStateOf<Observation?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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

            Column(
                modifier = Modifier.padding(
                    start = 6.dp
                )
            ) {

                Text(
                    text = stringResource(R.string.records_title),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Text(
                    text =
                        stringResource(R.string.records_subtitle),
                    fontSize = 14.sp,
                    color =
                        MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(22.dp)
        )

        if (observations.isEmpty()) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.tertiary.copy(
                        alpha = 0.22f
                    )
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 32.dp
                        ),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color =
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.25f
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Eco,
                            contentDescription = null,
                            modifier =
                                Modifier.padding(14.dp),
                            tint =
                                MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text =
                            stringResource(R.string.records_empty_title),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            stringResource(R.string.records_empty_description),
                        fontSize = 14.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp),
                contentPadding =
                    PaddingValues(
                        bottom = 24.dp
                    )
            ) {

                items(
                    items =
                        observations.asReversed()
                ) { observation ->

                    val observationImage =
                        remember(
                            observation.imageUri
                        ) {

                            observation.imageUri
                                ?.let { imageUri ->

                                    runCatching {

                                        val uri =
                                            Uri.parse(
                                                imageUri
                                            )

                                        if (
                                            uri.scheme ==
                                            "file" &&
                                            uri.path != null
                                        ) {

                                            BitmapFactory
                                                .decodeFile(
                                                    uri.path
                                                )
                                                ?.asImageBitmap()

                                        } else {

                                            context
                                                .contentResolver
                                                .openInputStream(
                                                    uri
                                                )
                                                ?.use {
                                                        inputStream ->

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
                            observation
                                .observationDate
                                .length == 8
                        ) {

                            "${observation.observationDate.substring(0, 2)}/" +
                                    "${observation.observationDate.substring(2, 4)}/" +
                                    observation.observationDate.substring(4)

                        } else {

                            observation.observationDate
                        }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onObservationClick(
                                    observation
                                )
                            },
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

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            if (
                                observationImage != null
                            ) {

                                Image(
                                    bitmap =
                                        observationImage,
                                    contentDescription =
                                        observation.speciesName,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                16.dp
                                            )
                                        ),
                                    contentScale =
                                        ContentScale.Crop
                                )

                            } else {

                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                16.dp
                                            )
                                        )
                                        .background(
                                            MaterialTheme.colorScheme.tertiary.copy(
                                                alpha = 0.24f
                                            )
                                        ),
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    Column(
                                        horizontalAlignment =
                                            Alignment.CenterHorizontally
                                    ) {

                                        Icon(
                                            imageVector =
                                                Icons.Filled.Eco,
                                            contentDescription =
                                                null,
                                            tint =
                                                MaterialTheme.colorScheme.tertiary
                                        )

                                        Spacer(
                                            modifier =
                                                Modifier.height(
                                                    3.dp
                                                )
                                        )

                                        Text(
                                            text = stringResource(R.string.no_photo),
                                            fontSize = 10.sp,
                                            color =
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.size(12.dp)
                            )

                            Column(
                                modifier =
                                    Modifier.weight(1f)
                            ) {

                                Text(
                                    text =
                                        observation.speciesName,
                                    fontSize = 17.sp,
                                    lineHeight = 19.sp,
                                    fontWeight =
                                        FontWeight.Bold,
                                    color =
                                        MaterialTheme.colorScheme.primary
                                )

                                if (
                                    !observation.scientificName
                                        .isNullOrBlank()
                                ) {

                                    Spacer(
                                        modifier =
                                            Modifier.height(
                                                4.dp
                                            )
                                    )

                                    Text(
                                        text =
                                            observation.scientificName,
                                        fontSize = 12.sp,
                                        lineHeight = 15.sp,
                                        color =
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(7.dp)
                                )

                                Surface(
                                    shape =
                                        RoundedCornerShape(
                                            50.dp
                                        ),
                                    color =
                                        MaterialTheme.colorScheme.tertiary.copy(
                                            alpha = 0.22f
                                        )
                                ) {

                                    Text(
                                        text =
                                            observation.category,
                                        modifier =
                                            Modifier.padding(
                                                horizontal =
                                                    9.dp,
                                                vertical =
                                                    4.dp
                                            ),
                                        fontSize = 11.sp,
                                        fontWeight =
                                            FontWeight.Medium,
                                        color =
                                            MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(8.dp)
                                )

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Filled.LocationOn,
                                        contentDescription =
                                            null,
                                        tint =
                                            MaterialTheme.colorScheme.secondary,
                                        modifier =
                                            Modifier.size(
                                                17.dp
                                            )
                                    )

                                    Text(
                                        text =
                                            observation.location,
                                        modifier =
                                            Modifier.padding(
                                                start = 4.dp
                                            ),
                                        fontSize = 13.sp,
                                        color =
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Spacer(
                                    modifier =
                                        Modifier.height(5.dp)
                                )

                                Row(
                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Icon(
                                        imageVector =
                                            Icons.Filled.DateRange,
                                        contentDescription =
                                            null,
                                        tint =
                                            MaterialTheme.colorScheme.tertiary,
                                        modifier =
                                            Modifier.size(
                                                16.dp
                                            )
                                    )

                                    Text(
                                        text =
                                            formattedDate,
                                        modifier =
                                            Modifier.padding(
                                                start = 4.dp
                                            ),
                                        fontSize = 12.sp,
                                        color =
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(
                                onClick = {

                                    observationToDelete.value =
                                        observation
                                },
                                modifier =
                                    Modifier.size(36.dp)
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Delete,
                                    contentDescription =
                                        stringResource(R.string.action_delete),
                                    tint =
                                        MaterialTheme.colorScheme.error.copy(
                                            alpha = 0.72f
                                        ),
                                    modifier =
                                        Modifier.size(19.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    observationToDelete.value
        ?.let { observation ->

            AlertDialog(
                onDismissRequest = {

                    observationToDelete.value =
                        null
                },
                title = {

                    Text(
                        text =
                            stringResource(R.string.records_delete_title),
                        color =
                            MaterialTheme.colorScheme.primary,
                        fontWeight =
                            FontWeight.Bold
                    )
                },
                text = {

                    Text(
                        text =
                            stringResource(R.string.records_delete_message)
                    )
                },
                confirmButton = {

                    TextButton(
                        onClick = {

                            onDeleteObservation(
                                observation
                            )

                            observationToDelete.value =
                                null
                        }
                    ) {

                        Text(
                            text = stringResource(R.string.records_delete_confirm),
                            color =
                                MaterialTheme.colorScheme.error
                        )
                    }
                },
                dismissButton = {

                    TextButton(
                        onClick = {

                            observationToDelete.value =
                                null
                        }
                    ) {

                        Text(
                            text = stringResource(R.string.records_delete_cancel),
                            color =
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
}

@Preview(showBackground = true)
@Composable
private fun ObservationsScreenPreview() {

    BioWatchTheme {

        ObservationsScreen(
            observations = listOf(
                Observation(
                    id = 1,
                    speciesName =
                        "Pomba-pretona",
                    scientificName =
                        "Patagioenas picazuro",
                    category = "Ave",
                    location =
                        "Praia Grande",
                    observationDate =
                        "05052026",
                    notes = "",
                    imageUri = null
                ),
                Observation(
                    id = 2,
                    speciesName =
                        "Corvo-marinho-d'orelhas",
                    scientificName =
                        "Nannopterum auritum",
                    category = "Ave",
                    location = "Suzano",
                    observationDate =
                        "25082026",
                    notes = "",
                    imageUri = null
                )
            )
        )
    }
}
