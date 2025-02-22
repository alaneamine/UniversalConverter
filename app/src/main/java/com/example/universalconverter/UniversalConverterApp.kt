package com.example.universalconverter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

//-------------------------------- Interface utilisateur --------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniversalConverterApp() {
    var selectedConversion by remember { mutableStateOf("Length") }
    var inputValue by remember { mutableStateOf("") }
    var resultValue by remember { mutableStateOf("") }
    var selectedUnitFrom by remember { mutableStateOf("") }
    var selectedUnitTo by remember { mutableStateOf("") }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F4F6))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Universal Converter",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ExposedDropdownMenuBox(selectedConversion, listOf("Length", "Weight", "Temperature", "Money")) { selectedConversion = it }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            label = { Text("Enter Value") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ExposedDropdownMenuBox(selectedUnitFrom, getUnitsForConversion(selectedConversion)) { selectedUnitFrom = it }
                            Text("→", fontSize = 24.sp)
                            ExposedDropdownMenuBox(selectedUnitTo, getUnitsForConversion(selectedConversion)) { selectedUnitTo = it }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val coroutineScope = rememberCoroutineScope()  // Coroutine scope

                Button(
                    onClick = {
                        coroutineScope.launch {
                            resultValue = when (selectedConversion) {
                                "Length" -> lengthConverter(
                                    inputValue.toDoubleOrNull() ?: 0.0,
                                    selectedUnitFrom,
                                    selectedUnitTo
                                )

                                "Weight" -> weightConverter(
                                    inputValue.toDoubleOrNull() ?: 0.0,
                                    selectedUnitFrom,
                                    selectedUnitTo
                                )

                                "Temperature" -> temperatureConverter(
                                    inputValue.toDoubleOrNull() ?: 0.0,
                                    selectedUnitFrom,
                                    selectedUnitTo
                                )

                                "Money" -> moneyConverter(
                                    inputValue.toDoubleOrNull() ?: 0.0,
                                    selectedUnitFrom,
                                    selectedUnitTo
                                )

                                else -> "Invalid conversion"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5784BA))
                ) {
                    Text("Convert", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (resultValue.isNotEmpty()) {
                    Text(resultValue, fontSize = 18.sp, color = Color(0xFF4CAF50))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBox(selected: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(if (selected.isEmpty()) "Select" else selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    onItemSelected(item)
                    expanded = false
                })
            }
        }
    }
}

fun getUnitsForConversion(conversionType: String): List<String> {
    return when (conversionType) {
        "Length" -> listOf("Meters", "Kilometers", "Miles", "Yards")
        "Weight" -> listOf("Kilograms", "Grams", "Pounds", "Ounces")
        "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
        "Money" -> listOf("USD", "AED","AFN", "ALL","AMD", "ANG", "AOA", "ARS",
            "AUD",        "AWG",        "AZN",        "BAM",        "BBD",        "BDT",        "BGN",        "BHD",
            "BIF",        "BMD",        "BND",        "BOB",        "BRL",        "BSD",        "BTN",        "BWP",
            "BYN",        "BZD",        "CAD",        "CDF",        "CHF",        "CLP",        "CNY",        "COP",
            "CRC",        "CUP",        "CVE",        "CZK",        "DJF",        "DKK",        "DOP",        "DZD",
            "EGP",        "ERN",        "ETB",        "EUR",        "FJD",        "FKP",        "FOK",        "GBP",
            "GEL",        "GGP",        "GHS",        "GIP",        "GMD",        "GNF",        "GTQ",        "GYD",
            "HKD",        "HNL",        "HRK",        "HTG",        "HUF",        "IDR",        "ILS",        "IMP",
            "INR",        "IQD",        "IRR",        "ISK",        "JEP",        "JMD",        "JOD",        "JPY",
            "KES",        "KGS",        "KHR",        "KID",        "KMF",        "KRW",        "KWD",        "KYD",
            "KZT",        "LAK",        "LBP",        "LKR",        "LRD",        "LSL",        "LYD",        "MAD",
            "MDL",        "MGA",        "MKD",        "MMK",        "MNT",        "MOP",        "MRU",        "MUR",
            "MVR",        "MWK",        "MXN",        "MYR",        "MZN",        "NAD", "NGN",        "NIO",        "NOK",
            "NPR",        "NZD",        "OMR",        "PAB",        "PEN",        "PGK",        "PHP",        "PKR",
            "PLN",        "PYG",        "QAR",        "RON",        "RSD",        "RUB",        "RWF",        "SAR",
            "SBD",        "SCR",        "SDG",        "SEK",        "SGD",        "SHP",        "SLE",        "SLL",
            "SOS",        "SRD",        "SSP",        "STN",        "SYP",        "SZL",        "THB",        "TJS",
            "TMT",        "TND",        "TOP",        "TRY",        "TTD",        "TVD",        "TWD",        "TZS",
            "UAH",        "UGX",        "UYU",        "UZS",        "VES",        "VND",        "VUV",        "WST",
            "XAF",        "XCD",       "XDR",        "XOF",        "XPF",        "YER",        "ZAR",        "ZMW", "ZWL")
        else -> emptyList()
    }
}
