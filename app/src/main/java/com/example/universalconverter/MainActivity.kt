package com.example.universalconverter

// Importation des librairies nécessaires
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import kotlinx.coroutines.*

// Application principale
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalConverterApp()
        }
    }
}

// Interface utilisateur

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
                    color = Color(0xFF6200EE))

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

// Fonctions de conversion

// Fonction de conversion de longueur
fun lengthConverter(value: Double, fromUnit: String, toUnit: String): String {
    val meters = when (fromUnit) {
        "Kilometers" -> value * 1000
        "Miles" -> value * 1609.34
        "Yards" -> value * 0.9144
        "Feet" -> value * 0.3048
        "Inches" -> value * 0.0254
        "Centimeters" -> value / 100
        "Millimeters" -> value / 1000
        "Micrometers" -> value / 1_000_000
        "Nanometers" -> value / 1_000_000_000
        else -> value
    }
    return when (toUnit) {
        "Kilometers" -> (meters / 1000).toString()
        "Miles" -> (meters / 1609.34).toString()
        "Yards" -> (meters / 0.9144).toString()
        "Feet" -> (meters / 0.3048).toString()
        "Inches" -> (meters / 0.0254).toString()
        "Centimeters" -> (meters * 100).toString()
        "Millimeters" -> (meters * 1000).toString()
        "Micrometers" -> (meters * 1_000_000).toString()
        "Nanometers" -> (meters * 1_000_000_000).toString()
        else -> meters.toString()
    }
}

//Fonctions de conversion de poids
fun weightConverter(value: Double, fromUnit: String, toUnit: String): String {
    val kilograms = when (fromUnit) {
        "Pounds" -> value * 0.453592
        "Grams" -> value / 1000
        "Milligrams" -> value / 1_000_000
        "Micrograms" -> value / 1_000_000_000
        "Tonnes" -> value * 1000
        "Ounces" -> value * 0.0283495
        "Stones" -> value * 6.35029
        else -> value
    }
    return when (toUnit) {
        "Pounds" -> (kilograms / 0.453592).toString()
        "Grams" -> (kilograms * 1000).toString()
        "Milligrams" -> (kilograms * 1_000_000).toString()
        "Micrograms" -> (kilograms * 1_000_000_000).toString()
        "Tonnes" -> (kilograms / 1000).toString()
        "Ounces" -> (kilograms / 0.0283495).toString()
        "Stones" -> (kilograms / 6.35029).toString()
        else -> kilograms.toString()
    }
}

//Fonctions de conversion de température
fun temperatureConverter(value: Double, fromUnit: String, toUnit: String): String {
    return when {
        fromUnit == "Celsius" && toUnit == "Fahrenheit" -> (value * 9/5 + 32).toString()
        fromUnit == "Fahrenheit" && toUnit == "Celsius" -> ((value - 32) * 5/9).toString()
        fromUnit == "Celsius" && toUnit == "Kelvin" -> (value + 273.15).toString()
        fromUnit == "Kelvin" && toUnit == "Celsius" -> (value - 273.15).toString()
        fromUnit == "Fahrenheit" && toUnit == "Kelvin" -> ((value - 32) * 5/9 + 273.15).toString()
        fromUnit == "Kelvin" && toUnit == "Fahrenheit" -> ((value - 273.15) * 9/5 + 32).toString()
        else -> value.toString()
    }
}

//Fonctions de conversion de monnaie
suspend fun moneyConverter(value: Double, fromUnit: String, toUnit: String): String {
    return try {
        val response = withContext(Dispatchers.IO) { RetrofitInstance.api.getExchangeRates() }
        val rates = response.conversion_rates

        val baseValue = value / (rates[fromUnit] ?: return "Invalid Currency")
        val convertedValue = baseValue * (rates[toUnit] ?: return "Invalid Currency")
        "%.2f".format(convertedValue)
    } catch (e: Exception) {
        "Error fetching rates"
    }
}