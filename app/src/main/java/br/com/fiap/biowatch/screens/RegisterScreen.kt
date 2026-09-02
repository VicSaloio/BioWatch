package br.com.fiap.biowatch.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import br.com.fiap.biowatch.R
import br.com.fiap.biowatch.model.Observation
import br.com.fiap.biowatch.network.NominatimClient
import br.com.fiap.biowatch.network.NominatimSearchResult
import br.com.fiap.biowatch.network.RetrofitClient
import br.com.fiap.biowatch.network.Taxon
import br.com.fiap.biowatch.ui.theme.BioWatchTheme
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.io.File
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@Composable
fun RegisterScreen(
    onBackClick: () -> Unit = {},
    onSaveObservation: (Observation) -> Unit = {}
) {

    val speciesName = remember {
        mutableStateOf("")
    }

    val scientificName = remember {
        mutableStateOf<String?>(null)
    }

    val category = remember {
        mutableStateOf("")
    }

    val location = remember {
        mutableStateOf("")
    }

    val observationDate = remember {
        mutableStateOf("")
    }

    val notes = remember {
        mutableStateOf("")
    }

    val selectedImageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val latitude = remember {
        mutableStateOf<Double?>(null)
    }

    val longitude = remember {
        mutableStateOf<Double?>(null)
    }

    val locationStatus = remember {
        mutableStateOf("")
    }

    val locationSearchMessage = remember {
        mutableStateOf("")
    }

    val locationSearchResults = remember {
        mutableStateOf<List<NominatimSearchResult>>(
            emptyList()
        )
    }

    val isSearchingLocation = remember {
        mutableStateOf(false)
    }

    val speciesSearchMessage = remember {
        mutableStateOf("")
    }

    val speciesSearchResults = remember {
        mutableStateOf<List<Taxon>>(emptyList())
    }

    val isSearchingSpecies = remember {
        mutableStateOf(false)
    }

    val validationMessage = remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    val searchingCurrentLocationText =
        stringResource(
            R.string.register_searching_current_location
        )

    val currentLocationCapturedText =
        stringResource(
            R.string.register_current_location_captured
        )

    val locationNameUnavailableText =
        stringResource(
            R.string.register_location_name_unavailable
        )

    val currentLocationErrorText =
        stringResource(
            R.string.register_current_location_error
        )

    val locationPermissionDeniedText =
        stringResource(
            R.string.register_location_permission_denied
        )

    val typeSpeciesText =
        stringResource(
            R.string.register_type_species
        )

    val selectSpeciesText =
        stringResource(
            R.string.register_select_species
        )

    val speciesNotFoundText =
        stringResource(
            R.string.register_species_not_found
        )

    val inaturalistErrorText =
        stringResource(
            R.string.register_inaturalist_error
        )

    val typeLocationText =
        stringResource(
            R.string.register_type_location
        )

    val selectLocationText =
        stringResource(
            R.string.register_select_location
        )

    val locationNotFoundText =
        stringResource(
            R.string.register_location_not_found
        )

    val locationSearchErrorText =
        stringResource(
            R.string.register_location_search_error
        )

    val locationSelectedText =
        stringResource(
            R.string.register_location_selected
        )

    val locationCoordinatesErrorText =
        stringResource(
            R.string.register_location_coordinates_error
        )

    val validationSpeciesText =
        stringResource(
            R.string.validation_species
        )

    val validationCategoryText =
        stringResource(
            R.string.validation_category
        )

    val validationLocationText =
        stringResource(
            R.string.validation_location
        )

    val validationLocationMapText =
        stringResource(
            R.string.validation_location_map
        )

    val validationDateText =
        stringResource(
            R.string.validation_date
        )

    val coroutineScope = rememberCoroutineScope()

    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(
            context
        )
    }

    val captureCurrentLocation: () -> Unit = {

        val hasFinePermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val hasCoarsePermission =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (
            hasFinePermission ||
            hasCoarsePermission
        ) {

            locationStatus.value =
                searchingCurrentLocationText

            locationSearchResults.value =
                emptyList()

            locationSearchMessage.value =
                ""

            fusedLocationClient
                .getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                )
                .addOnSuccessListener { currentLocation ->

                    if (
                        currentLocation != null
                    ) {

                        latitude.value =
                            currentLocation.latitude

                        longitude.value =
                            currentLocation.longitude

                        locationStatus.value =
                            currentLocationCapturedText

                        coroutineScope.launch {

                            try {

                                val response =
                                    NominatimClient.api
                                        .reverseGeocode(
                                            latitude =
                                                currentLocation.latitude,
                                            longitude =
                                                currentLocation.longitude
                                        )

                                val address =
                                    response.address

                                val city =
                                    address?.city
                                        ?: address?.town
                                        ?: address?.village
                                        ?: address?.municipality

                                val state =
                                    address?.state

                                val locationName =
                                    listOfNotNull(
                                        city,
                                        state
                                    )
                                        .filter {
                                            it.isNotBlank()
                                        }
                                        .distinct()
                                        .joinToString(", ")

                                if (
                                    locationName.isNotBlank()
                                ) {

                                    location.value =
                                        locationName

                                    locationStatus.value =
                                        currentLocationCapturedText

                                } else if (
                                    !response.display_name
                                        .isNullOrBlank()
                                ) {

                                    location.value =
                                        response.display_name

                                    locationStatus.value =
                                        currentLocationCapturedText
                                }

                            } catch (
                                _: Exception
                            ) {

                                locationStatus.value =
                                    locationNameUnavailableText
                            }
                        }

                    } else {

                        locationStatus.value =
                            currentLocationErrorText
                    }
                }
                .addOnFailureListener {

                    locationStatus.value =
                        currentLocationErrorText
                }
        }
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .RequestMultiplePermissions()
        ) { permissions ->

            val fineLocationGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                ] == true

            val coarseLocationGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_COARSE_LOCATION
                ] == true

            if (
                fineLocationGranted ||
                coarseLocationGranted
            ) {

                captureCurrentLocation()

            } else {

                locationStatus.value =
                    locationPermissionDeniedText
            }
        }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.OpenDocument()
        ) { uri ->

            uri?.let { selectedUri ->

                val imageFile = File(
                    context.filesDir,
                    "observation_${System.currentTimeMillis()}.jpg"
                )

                try {

                    context.contentResolver
                        .openInputStream(
                            selectedUri
                        )
                        ?.use { inputStream ->

                            imageFile
                                .outputStream()
                                .use { outputStream ->

                                    inputStream.copyTo(
                                        outputStream
                                    )
                                }
                        }

                    if (
                        imageFile.exists() &&
                        imageFile.length() > 0
                    ) {

                        selectedImageUri.value =
                            Uri.fromFile(
                                imageFile
                            )
                    }

                } catch (
                    exception: Exception
                ) {

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
                modifier = Modifier.padding(
                    start = 6.dp
                )
            ) {

                Text(
                    text =
                        stringResource(R.string.register_title),
                    fontSize = 28.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Text(
                    text =
                        stringResource(R.string.register_subtitle),
                    fontSize = 14.sp,
                    color =
                        MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        Surface(
            shape =
                RoundedCornerShape(50.dp),
            color =
                MaterialTheme.colorScheme.tertiary.copy(
                    alpha = 0.75f
                )
        ) {

            Text(
                text =
                    stringResource(R.string.register_step_identification),
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 7.dp
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
                Modifier.height(8.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(22.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    )
            ) {

                Text(
                    text =
                        stringResource(R.string.register_species_question),
                    fontSize = 17.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value =
                        speciesName.value,
                    onValueChange = {

                        speciesName.value = it

                        scientificName.value =
                            null

                        speciesSearchMessage.value =
                            ""

                        speciesSearchResults.value =
                            emptyList()
                    },
                    label = {
                        Text(
                            stringResource(R.string.register_species_name)
                        )
                    },
                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.Search,
                            contentDescription =
                                null,
                            tint =
                                MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.Transparent,
                            unfocusedContainerColor =
                                Color.Transparent,
                            focusedBorderColor =
                                MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.tertiary.copy(
                                    alpha = 0.55f
                                )
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {

                        if (
                            speciesName.value
                                .isBlank()
                        ) {

                            speciesSearchMessage.value =
                                typeSpeciesText

                            speciesSearchResults.value =
                                emptyList()

                        } else {

                            coroutineScope.launch {

                                isSearchingSpecies.value =
                                    true

                                speciesSearchMessage.value =
                                    ""

                                speciesSearchResults.value =
                                    emptyList()

                                try {

                                    val response =
                                        RetrofitClient.api
                                            .searchTaxa(
                                                speciesName.value
                                                    .trim()
                                            )

                                    val results =
                                        response.results
                                            .filter {
                                                !it.preferred_common_name
                                                    .isNullOrBlank()
                                            }
                                            .take(5)

                                    if (
                                        results.isNotEmpty()
                                    ) {

                                        speciesSearchResults.value =
                                            results

                                        speciesSearchMessage.value =
                                            selectSpeciesText

                                    } else {

                                        speciesSearchMessage.value =
                                            speciesNotFoundText
                                    }

                                } catch (
                                    _: Exception
                                ) {

                                    speciesSearchMessage.value =
                                        inaturalistErrorText

                                } finally {

                                    isSearchingSpecies.value =
                                        false
                                }
                            }
                        }
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.60f
                            )
                        ),
                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.16f
                                    ),
                                contentColor =
                                    MaterialTheme.colorScheme.primary
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Search,
                        contentDescription =
                            null
                    )

                    Text(
                        text =
                            if (
                                isSearchingSpecies.value
                            ) {
                                stringResource(R.string.register_searching_species)
                            } else {
                                stringResource(R.string.register_search_species)
                            },
                        modifier =
                            Modifier.padding(
                                start = 8.dp
                            ),
                        fontWeight =
                            FontWeight.Medium
                    )
                }

                if (
                    speciesSearchMessage.value
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )

                    Text(
                        text =
                            speciesSearchMessage.value,
                        modifier =
                            Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        color =
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign =
                            TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }

                speciesSearchResults.value
                    .forEach { taxon ->

                        val selectedSpeciesText =
                            stringResource(
                                R.string.register_selected_species,
                                taxon.name
                            )

                        val selectedSpeciesScientificText =
                            stringResource(
                                R.string.register_selected_species_scientific,
                                taxon.preferred_common_name
                                    ?: taxon.name,
                                taxon.name
                            )

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )

                        OutlinedButton(
                            onClick = {

                                speciesName.value =
                                    taxon.preferred_common_name
                                        ?: taxon.name

                                scientificName.value =
                                    taxon.name

                                speciesSearchResults.value =
                                    emptyList()

                                speciesSearchMessage.value =
                                    if (
                                        taxon.preferred_common_name !=
                                        null
                                    ) {

                                        selectedSpeciesScientificText

                                    } else {

                                        selectedSpeciesText
                                    }
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),
                            border =
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.60f
                                    )
                                ),
                            colors =
                                ButtonDefaults
                                    .outlinedButtonColors(
                                        containerColor =
                                            MaterialTheme.colorScheme.tertiary.copy(
                                                alpha = 0.14f
                                            ),
                                        contentColor =
                                            MaterialTheme.colorScheme.primary
                                    )
                        ) {

                            Column(
                                modifier =
                                    Modifier.padding(
                                        vertical = 4.dp
                                    ),
                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text =
                                        taxon.preferred_common_name
                                            ?: stringResource(R.string.register_common_name_unavailable),
                                    fontWeight =
                                        FontWeight.SemiBold,
                                    textAlign =
                                        TextAlign.Center
                                )

                                Spacer(
                                    modifier =
                                        Modifier.height(
                                            2.dp
                                        )
                                )

                                Text(
                                    text =
                                        taxon.name,
                                    fontSize = 13.sp,
                                    fontStyle =
                                        FontStyle.Italic,
                                    color =
                                        MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign =
                                        TextAlign.Center
                                )
                            }
                        }
                    }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value = category.value,
                    onValueChange = {
                        category.value = it
                    },
                    label = {
                        Text(stringResource(R.string.register_category))
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.register_category_example)
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.Transparent,
                            unfocusedContainerColor =
                                Color.Transparent,
                            focusedBorderColor =
                                MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.tertiary.copy(
                                    alpha = 0.55f
                                )
                        )
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Surface(
            shape =
                RoundedCornerShape(50.dp),
            color =
                MaterialTheme.colorScheme.secondary
        ) {

            Text(
                text =
                    stringResource(R.string.register_step_location_date),
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 7.dp
                ),
                fontSize = 11.sp,
                fontWeight =
                    FontWeight.Bold,
                color =
                    MaterialTheme.colorScheme.onSecondary
            )
        }

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(22.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    )
            ) {

                Text(
                    text =
                        stringResource(R.string.register_location_question),
                    fontSize = 17.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value =
                        location.value,
                    onValueChange = {

                        location.value = it

                        latitude.value = null

                        longitude.value = null

                        locationStatus.value =
                            ""

                        locationSearchMessage.value =
                            ""

                        locationSearchResults.value =
                            emptyList()
                    },
                    label = {
                        Text(
                            stringResource(R.string.register_location)
                        )
                    },
                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.LocationOn,
                            contentDescription =
                                null,
                            tint =
                                MaterialTheme.colorScheme.secondary
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.Transparent,
                            unfocusedContainerColor =
                                Color.Transparent,
                            focusedBorderColor =
                                MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.42f
                                )
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                OutlinedButton(
                    onClick = {

                        if (
                            location.value
                                .isBlank()
                        ) {

                            locationSearchMessage.value =
                                typeLocationText

                            locationSearchResults.value =
                                emptyList()

                        } else {

                            coroutineScope.launch {

                                isSearchingLocation.value =
                                    true

                                locationSearchMessage.value =
                                    ""

                                locationSearchResults.value =
                                    emptyList()

                                locationStatus.value =
                                    ""

                                try {

                                    val results =
                                        NominatimClient.api
                                            .searchPlace(
                                                query =
                                                    location.value
                                                        .trim()
                                            )
                                            .take(5)

                                    if (
                                        results.isNotEmpty()
                                    ) {

                                        locationSearchResults.value =
                                            results

                                        locationSearchMessage.value =
                                            selectLocationText

                                    } else {

                                        locationSearchMessage.value =
                                            locationNotFoundText
                                    }

                                } catch (
                                    _: Exception
                                ) {

                                    locationSearchMessage.value =
                                        locationSearchErrorText

                                } finally {

                                    isSearchingLocation.value =
                                        false
                                }
                            }
                        }
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.65f
                            )
                        ),
                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.10f
                                    ),
                                contentColor =
                                    MaterialTheme.colorScheme.secondary
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.Search,
                        contentDescription =
                            null
                    )

                    Text(
                        text =
                            if (
                                isSearchingLocation.value
                            ) {
                                stringResource(R.string.register_searching_location)
                            } else {
                                stringResource(R.string.register_search_location)
                            },
                        modifier =
                            Modifier.padding(
                                start = 8.dp
                            ),
                        fontWeight =
                            FontWeight.Medium
                    )
                }

                if (
                    locationSearchMessage.value
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                9.dp
                            )
                    )

                    Text(
                        text =
                            locationSearchMessage.value,
                        modifier =
                            Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        color =
                            MaterialTheme.colorScheme.secondary,
                        textAlign =
                            TextAlign.Center
                    )
                }

                locationSearchResults.value
                    .forEach { result ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    8.dp
                                )
                        )

                        OutlinedButton(
                            onClick = {

                                val resultLatitude =
                                    result.lat
                                        .toDoubleOrNull()

                                val resultLongitude =
                                    result.lon
                                        .toDoubleOrNull()

                                if (
                                    resultLatitude != null &&
                                    resultLongitude != null
                                ) {

                                    latitude.value =
                                        resultLatitude

                                    longitude.value =
                                        resultLongitude

                                    val shortLocationName =
                                        result.display_name
                                            .substringBefore(
                                                ","
                                            )
                                            .trim()

                                    location.value =
                                        shortLocationName.ifBlank {
                                            result.display_name
                                        }

                                    locationSearchResults.value =
                                        emptyList()

                                    locationSearchMessage.value =
                                        ""

                                    locationStatus.value =
                                        locationSelectedText

                                } else {

                                    locationStatus.value =
                                        locationCoordinatesErrorText
                                }
                            },
                            modifier =
                                Modifier.fillMaxWidth(),
                            shape =
                                RoundedCornerShape(
                                    16.dp
                                ),
                            border =
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.50f
                                    )
                                ),
                            colors =
                                ButtonDefaults
                                    .outlinedButtonColors(
                                        containerColor =
                                            MaterialTheme.colorScheme.secondary.copy(
                                                alpha = 0.07f
                                            ),
                                        contentColor =
                                            MaterialTheme.colorScheme.primary
                                    )
                        ) {

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),
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
                                        result.display_name,
                                    modifier =
                                        Modifier.padding(
                                            start = 8.dp
                                        ),
                                    fontSize = 13.sp,
                                    lineHeight = 17.sp,
                                    color =
                                        MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                Text(
                    text = stringResource(R.string.register_or),
                    modifier =
                        Modifier.fillMaxWidth(),
                    fontSize = 12.sp,
                    color =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                OutlinedButton(
                    onClick = {

                        val hasFinePermission =
                            ContextCompat
                                .checkSelfPermission(
                                    context,
                                    Manifest.permission
                                        .ACCESS_FINE_LOCATION
                                ) ==
                                    PackageManager
                                        .PERMISSION_GRANTED

                        val hasCoarsePermission =
                            ContextCompat
                                .checkSelfPermission(
                                    context,
                                    Manifest.permission
                                        .ACCESS_COARSE_LOCATION
                                ) ==
                                    PackageManager
                                        .PERMISSION_GRANTED

                        if (
                            hasFinePermission ||
                            hasCoarsePermission
                        ) {

                            captureCurrentLocation()

                        } else {

                            locationPermissionLauncher
                                .launch(
                                    arrayOf(
                                        Manifest.permission
                                            .ACCESS_FINE_LOCATION,
                                        Manifest.permission
                                            .ACCESS_COARSE_LOCATION
                                    )
                                )
                        }
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.65f
                            )
                        ),
                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.secondary.copy(
                                        alpha = 0.10f
                                    ),
                                contentColor =
                                    MaterialTheme.colorScheme.secondary
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.MyLocation,
                        contentDescription =
                            null
                    )

                    Text(
                        text =
                            stringResource(R.string.register_use_current_location),
                        modifier =
                            Modifier.padding(
                                start = 8.dp
                            ),
                        fontWeight =
                            FontWeight.Medium
                    )
                }

                if (
                    locationStatus.value
                        .isNotBlank()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            locationStatus.value,
                        modifier =
                            Modifier.fillMaxWidth(),
                        fontSize = 13.sp,
                        color =
                            MaterialTheme.colorScheme.secondary,
                        textAlign =
                            TextAlign.Center
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                OutlinedTextField(
                    value =
                        observationDate.value,
                    onValueChange = { newValue ->

                        observationDate.value =
                            newValue
                                .filter {
                                    it.isDigit()
                                }
                                .take(8)
                    },
                    label = {
                        Text(
                            stringResource(R.string.register_date)
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.register_date_placeholder)
                        )
                    },
                    leadingIcon = {

                        Icon(
                            imageVector =
                                Icons.Filled.DateRange,
                            contentDescription =
                                null,
                            tint =
                                MaterialTheme.colorScheme.secondary
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.Transparent,
                            unfocusedContainerColor =
                                Color.Transparent,
                            focusedBorderColor =
                                MaterialTheme.colorScheme.secondary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.secondary.copy(
                                    alpha = 0.42f
                                )
                        ),
                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        ),
                    visualTransformation =
                        VisualTransformation { text ->

                            val numbers =
                                text.text

                            val formattedDate =
                                buildString {

                                    numbers
                                        .forEachIndexed {
                                                index,
                                                character ->

                                            append(
                                                character
                                            )

                                            if (
                                                index == 1 &&
                                                numbers.length >
                                                2
                                            ) {

                                                append("/")
                                            }

                                            if (
                                                index == 3 &&
                                                numbers.length >
                                                4
                                            ) {

                                                append("/")
                                            }
                                        }
                                }

                            val offsetMapping =
                                object :
                                    OffsetMapping {

                                    override fun originalToTransformed(
                                        offset: Int
                                    ): Int {

                                        val newOffset =
                                            offset +
                                                    if (
                                                        offset > 1
                                                    ) {
                                                        1
                                                    } else {
                                                        0
                                                    } +
                                                    if (
                                                        offset > 3
                                                    ) {
                                                        1
                                                    } else {
                                                        0
                                                    }

                                        return newOffset
                                            .coerceAtMost(
                                                formattedDate.length
                                            )
                                    }

                                    override fun transformedToOriginal(
                                        offset: Int
                                    ): Int {

                                        val newOffset =
                                            offset -
                                                    if (
                                                        offset > 2
                                                    ) {
                                                        1
                                                    } else {
                                                        0
                                                    } -
                                                    if (
                                                        offset > 5
                                                    ) {
                                                        1
                                                    } else {
                                                        0
                                                    }

                                        return newOffset
                                            .coerceIn(
                                                0,
                                                numbers.length
                                            )
                                    }
                                }

                            TransformedText(
                                text =
                                    AnnotatedString(
                                        formattedDate
                                    ),
                                offsetMapping =
                                    offsetMapping
                            )
                        }
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Surface(
            shape =
                RoundedCornerShape(50.dp),
            color =
                MaterialTheme.colorScheme.tertiary.copy(
                    alpha = 0.85f
                )
        ) {

            Text(
                text =
                    stringResource(R.string.register_step_details),
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 7.dp
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
                Modifier.height(8.dp)
        )

        Card(
            modifier =
                Modifier.fillMaxWidth(),
            shape =
                RoundedCornerShape(22.dp),
            colors =
                CardDefaults.cardColors(
                    containerColor =
                        MaterialTheme.colorScheme.surface
                ),
            elevation =
                CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    )
            ) {

                Text(
                    text =
                        stringResource(R.string.register_complete_record),
                    fontSize = 17.sp,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedTextField(
                    value =
                        notes.value,
                    onValueChange = {
                        notes.value = it
                    },
                    label = {
                        Text(
                            stringResource(R.string.register_notes)
                        )
                    },
                    placeholder = {
                        Text(
                            stringResource(R.string.register_notes_placeholder)
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        OutlinedTextFieldDefaults.colors(
                            focusedContainerColor =
                                Color.Transparent,
                            unfocusedContainerColor =
                                Color.Transparent,
                            focusedBorderColor =
                                MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor =
                                MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.30f
                                )
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                OutlinedButton(
                    onClick = {

                        imagePickerLauncher.launch(
                            arrayOf(
                                "image/*"
                            )
                        )
                    },
                    modifier =
                        Modifier.fillMaxWidth(),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    border =
                        BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.tertiary.copy(
                                alpha = 0.65f
                            )
                        ),
                    colors =
                        ButtonDefaults
                            .outlinedButtonColors(
                                containerColor =
                                    MaterialTheme.colorScheme.tertiary.copy(
                                        alpha = 0.14f
                                    ),
                                contentColor =
                                    MaterialTheme.colorScheme.primary
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Filled.PhotoCamera,
                        contentDescription =
                            null
                    )

                    Text(
                        text =
                            if (
                                selectedImageUri.value ==
                                null
                            ) {
                                stringResource(R.string.register_add_photo)
                            } else {
                                stringResource(R.string.register_change_photo)
                            },
                        modifier =
                            Modifier.padding(
                                start = 8.dp
                            ),
                        fontWeight =
                            FontWeight.Medium
                    )
                }

                selectedImageUri.value
                    ?.let { uri ->

                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )

                        val selectedImage =
                            remember(uri) {

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
                            }

                        selectedImage?.let { image ->

                            Image(
                                bitmap = image,
                                contentDescription =
                                    stringResource(R.string.register_observation_photo),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            18.dp
                                        )
                                    ),
                                contentScale =
                                    ContentScale.Crop
                            )
                        }
                    }
            }
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        if (
            validationMessage.value
                .isNotBlank()
        ) {

            Surface(
                modifier =
                    Modifier.fillMaxWidth(),
                shape =
                    RoundedCornerShape(
                        16.dp
                    ),
                color =
                    MaterialTheme.colorScheme.error.copy(
                        alpha = 0.10f
                    )
            ) {

                Text(
                    text =
                        validationMessage.value,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    fontSize = 14.sp,
                    color =
                        MaterialTheme.colorScheme.error,
                    textAlign =
                        TextAlign.Center
                )
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )
        }

        Button(
            onClick = {

                when {

                    speciesName.value
                        .isBlank() -> {

                        validationMessage.value =
                            validationSpeciesText
                    }

                    category.value
                        .isBlank() -> {

                        validationMessage.value =
                            validationCategoryText
                    }

                    location.value
                        .isBlank() -> {

                        validationMessage.value =
                            validationLocationText
                    }

                    latitude.value == null ||
                            longitude.value == null -> {

                        validationMessage.value =
                            validationLocationMapText
                    }

                    observationDate.value.length !=
                            8 -> {

                        validationMessage.value =
                            validationDateText
                    }

                    else -> {

                        validationMessage.value =
                            ""

                        val observation =
                            Observation(
                                speciesName =
                                    speciesName.value,
                                category =
                                    category.value,
                                location =
                                    location.value,
                                observationDate =
                                    observationDate.value,
                                notes =
                                    notes.value,
                                imageUri =
                                    selectedImageUri.value
                                        ?.toString(),
                                latitude =
                                    latitude.value,
                                longitude =
                                    longitude.value,
                                scientificName =
                                    scientificName.value
                            )

                        onSaveObservation(
                            observation
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape =
                RoundedCornerShape(50.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary
                )
        ) {

            Icon(
                imageVector =
                    Icons.Filled.Save,
                contentDescription =
                    null
            )

            Text(
                text =
                    stringResource(R.string.register_save),
                modifier =
                    Modifier.padding(
                        start = 8.dp
                    ),
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.SemiBold
            )
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterScreenPreview() {

    BioWatchTheme {
        RegisterScreen()
    }
}
