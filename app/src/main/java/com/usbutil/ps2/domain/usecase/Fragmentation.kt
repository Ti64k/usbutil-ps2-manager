package com.usbutil.ps2.domain.usecase

import com.usbutil.ps2.domain.model.Ps2Game

enum class FragmentationRisk { NONE, LOW, HIGH }

data class FragmentationReport(
    val risk: FragmentationRisk,
    val fragmentedParts: List<String>,
    val message: String,
)

interface FragmentationAnalyzer {
    suspend fun analyze(game: Ps2Game): FragmentationReport
}
