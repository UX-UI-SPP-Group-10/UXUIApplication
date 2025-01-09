import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DatePicker(onDateSelect: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    var selectedDay by remember { mutableStateOf(currentDay) }
    var selectedMonth by remember { mutableStateOf(currentMonth) }
    var selectedYear by remember { mutableStateOf(currentYear) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Pick a Date", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Day Picker
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(31) { day ->
                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            selectedDay = day + 1
                            onDateSelect(formatDate(selectedDay, selectedMonth, selectedYear))
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (day + 1 == selectedDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text((day + 1).toString(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Month Picker
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val months = listOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )
            items(months.size) { index ->
                Card(
                    modifier = Modifier
                        .size(60.dp, 40.dp)
                        .clickable {
                            selectedMonth = index
                            onDateSelect(formatDate(selectedDay, selectedMonth, selectedYear))
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (index == selectedMonth) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(months[index], style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Year Picker
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(21) { offset ->
                val year = currentYear - 10 + offset
                Card(
                    modifier = Modifier
                        .size(60.dp, 40.dp)
                        .clickable {
                            selectedYear = year
                            onDateSelect(formatDate(selectedDay, selectedMonth, selectedYear))
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (year == selectedYear) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(year.toString(), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

// Helper function to format the date as "MMM dd"
fun formatDate(day: Int, month: Int, year: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, day)
    return SimpleDateFormat("MMM dd", Locale.getDefault()).format(calendar.time)
}
