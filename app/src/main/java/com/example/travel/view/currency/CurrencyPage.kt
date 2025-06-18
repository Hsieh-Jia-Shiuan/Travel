package com.example.travel.view.currency

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.ui.theme.ProjectColor
import com.example.travel.ui.theme.ProjectTextStyle
import com.example.travel.util.NetworkResult
import com.example.travel.viewModel.CurrencyViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CurrencyPage(
    viewModel: CurrencyViewModel = koinViewModel<CurrencyViewModel>()
) {
    val currencyListResult by viewModel.currencyList.observeAsState()
    val context = LocalContext.current

    // State to hold the current base currency
    var baseCurrency by remember { mutableStateOf("USD") }

    val reloadData: () -> Unit = {
        viewModel.fetchLatestCurrencies(baseCurrency = baseCurrency, currencies = null)
    }

    LaunchedEffect(baseCurrency) {
        // Trigger data request when baseCurrency changes or on initial load
        reloadData()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val currentResult = currencyListResult

        if (currentResult == null) {
            Text(
                stringResource(R.string.currency_data_loading)
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
                    val currencyMap = currentResult.data?.data
                    if (currencyMap != null && currencyMap.isNotEmpty()) {
                        val baseCurrencyValue = currencyMap[baseCurrency] ?: 1.0

                        FixedBaseCurrencyDisplay(
                            baseCurrency = baseCurrency,
                            baseCurrencyRate = baseCurrencyValue
                        )

                        val filteredCurrencies = currencyMap.filterKeys { it != baseCurrency }

                        if (filteredCurrencies.isNotEmpty()) {
                            CurrencyList(
                                currencies = filteredCurrencies,
                                onCurrencyClick = { clickedCurrency ->
                                    baseCurrency = clickedCurrency
                                }
                            )
                        } else {
                            Text(
                                stringResource(R.string.currency_data_not_found),
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Text(
                            stringResource(R.string.currency_data_not_found),
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
fun FixedBaseCurrencyDisplay(
    baseCurrency: String,
    baseCurrencyRate: Double
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = baseCurrency,
                style = ProjectTextStyle.H5,
                color = ProjectColor.Black
            )

            Text(
                text = baseCurrencyRate.toString(),
                style = ProjectTextStyle.H5,
                color = ProjectColor.Black
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CurrencyList(
    currencies: Map<String, Double>,
    onCurrencyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(currencies.entries.toList()) { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp)
                    .clickable { onCurrencyClick(entry.key) },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = entry.key,
                        style = ProjectTextStyle.H8,
                        color = ProjectColor.Black
                    )
                    Text(
                        text = entry.value.toString(),
                        style = ProjectTextStyle.H8,
                        color = ProjectColor.Black
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}