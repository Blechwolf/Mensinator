package com.mensinator.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mensinator.app.navigation.displayCutoutExcludingStatusBarsPadding
import org.koin.compose.koinInject
import java.time.LocalDate

@Composable
fun StatisticsScreen() {
    val dbHelper: IPeriodDatabaseHelper = koinInject()
    val calcHelper: ICalculationsHelper = koinInject()
    val ovulationPrediction: IOvulationPrediction = koinInject()
    val periodPrediction: IPeriodPrediction = koinInject()

    val averageCycleLength = calcHelper.averageCycleLength()
    val periodCount = dbHelper.getPeriodCount()
    val ovulationCount = dbHelper.getOvulationCount()
    val averagePeriodLength = calcHelper.averagePeriodLength()
    val avgLutealLength = calcHelper.averageLutealLength()
    val follicleGrowthDays = calcHelper.averageFollicalGrowthInDays()
    val ovulationPredictionDate = ovulationPrediction.getPredictedOvulationDate()
    val periodPredictionDate = periodPrediction.getPredictedPeriodDate()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .displayCutoutExcludingStatusBarsPadding()
            .padding(16.dp)
    ) {
        RowOfText(
            stringResource(id = R.string.period_count),
            periodCount.toString()
        )

        RowOfText(
            stringResource(id = R.string.average_cycle_length),
            (Math.round(averageCycleLength * 10) / 10.0).toString() + " " + stringResource(id = R.string.days)
        )

        RowOfText(
            stringResource(id = R.string.average_period_length),
            (Math.round(averagePeriodLength * 10) / 10.0).toString() + " " + stringResource(id = R.string.days)
        )

        RowOfText(
            if (periodPredictionDate < LocalDate.now()) {
                stringResource(id = R.string.next_period_start_past)
            } else {
                stringResource(id = R.string.next_period_start_future)
            },
            periodPredictionDate.toString()
        )

        RowOfText(
            stringResource(id = R.string.ovulation_count),
            ovulationCount.toString()
        )

        RowOfText(
            stringResource(id = R.string.average_ovulation_day),
            follicleGrowthDays.toString()
        )

        RowOfText(
            stringResource(id = R.string.next_predicted_ovulation),
            //nextPredictedOvulation
            ovulationPredictionDate.toString()
        )

        RowOfText(
            stringResource(id = R.string.average_luteal_length),
            (Math.round(avgLutealLength * 10) / 10.0).toString() + " " + stringResource(id = R.string.days)
        )
    }
}

@Composable
fun RowOfText(stringOne: String, stringTwo: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = stringOne,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringTwo,
            fontSize = 17.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RowOfTextPreview() {
    RowOfText("firstString", "secondstring")
}