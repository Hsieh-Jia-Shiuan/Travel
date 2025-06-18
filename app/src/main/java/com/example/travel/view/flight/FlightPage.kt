package com.example.travel.view.flight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.model.entities.flight.FlightSchedule
import com.example.travel.ui.theme.ProjectColor
import com.example.travel.ui.theme.ProjectTextStyle
import com.example.travel.util.NetworkResult
import com.example.travel.viewModel.AirFlyIO
import com.example.travel.viewModel.AirFlyLine
import com.example.travel.viewModel.FlightViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun FlightPage(
    viewModel: FlightViewModel = koinViewModel<FlightViewModel>()
) {
    val flightSchedulesResult by viewModel.flightSchedules.observeAsState()
    val context = LocalContext.current

    // Default
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedLineTypeIndex by remember { mutableIntStateOf(0) }

    val currentAirFlyIO = remember(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> AirFlyIO.Departure
            1 -> AirFlyIO.Arrival
            else -> AirFlyIO.Departure
        }
    }

    val currentAirFlyLine = remember(selectedLineTypeIndex) {
        when (selectedLineTypeIndex) {
            0 -> AirFlyLine.International
            1 -> AirFlyLine.Domestic
            else -> AirFlyLine.International
        }
    }

    val reloadData: () -> Unit = {
        viewModel.fetchFlightSchedules(
            airFlyLine = currentAirFlyLine,
            airFlyIO = currentAirFlyIO
        )
    }

    LaunchedEffect(currentAirFlyIO, currentAirFlyLine) {
        reloadData()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            SegmentedButton(
                selected = selectedLineTypeIndex == 0,
                onClick = { selectedLineTypeIndex = 0 },
                shape = ButtonDefaults.outlinedShape,
                label = { Text(stringResource(R.string.tab_international)) }
            )

            Spacer(modifier = Modifier.size(8.dp))

            SegmentedButton(
                selected = selectedLineTypeIndex == 1,
                onClick = { selectedLineTypeIndex = 1 },
                shape = ButtonDefaults.outlinedShape,
                label = { Text(stringResource(R.string.tab_domestic)) }
            )
        }

        Spacer(Modifier.height(8.dp))

        TabRow(selectedTabIndex = selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text(stringResource(R.string.tab_departure)) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text(stringResource(R.string.tab_arrival)) }
            )
        }

        val currentResult = flightSchedulesResult

        if (currentResult == null) {
            Text(
                stringResource(R.string.flight_data_loading)
            )
        } else {
            when (currentResult) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(50.dp)
                    )
                    Text(
                        stringResource(R.string.loading),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is NetworkResult.Success -> {
                    val schedules = currentResult.data?.instantSchedule
                    if (schedules != null && schedules.isNotEmpty()) {
                        FlightList(
                            schedules = schedules,
                            currentAirFlyIO = currentAirFlyIO
                        )
                    } else {
                        Text(
                            stringResource(R.string.flight_data_not_found),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                is NetworkResult.Error -> {
                    val errorMessage = currentResult.message
                    Text(
                        "Error: ${errorMessage ?: "UnKnown error"}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        onClick = reloadData,
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text(stringResource(R.string.reload))
                    }
                }
            }
        }
    }
}

