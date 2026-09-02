package br.com.fiap.biowatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.fiap.biowatch.local.BioWatchDatabase
import br.com.fiap.biowatch.local.toEntity
import br.com.fiap.biowatch.local.toObservation
import br.com.fiap.biowatch.model.Observation
import br.com.fiap.biowatch.screens.DetailsScreen
import br.com.fiap.biowatch.screens.HomeScreen
import br.com.fiap.biowatch.screens.MapScreen
import br.com.fiap.biowatch.screens.ObservationsScreen
import br.com.fiap.biowatch.screens.ProfileScreen
import br.com.fiap.biowatch.screens.RegisterScreen
import br.com.fiap.biowatch.ui.theme.BioWatchTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            BioWatchTheme {

                val showRegisterScreen = remember {
                    mutableStateOf(false)
                }

                val showObservationsScreen = remember {
                    mutableStateOf(false)
                }

                val showMapScreen = remember {
                    mutableStateOf(false)
                }

                val showProfileScreen = remember {
                    mutableStateOf(false)
                }

                val selectedObservation = remember {
                    mutableStateOf<Observation?>(null)
                }

                val observations = remember {
                    mutableStateListOf<Observation>()
                }

                val database = remember {
                    BioWatchDatabase.getDatabase(applicationContext)
                }

                val observationDao = remember {
                    database.observationDao()
                }

                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(Unit) {

                    val savedObservations = observationDao
                        .getAll()
                        .reversed()
                        .map {
                            it.toObservation()
                        }

                    observations.clear()
                    observations.addAll(savedObservations)
                }

                BackHandler(
                    enabled =
                        showRegisterScreen.value ||
                                showObservationsScreen.value ||
                                showMapScreen.value ||
                                showProfileScreen.value ||
                                selectedObservation.value != null
                ) {

                    if (selectedObservation.value != null) {

                        selectedObservation.value = null

                    } else {

                        showRegisterScreen.value = false
                        showObservationsScreen.value = false
                        showMapScreen.value = false
                        showProfileScreen.value = false
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    if (showRegisterScreen.value) {

                        RegisterScreen(
                            onBackClick = {
                                showRegisterScreen.value = false
                            },
                            onSaveObservation = { observation ->

                                coroutineScope.launch {

                                    val generatedId = observationDao.insert(
                                        observation.toEntity()
                                    )

                                    observations.add(
                                        observation.copy(
                                            id = generatedId.toInt()
                                        )
                                    )

                                    showRegisterScreen.value = false
                                }
                            }
                        )

                    } else if (selectedObservation.value != null) {

                        DetailsScreen(
                            observation = selectedObservation.value!!,
                            onBackClick = {
                                selectedObservation.value = null
                            }
                        )

                    } else if (showObservationsScreen.value) {

                        ObservationsScreen(
                            observations = observations,
                            onBackClick = {
                                showObservationsScreen.value = false
                            },
                            onDeleteObservation = { observation ->

                                coroutineScope.launch {

                                    observationDao.deleteById(
                                        observation.id
                                    )

                                    observations.remove(observation)
                                }
                            },
                            onObservationClick = { observation ->
                                selectedObservation.value = observation
                            }
                        )

                    } else if (showMapScreen.value) {

                        MapScreen(
                            observations = observations,
                            onBackClick = {
                                showMapScreen.value = false
                            },
                            onObservationClick = { observation ->
                                selectedObservation.value = observation
                            }
                        )

                    } else if (showProfileScreen.value) {

                        ProfileScreen(
                            observationsCount = observations.size,

                            speciesCount = observations
                                .map {
                                    it.speciesName
                                        .trim()
                                        .lowercase()
                                }
                                .filter {
                                    it.isNotEmpty()
                                }
                                .distinct()
                                .size,

                            locationCount = observations
                                .map { observation ->

                                    if (observation.location.isNotBlank()) {

                                        observation.location
                                            .trim()
                                            .lowercase()

                                    } else if (
                                        observation.latitude != null &&
                                        observation.longitude != null
                                    ) {

                                        "${observation.latitude},${observation.longitude}"

                                    } else {

                                        ""
                                    }
                                }
                                .filter {
                                    it.isNotEmpty()
                                }
                                .distinct()
                                .size,

                            onBackClick = {
                                showProfileScreen.value = false
                            }
                        )

                    } else {

                        HomeScreen(
                            onRegisterClick = {
                                showRegisterScreen.value = true
                            },
                            onObservationsClick = {
                                showObservationsScreen.value = true
                            },
                            onMapClick = {
                                showMapScreen.value = true
                            },
                            onProfileClick = {
                                showProfileScreen.value = true
                            },

                            lastObservation = observations.lastOrNull(),

                            speciesCount = observations
                                .map {
                                    it.speciesName
                                        .trim()
                                        .lowercase()
                                }
                                .filter {
                                    it.isNotEmpty()
                                }
                                .distinct()
                                .size,

                            locationCount = observations
                                .map { observation ->

                                    if (observation.location.isNotBlank()) {

                                        observation.location
                                            .trim()
                                            .lowercase()

                                    } else if (
                                        observation.latitude != null &&
                                        observation.longitude != null
                                    ) {

                                        "${observation.latitude},${observation.longitude}"

                                    } else {

                                        ""
                                    }
                                }
                                .filter {
                                    it.isNotEmpty()
                                }
                                .distinct()
                                .size
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier
) {

    Text(
        text = "BioWatch",
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    BioWatchTheme {
        Greeting("Android")
    }
}