package com.example.universalconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UniversalConverterApp()
        }
    }
}

@Composable
fun UniversalConverterApp() {
    var selectedConversion by remember { mutableStateOf("Length") }
    var inputValue by remember { mutableStateOf("") }
    var resultValue by remember { mutableStateOf("") }
    var selectedUnitFrom by remember { mutableStateOf("") }
    var selectedUnitTo by remember { mutableStateOf("") }
    var conversionSuccess by remember { mutableStateOf(false) }
    var conversionError by remember { mutableStateOf(false) }

    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F6CF)) // Couleur de fond de toute la page
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF7F6CF)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Universal Converter", style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold), color = Color(0xFF6200EE))

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown pour le type de conversion
            val conversionTypes = listOf("Length", "Weight", "Temperature", "Money")
            DropdownMenu(selectedConversion, conversionTypes) { selectedConversion = it }

            Spacer(modifier = Modifier.height(16.dp))

            // Champ de texte pour la valeur d'entrée
            TextField(
                value = inputValue,
                onValueChange = { inputValue = it },
                label = { Text("Enter Value") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown pour les unités côte à côte
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownMenu(selectedUnitFrom, getUnitsForConversion(selectedConversion)) { selectedUnitFrom = it }
                Spacer(modifier = Modifier.width(16.dp))
                Text("→", style = MaterialTheme.typography.h4)
                Spacer(modifier = Modifier.width(16.dp))
                DropdownMenu(selectedUnitTo, getUnitsForConversion(selectedConversion)) { selectedUnitTo = it }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bouton de conversion
            Button(
                onClick = {
                    resultValue = when (selectedConversion) {
                        "Length" -> lengthConverter(inputValue.toDoubleOrNull() ?: 0.0, selectedUnitFrom, selectedUnitTo)
                        "Weight" -> weightConverter(inputValue.toDoubleOrNull() ?: 0.0, selectedUnitFrom, selectedUnitTo)
                        "Temperature" -> temperatureConverter(inputValue.toDoubleOrNull() ?: 0.0, selectedUnitFrom, selectedUnitTo)
                        "Money" -> moneyConverter(inputValue.toDoubleOrNull() ?: 0.0, selectedUnitFrom, selectedUnitTo)
                        else -> "Invalid conversion"
                    }
                    conversionSuccess = resultValue.isNotEmpty()
                    conversionError = resultValue.isEmpty()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5784BA))
            ) {
                Text("Convert", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.ArrowForward, contentDescription = "Convert")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Affichage du résultat if (conversionSuccess) {
            Text("Result: $resultValue", style = MaterialTheme.typography.h6, color = Color(0xFF4CAF50))

        Spacer(modifier = Modifier.height(380.dp))

        // Section de crédits
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color(0xFF5784BA),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Credits", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Developed by Amine, Yanis & Saad", fontSize = 16.sp)
                Text("©2025 UniversalConverter@estin.dz", fontSize = 14.sp)
            }
        }
    }
}
}

@Composable
fun DropdownMenu(selected: String, items: List<String>, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        TextButton(onClick = { expanded = true }) {
            Text(if (selected.isEmpty()) "Select Unit" else selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(item)
                }
            }
        }
    }
}

// Fonction pour obtenir les unités en fonction du type de conversion
fun getUnitsForConversion(conversionType: String): List<String> {
    return when (conversionType) {
        "Length" -> listOf("Meters", "Kilometers", "Centimeters", "Millimeters", "Micrometers", "Nanometers", "Miles", "Yards", "Feet", "Inches")
        "Weight" -> listOf("Kilograms", "Grams", "Milligrams", "Micrograms", "Tonnes", "Pounds", "Ounces", "Stones")
        "Temperature" -> listOf("Celsius", "Fahrenheit", "Kelvin")
        "Money" -> listOf("USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "DZD")
        else -> emptyList()
    }
}

// Fonctions de conversion

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

fun moneyConverter(value: Double, fromUnit: String, toUnit: String): String {
    // Exemple de conversion de devises, les taux de change doivent être mis à jour
    val exchangeRates = mapOf(
        "USD" to 1.0,
        "EUR" to 0.85,
        "GBP" to 0.75,
        "JPY" to 110.0,
        "AUD" to 1.35,
        "CAD" to 1.25,
        "CHF" to 0.92,
        "CNY" to 6.45,
        "SEK" to 8.5,
        "DZD" to 130.0
    )

    val baseValue = value / (exchangeRates[fromUnit] ?: 1.0)
    return (baseValue * (exchangeRates[toUnit] ?: 1.0)).toString()
}