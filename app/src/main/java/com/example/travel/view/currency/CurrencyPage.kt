package com.example.travel.view.currency

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.travel.R
import com.example.travel.ui.theme.ProjectColor
import com.example.travel.ui.theme.ProjectTextStyle
import com.example.travel.util.NetworkResult
import com.example.travel.viewModel.CurrencyViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.DecimalFormat

@Composable
fun CurrencyPage(
    viewModel: CurrencyViewModel = koinViewModel<CurrencyViewModel>()
) {
    val currencyListResult by viewModel.currencyList.observeAsState()
    val context = LocalContext.current

    var baseCurrency by remember { mutableStateOf("USD") }
    var baseAmountInput by remember { mutableStateOf("1.00") }

    val reloadData: () -> Unit = {
        viewModel.fetchLatestCurrencies(
            baseCurrency = baseCurrency,
            currencies = "EUR,JPY,USD,CNY,AUD,KRW"
        )
    }

    LaunchedEffect(baseCurrency) {
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
                            baseAmountInput = baseAmountInput,
                            onAmountChange = { newValue ->
                                // Allow only valid decimal numbers
                                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    baseAmountInput = newValue
                                }
                            }
                        )

                        val filteredCurrencies = currencyMap.filterKeys { it != baseCurrency }

                        if (filteredCurrencies.isNotEmpty()) {
                            val parsedBaseAmount = baseAmountInput.toDoubleOrNull() ?: 0.0

                            CurrencyList(
                                currencies = filteredCurrencies,
                                baseAmount = parsedBaseAmount, // Pass the parsed input amount
                                onCurrencyClick = { clickedCurrency ->
                                    baseCurrency = clickedCurrency
                                    // Reset amount to "1.00" when base currency changes
                                    baseAmountInput = "1.00"
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
    baseAmountInput: String,
    onAmountChange: (String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = baseCurrency,
                style = ProjectTextStyle.H5,
                color = ProjectColor.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = baseAmountInput,
                onValueChange = onAmountChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        onAmountChange
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(width = 1.dp, color = ProjectColor.Black5),
                        shape = RoundedCornerShape(8.dp)
                    ),
                placeholder = {
                    Text(
                        text = "100.0",
                        style = ProjectTextStyle.H8,
                        color = ProjectColor.Black50
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.currency_dollar),
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                    )
                },
                trailingIcon = {
                    if (baseAmountInput.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                                .clickable {
                                    onAmountChange("")
                                },
                        )
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@SuppressLint("DefaultLocale")
@Composable
fun CurrencyList(
    currencies: Map<String, Double>,
    baseAmount: Double,
    onCurrencyClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(currencies.entries.toList()) { entry ->
            val calculatedValue = entry.value * baseAmount
            val formatter = DecimalFormat("#.##")

            val formattedValue = if (
                calculatedValue != 0.0 &&
                calculatedValue < 0.01 &&
                calculatedValue > -0.01
            ) {
                String.format("%.6f", calculatedValue)
            } else if (calculatedValue == 0.0) {
                String.format("%.2f", calculatedValue)
            } else {
                formatter.format(calculatedValue)
            }

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
                        text = formattedValue,
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