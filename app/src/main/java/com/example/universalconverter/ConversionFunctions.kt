package com.example.universalconverter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// ------------------------------Fonctions de conversion--------------------------------------------------------------------

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