@Composable
fun FlightList(
    schedules: List<FlightSchedule>,
    currentAirFlyIO: AirFlyIO
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(schedules) { flight ->
            FlightScheduleCard(flight = flight, airFlyIO = currentAirFlyIO)
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun FlightScheduleCard(
    flight: FlightSchedule,
    airFlyIO: AirFlyIO,
) {
    val statusColor = when {
        flight.airFlyStatus.contains("延遲") || flight.airFlyStatus.contains("Delayed") -> ProjectColor.Delayed // Assuming ProjectColor.Delayed is defined
        flight.airFlyStatus.contains("取消") || flight.airFlyStatus.contains("Cancelled") -> ProjectColor.Cancelled // Assuming ProjectColor.Cancelled is defined
        flight.airFlyStatus.contains("離站") || flight.airFlyStatus.contains("Departed") -> ProjectColor.Departed // Assuming ProjectColor.Departed is defined
        flight.airFlyStatus.contains("抵達") || flight.airFlyStatus.contains("Arrived") -> ProjectColor.Arrived // Assuming ProjectColor.Arrived is defined
        else -> ProjectColor.Black
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Estimated Time
                    Column {
                        Text(
                            text = stringResource(R.string.flight_data_estimated_time),
                            style = ProjectTextStyle.H8,
                            color = ProjectColor.Black
                        )
                        Text(
                            text = flight.expectTime,
                            style = ProjectTextStyle.H4,
                            color = ProjectColor.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Actual Time
                    Column {
                        Text(
                            text = stringResource(R.string.flight_data_actual_time),
                            style = ProjectTextStyle.H8,
                            color = ProjectColor.Black
                        )
                        Text(
                            text = flight.realTime,
                            style = ProjectTextStyle.H4,
                            color = ProjectColor.Black
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // AirFly Line (Flight Number)
                Text(
                    text = stringResource(R.string.flight_data_flight_number, flight.airLineNum),
                    style = ProjectTextStyle.H8,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Spacer(Modifier.height(8.dp))

                // Terminal and Gate
                Text(
                    text = stringResource(
                        R.string.flight_data_terminal_gate,
                        flight.airBoardingGate
                    ),
                    style = ProjectTextStyle.H8,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = flight.airFlyStatus,
                    style = ProjectTextStyle.H5,
                    color = statusColor,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                // Delay Cause
                if (flight.airFlyDelayCause.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(
                            R.string.flight_data_delay_reason,
                            flight.airFlyDelayCause
                        ),
                        style = ProjectTextStyle.H8,
                        color = ProjectColor.Red,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }


            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val otherAirportCode = if (airFlyIO == AirFlyIO.Departure) {
                    flight.goalAirportCode
                } else {
                    flight.upAirportCode
                }
                val otherAirportName = if (airFlyIO == AirFlyIO.Departure) {
                    flight.goalAirportName
                } else {
                    flight.upAirportName
                }

                Text(
                    text = otherAirportCode ?: "N/A",
                    style = ProjectTextStyle.H6,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = otherAirportName ?: "Unknown Airport",
                    style = ProjectTextStyle.H6,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                Text(
                    text = "|", // Separator
                    style = ProjectTextStyle.H6,
                    color = ProjectColor.Black
                )

                Text(
                    text = stringResource(R.string.current_airport_code),
                    style = ProjectTextStyle.H6,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Text(
                    text = stringResource(R.string.current_airport),
                    style = ProjectTextStyle.H6,
                    color = ProjectColor.Black,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "FlightScheduleCard - Departure")
@Composable
fun PreviewFlightScheduleCardDeparture() {
    MaterialTheme {
        FlightScheduleCard(
            flight = FlightSchedule(
                expectTime = "07:00",
                realTime = "07:05",
                airLineName = "中華航空",
                airLineCode = "CAL",
                airLineLogo = "...",
                airLineUrl = "...",
                airLineNum = "CI123",
                upAirportCode = null,
                upAirportName = null,
                goalAirportCode = "KIX",
                goalAirportName = "關西",
                airPlaneType = "A321",
                airBoardingGate = "01/A9",
                airFlyStatus = "離站Departed",
                airFlyDelayCause = ""
            ),
            airFlyIO = AirFlyIO.Departure,
        )
    }
}

@Preview(showBackground = true, name = "FlightScheduleCard - Arrival")
@Composable
fun PreviewFlightScheduleCardArrival() {
    MaterialTheme {
        FlightScheduleCard(
            flight = FlightSchedule(
                expectTime = "14:30",
                realTime = "14:25",
                airLineName = "長榮航空",
                airLineCode = "EVA",
                airLineLogo = "...",
                airLineUrl = "...",
                airLineNum = "BR789",
                upAirportCode = "SFO",
                upAirportName = "舊金山",
                goalAirportCode = null,
                goalAirportName = null,
                airPlaneType = "B777",
                airBoardingGate = "B5",
                airFlyStatus = "抵達Arrived",
                airFlyDelayCause = "空域流量管制"
            ),
            airFlyIO = AirFlyIO.Arrival,
        )
    }
}

@Preview(showBackground = true, name = "FlightScheduleCard - Delayed Departure")
@Composable
fun PreviewFlightScheduleCardDelayedDeparture() {
    MaterialTheme {
        FlightScheduleCard(
            flight = FlightSchedule(
                expectTime = "09:00",
                realTime = "10:30",
                airLineName = "國泰航空",
                airLineCode = "CPA",
                airLineLogo = "...",
                airLineUrl = "...",
                airLineNum = "CX400",
                upAirportCode = null,
                upAirportName = null,
                goalAirportCode = "HKG",
                goalAirportName = "香港",
                airPlaneType = "A330",
                airBoardingGate = "C12",
                airFlyStatus = "延遲Delayed",
                airFlyDelayCause = "機件維修，預計延誤時間3小時"
            ),
            airFlyIO = AirFlyIO.Departure,
        )
    }
}