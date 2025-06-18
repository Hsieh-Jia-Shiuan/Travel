package com.example.travel.view.flight

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.travel.model.entities.flight.FlightSchedule
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

    LaunchedEffect(Unit) {
        viewModel.fetchFlightSchedules(
            airFlyLine = AirFlyLine.International,
            airFlyIO = AirFlyIO.Departure
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val currentResult = flightSchedulesResult // 獲取當前觀察到的值

        if (currentResult == null) {
            // 如果 currentResult 為 null (通常是 LiveData 初始值或還沒有數據)，顯示等待訊息
            Text("等待加載航班資料...")
        } else {
            // 現在 currentResult 已經被智能推斷為非 null 的 NetworkResult<InstantScheduleResponse>
            when (currentResult) {
                is NetworkResult.Loading -> { // 這裡不再需要 <InstantScheduleResponse>，因為 `NetworkResult.Loading` 已經是 `NetworkResult<T>` 的子類，且 `currentResult` 類型已知
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                    Text("加載中...", modifier = Modifier.padding(top = 16.dp))
                }

                is NetworkResult.Success -> {
                    // 現在 currentResult 被智能推斷為 NetworkResult.Success<InstantScheduleResponse>
                    val schedules = currentResult.data?.instantSchedule
                    if (schedules != null && schedules.isNotEmpty()) {
                        FlightList(schedules = schedules)
                    } else {
                        Text("沒有找到航班資料。", modifier = Modifier.padding(16.dp))
                    }
                }

                is NetworkResult.Error -> {
                    // 現在 currentResult 被智能推斷為 NetworkResult.Error<InstantScheduleResponse>
                    val errorMessage = currentResult.message
                    Text(
                        "錯誤: ${errorMessage ?: "未知錯誤"}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                    Log.e("FlightPage", "加載航班資料錯誤: $errorMessage")
                    Toast.makeText(
                        context,
                        "加載失敗: ${errorMessage ?: "未知錯誤"}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                // 不需要 null 分支了，因為 null 已經在外面處理
            }
        }
    }
}

@Composable
fun FlightList(schedules: List<FlightSchedule>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp) // 項目之間間距
    ) {
        items(schedules) { flight ->
            FlightScheduleCard(flight = flight) // 為每個航班數據創建一個卡片 Composable
        }
    }
}

// 您可能需要為每個航班項目創建一個單獨的 Composable，例如 FlightScheduleCard
@Composable
fun FlightScheduleCard(flight: FlightSchedule) {
    // 這是一個簡化的卡片顯示，您可以根據 item_flight_schedule.xml 的設計來創建更複雜的 Composable
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${flight.airLineName} - ${flight.airLineNum}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "起飛機場: ${flight.upAirportName}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "預計時間: ${flight.expectTime}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(text = "實際時間: ${flight.realTime}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "航班狀態: ${flight.airFlyStatus}",
                style = MaterialTheme.typography.bodySmall
            )
            // 您可以根據需要添加更多資料，如 airBoardingGate, airPlaneType 等
        }
    }
}