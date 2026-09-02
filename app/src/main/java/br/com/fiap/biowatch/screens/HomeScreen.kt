package br.com.fiap.biowatch.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
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
fun HomeScreen(
    onRegisterClick: () -> Unit = {},
    onObservationsClick: () -> Unit = {},
    onMapClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    lastObservation: Observation? = null,
    speciesCount: Int = 0,
    locationCount: Int = 0
) {

    val context =
        LocalContext.current

    val lastObservationImage =
        remember(
            lastObservation?.imageUri
        ) {

            lastObservation
                ?.imageUri
                ?.let { imageUri ->

                    runCatching {

                        val uri =
                            Uri.parse(
                                imageUri
                            )

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

                            context
                                .contentResolver
                                .openInputStream(
                                    uri
                                )
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

    val lastObservationDate =
        lastObservation
            ?.observationDate
            ?.let { date ->

                if (
                    date.length == 8
                ) {

                    "${date.substring(0, 2)}/" +
                            "${date.substring(2, 4)}/" +
                            date.substring(4)

                } else {

                    date
                }

            } ?: "28 ago 2026"

    Scaffold(
        modifier =
            Modifier.fillMaxSize(),
        containerColor =
            MaterialTheme.colorScheme.background,
        bottomBar = {

            Surface(
                color =
                    MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp
            ) {

                NavigationBar(
                    modifier =
                        Modifier.fillMaxWidth(),
                    containerColor =
                        MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {

                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.Home,
                                contentDescription =
                                    stringResource(
                                        R.string.nav_home
                                    )
                            )
                        },
                        label = {

                            Text(
                                text =
                                    stringResource(
                                        R.string.nav_home
                                    ),
                                fontSize = 11.sp
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.18f
                                    ),
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick =
                            onObservationsClick,
                        icon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.List,
                                contentDescription =
                                    stringResource(
                                        R.string.nav_records
                                    )
                            )
                        },
                        label = {

                            Text(
                                text =
                                    stringResource(
                                        R.string.nav_records
                                    ),
                                fontSize = 11.sp
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.18f
                                    ),
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick =
                            onRegisterClick,
                        icon = {

                            Surface(
                                shape =
                                    CircleShape,
                                color =
                                    MaterialTheme.colorScheme.primary
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Add,
                                    contentDescription =
                                        stringResource(
                                            R.string.register_title
                                        ),
                                    tint =
                                        MaterialTheme.colorScheme.onPrimary,
                                    modifier =
                                        Modifier.padding(
                                            7.dp
                                        )
                                )
                            }
                        },
                        label = {

                            Text(
                                text =
                                    stringResource(
                                        R.string.nav_new
                                    ),
                                fontSize = 11.sp
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.surface,
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick =
                            onMapClick,
                        icon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.Map,
                                contentDescription =
                                    stringResource(
                                        R.string.nav_map
                                    )
                            )
                        },
                        label = {

                            Text(
                                text =
                                    stringResource(
                                        R.string.nav_map
                                    ),
                                fontSize = 11.sp
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.18f
                                    ),
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick =
                            onProfileClick,
                        icon = {

                            Icon(
                                imageVector =
                                    Icons.Filled.Person,
                                contentDescription =
                                    stringResource(
                                        R.string.nav_profile
                                    )
                            )
                        },
                        label = {

                            Text(
                                text =
                                    stringResource(
                                        R.string.nav_profile
                                    ),
                                fontSize = 11.sp
                            )
                        },
                        colors =
                            NavigationBarItemDefaults.colors(
                                selectedIconColor =
                                    MaterialTheme.colorScheme.primary,
                                selectedTextColor =
                                    MaterialTheme.colorScheme.primary,
                                indicatorColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.18f
                                    ),
                                unselectedIconColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(
                    innerPadding
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Surface(
                    shape =
                        RoundedCornerShape(
                            50.dp
                        ),
                    color =
                        MaterialTheme.colorScheme.tertiary.copy(
                            alpha = 0.28f
                        )
                ) {

                    Text(
                        text =
                            stringResource(
                                R.string.home_title
                            ).uppercase(),
                        modifier =
                            Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                        fontSize = 11.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                Text(
                    text =
                        stringResource(
                            R.string.home_title
                        ),
                    color =
                        MaterialTheme.colorScheme.primary,
                    fontSize = 34.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            2.dp
                        )
                )

                Text(
                    text =
                        stringResource(
                            R.string.home_slogan
                        ),
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.Medium,
                    color =
                        MaterialTheme.colorScheme.secondary
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )

                Text(
                    text =
                        stringResource(
                            R.string.home_question
                        ),
                    fontSize = 16.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        22.dp
                    )
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        28.dp
                    ),
                colors =
                    CardDefaults.cardColors(
                        containerColor =
                            MaterialTheme.colorScheme.surface
                    ),
                elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
            ) {

                Column(
                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Image(
                        painter =
                            painterResource(
                                id =
                                    R.drawable.natureza_home
                            ),
                        contentDescription =
                            stringResource(
                                R.string.home_nature_image_description
                            ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                16f / 9f
                            ),
                        contentScale =
                            ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 20.dp,
                                vertical = 20.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Surface(
                            shape =
                                CircleShape,
                            color =
                                MaterialTheme.colorScheme.tertiary.copy(
                                    alpha = 0.32f
                                )
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Eco,
                                contentDescription =
                                    null,
                                tint =
                                    MaterialTheme.colorScheme.primary,
                                modifier =
                                    Modifier.padding(
                                        14.dp
                                    )
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(
                                    1f
                                )
                                .padding(
                                    start = 14.dp
                                )
                        ) {

                            Text(
                                text =
                                    stringResource(
                                        R.string.home_explore_title
                                    ),
                                fontSize = 20.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary,
                                lineHeight = 23.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        8.dp
                                    )
                            )

                            Text(
                                text =
                                    stringResource(
                                        R.string.home_explore_description
                                    ),
                                fontSize = 14.sp,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 19.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        14.dp
                                    )
                            )

                            Button(
                                onClick =
                                    onRegisterClick,
                                shape =
                                    RoundedCornerShape(
                                        50.dp
                                    ),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor =
                                            MaterialTheme.colorScheme.primary
                                    )
                            ) {

                                Icon(
                                    imageVector =
                                        Icons.Filled.Eco,
                                    contentDescription =
                                        null
                                )

                                Text(
                                    text =
                                        stringResource(
                                            R.string.home_register_species
                                        ),
                                    modifier =
                                        Modifier.padding(
                                            start = 7.dp
                                        ),
                                    fontSize = 14.sp,
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )

            Text(
                text =
                    stringResource(
                        R.string.home_your_records
                    ),
                modifier =
                    Modifier.fillMaxWidth(),
                fontSize = 19.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Card(
                    modifier =
                        Modifier.weight(
                            1f
                        ),
                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.16f
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
                            .padding(
                                horizontal = 14.dp,
                                vertical = 17.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Surface(
                            shape =
                                CircleShape,
                            color =
                                MaterialTheme.colorScheme.tertiary.copy(
                                    alpha = 0.30f
                                )
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.Eco,
                                contentDescription =
                                    null,
                                tint =
                                    MaterialTheme.colorScheme.primary,
                                modifier =
                                    Modifier.padding(
                                        8.dp
                                    )
                            )
                        }

                        Column(
                            modifier =
                                Modifier.padding(
                                    start = 10.dp
                                )
                        ) {

                            Text(
                                text =
                                    speciesCount
                                        .toString(),
                                fontSize = 27.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text =
                                    stringResource(
                                        R.string.label_species
                                    ),
                                fontSize = 12.sp,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Card(
                    modifier =
                        Modifier.weight(
                            1f
                        ),
                    shape =
                        RoundedCornerShape(
                            20.dp
                        ),
                    colors =
                        CardDefaults.cardColors(
                            containerColor =
                                MaterialTheme.colorScheme.surface
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.16f
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
                            .padding(
                                horizontal = 14.dp,
                                vertical = 17.dp
                            ),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Surface(
                            shape =
                                CircleShape,
                            color =
                                MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.20f
                                )
                        ) {

                            Icon(
                                imageVector =
                                    Icons.Filled.LocationOn,
                                contentDescription =
                                    null,
                                tint =
                                    MaterialTheme.colorScheme.secondary,
                                modifier =
                                    Modifier.padding(
                                        8.dp
                                    )
                            )
                        }

                        Column(
                            modifier =
                                Modifier.padding(
                                    start = 10.dp
                                )
                        ) {

                            Text(
                                text =
                                    locationCount
                                        .toString(),
                                fontSize = 27.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text =
                                    stringResource(
                                        R.string.label_locations
                                    ),
                                fontSize = 12.sp,
                                color =
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )

            Text(
                text =
                    stringResource(
                        R.string.home_latest_observation
                    ),
                modifier =
                    Modifier.fillMaxWidth(),
                fontSize = 19.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Card(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        22.dp
                    ),
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
                        .padding(
                            12.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    if (
                        lastObservationImage !=
                        null
                    ) {

                        Image(
                            bitmap =
                                lastObservationImage,
                            contentDescription =
                                lastObservation
                                    ?.speciesName,
                            modifier = Modifier
                                .weight(
                                    1f
                                )
                                .aspectRatio(
                                    1f
                                )
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
                                .weight(
                                    1f
                                )
                                .aspectRatio(
                                    1f
                                )
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
                                            4.dp
                                        )
                                )

                                Text(
                                    text =
                                        stringResource(
                                            R.string.no_photo
                                        ),
                                    fontSize = 12.sp,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(
                                1.35f
                            )
                            .padding(
                                start = 16.dp,
                                end = 4.dp
                            )
                    ) {

                        Text(
                            text =
                                lastObservation
                                    ?.speciesName
                                    ?: "Bem-te-vi",
                            fontSize = 18.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color =
                                MaterialTheme.colorScheme.primary
                        )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    7.dp
                                )
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
                                    MaterialTheme.colorScheme.secondary
                            )

                            Text(
                                text =
                                    lastObservation
                                        ?.location
                                        ?: "Mogi das Cruzes",
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
                                Modifier.height(
                                    6.dp
                                )
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
                                    lastObservationDate,
                                modifier =
                                    Modifier.padding(
                                        horizontal = 9.dp,
                                        vertical = 4.dp
                                    ),
                                fontSize = 11.sp,
                                fontWeight =
                                    FontWeight.Medium,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {

    BioWatchTheme {
        HomeScreen()
    }
}