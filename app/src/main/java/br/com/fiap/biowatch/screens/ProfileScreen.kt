package br.com.fiap.biowatch.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.biowatch.R
import br.com.fiap.biowatch.ui.theme.BioWatchTheme
import java.io.File

@Composable
fun ProfileScreen(
    observationsCount: Int = 0,
    speciesCount: Int = 0,
    locationCount: Int = 0,
    onBackClick: () -> Unit = {}
) {

    val context = LocalContext.current

    val profileImageFile = remember {
        File(
            context.filesDir,
            "profile_photo.jpg"
        )
    }

    val profileImageUri = remember {
        mutableStateOf<Uri?>(
            if (
                profileImageFile.exists() &&
                profileImageFile.length() > 0
            ) {
                Uri.fromFile(
                    profileImageFile
                )
            } else {
                null
            }
        )
    }

    val profileImageVersion = remember {
        mutableStateOf(0)
    }

    val profileImage = remember(
        profileImageUri.value,
        profileImageVersion.value
    ) {

        profileImageUri.value?.let { uri ->

            runCatching {

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

    val profileImagePicker =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.OpenDocument()
        ) { uri ->

            uri?.let { selectedUri ->

                try {

                    context.contentResolver
                        .openInputStream(
                            selectedUri
                        )
                        ?.use { inputStream ->

                            profileImageFile
                                .outputStream()
                                .use { outputStream ->

                                    inputStream.copyTo(
                                        outputStream
                                    )
                                }
                        }

                    if (
                        profileImageFile.exists() &&
                        profileImageFile.length() > 0
                    ) {

                        profileImageUri.value =
                            Uri.fromFile(
                                profileImageFile
                            )

                        profileImageVersion.value++
                    }

                } catch (exception: Exception) {

                    exception.printStackTrace()
                }
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

            Column(
                modifier = Modifier.padding(
                    start = 6.dp
                )
            ) {

                Text(
                    text = stringResource(R.string.profile_title),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Text(
                    text =
                        stringResource(R.string.profile_subtitle),
                    fontSize = 14.sp,
                    color =
                        MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(24.dp),
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
                    .padding(
                        horizontal = 20.dp,
                        vertical = 22.dp
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                if (
                    profileImage != null
                ) {

                    Image(
                        bitmap = profileImage,
                        contentDescription =
                            stringResource(R.string.profile_photo_description),
                        modifier = Modifier
                            .size(92.dp)
                            .clip(
                                CircleShape
                            ),
                        contentScale =
                            ContentScale.Crop
                    )

                } else {

                    Surface(
                        modifier =
                            Modifier.size(92.dp),
                        shape = CircleShape,
                        color =
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.28f
                            )
                    ) {

                        Column(
                            modifier =
                                Modifier.fillMaxSize(),
                            horizontalAlignment =
                                Alignment.CenterHorizontally
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.height(
                                        26.dp
                                    )
                            )

                            Icon(
                                imageVector =
                                    Icons.Filled.Person,
                                contentDescription =
                                    null,
                                modifier =
                                    Modifier.size(
                                        38.dp
                                    ),
                                tint =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {

                        profileImagePicker.launch(
                            arrayOf(
                                "image/*"
                            )
                        )
                    },
                    shape =
                        RoundedCornerShape(50.dp),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.65f
                            )
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.PhotoCamera,
                        contentDescription = null,
                        tint =
                            MaterialTheme.colorScheme.primary,
                        modifier =
                            Modifier.size(18.dp)
                    )

                    Text(
                        text =
                            if (
                                profileImage == null
                            ) {
                                stringResource(R.string.profile_add_photo)
                            } else {
                                stringResource(R.string.profile_change_photo)
                            },
                        modifier =
                            Modifier.padding(
                                start = 7.dp
                            ),
                        color =
                            MaterialTheme.colorScheme.primary,
                        fontWeight =
                            FontWeight.Medium
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = stringResource(R.string.profile_activity_title),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                Text(
                    text =
                        stringResource(R.string.profile_activity_description),
                    modifier =
                        Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Text(
            text = stringResource(R.string.profile_summary),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color =
                MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.spacedBy(10.dp)
        ) {

            ProfileStatCard(
                modifier = Modifier.weight(1f),
                icon =
                    Icons.Filled.List,
                value =
                    observationsCount.toString(),
                label = stringResource(R.string.label_records),
                accent = ProfileAccent.Green
            )

            ProfileStatCard(
                modifier = Modifier.weight(1f),
                icon =
                    Icons.Filled.Eco,
                value =
                    speciesCount.toString(),
                label = stringResource(R.string.label_species),
                accent = ProfileAccent.Olive
            )

            ProfileStatCard(
                modifier = Modifier.weight(1f),
                icon =
                    Icons.Filled.LocationOn,
                value =
                    locationCount.toString(),
                label = stringResource(R.string.label_locations),
                accent = ProfileAccent.Terracotta
            )
        }

        Spacer(
            modifier = Modifier.height(28.dp)
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
                    MaterialTheme.colorScheme.tertiary.copy(
                        alpha = 0.18f
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
                    .padding(20.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    shape = CircleShape,
                    color =
                        MaterialTheme.colorScheme.tertiary.copy(
                            alpha = 0.28f
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Eco,
                        contentDescription = null,
                        modifier =
                            Modifier.padding(12.dp),
                        tint =
                            MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = 14.dp
                        )
                ) {

                    Text(
                        text = stringResource(R.string.profile_about_title),
                        fontSize = 18.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color =
                            MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier =
                            Modifier.height(6.dp)
                    )

                    Text(
                        text =
                            stringResource(R.string.profile_about_description),
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(20.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
            border =
                BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.tertiary.copy(
                        alpha = 0.18f
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
                    .padding(18.dp)
            ) {

                Text(
                    text =
                        stringResource(
                            R.string.profile_data_sources_title
                        ),
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text =
                        stringResource(
                            R.string.profile_data_sources_species
                        ),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        stringResource(
                            R.string.profile_data_sources_location
                        ),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        stringResource(
                            R.string.profile_data_sources_map
                        ),
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
private fun ProfileStatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    accent: ProfileAccent
) {

    val accentColor =
        when (accent) {

            ProfileAccent.Green ->
                MaterialTheme.colorScheme.primary

            ProfileAccent.Olive ->
                MaterialTheme.colorScheme.tertiary

            ProfileAccent.Terracotta ->
                MaterialTheme.colorScheme.secondary
        }

    Card(
        modifier = modifier,
        shape =
            RoundedCornerShape(20.dp),
        colors =
            CardDefaults.cardColors(
                containerColor =
                    MaterialTheme.colorScheme.surface
            ),
        border =
            BorderStroke(
                1.dp,
                accentColor.copy(
                    alpha = 0.18f
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
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                shape = CircleShape,
                color =
                    accentColor.copy(
                        alpha =
                            if (
                                accent ==
                                ProfileAccent.Terracotta
                            ) {
                                0.16f
                            } else {
                                0.23f
                            }
                    )
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier =
                        Modifier.padding(8.dp),
                    tint = accentColor
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = value,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier =
                    Modifier.height(2.dp)
            )

            Text(
                text = label,
                fontSize = 11.sp,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign =
                    TextAlign.Center
            )
        }
    }
}

private enum class ProfileAccent {
    Green,
    Olive,
    Terracotta
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {

    BioWatchTheme {

        ProfileScreen(
            observationsCount = 8,
            speciesCount = 5,
            locationCount = 3
        )
    }
}
