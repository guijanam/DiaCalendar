package com.sonbum.diacalendar.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.sonbum.diacalendar.NavigationIcon
import com.sonbum.diacalendar.R
import com.sonbum.diacalendar.rememberFirstVisibleWeekAfterScroll
import com.sonbum.diacalendar.shared.displayText
import com.sonbum.diacalendar.shared.getWeekPageTitle

import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WeekViewScreen(close: () -> Unit = {}) {
    val currentDate = remember { LocalDate.now() }
    val startDate = remember { currentDate.minusDays(500) }
    val endDate = remember { currentDate.plusDays(500) }
    var selection by remember { mutableStateOf(currentDate) }
    Row {
        Column(
            modifier = Modifier
                .background(Color.Yellow),
        ) {
            Text(text = "동료이름")

        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
        ) {
            val state = rememberWeekCalendarState(
                startDate = startDate,
                endDate = endDate,
                firstVisibleWeekDate = currentDate,
            )
            val visibleWeek = rememberFirstVisibleWeekAfterScroll(state)
            TopAppBar(
                elevation = 0.dp,
                title = { Text(text = getWeekPageTitle(visibleWeek)) },
//                navigationIcon = {
//                    NavigationIcon(
//                        onBackClick = close
//                    ) },

            )
            WeekCalendar(
                modifier = Modifier
                    .fillMaxHeight()
                    .background(color = colorResource(R.color.example_4_grey)),

                state = state,
                dayContent = { day ->
                    Day(day.date, isSelected = selection == day.date) { clicked ->
                        if (selection != clicked) {
                            selection = clicked
                        }
                    }
                },
            )

        }
    }


}

private val dateFormatter = DateTimeFormatter.ofPattern("dd")

@Composable
private fun Day(
    date: LocalDate,
    isSelected: Boolean,

    onClick: (LocalDate) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onClick(date) }
            .border(
                width = 0.2.dp,
                color = Color.LightGray,
                shape = RectangleShape
            )

    ) {
        Column(
            modifier = Modifier

                .padding(vertical = 10.dp),


            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = date.dayOfWeek.displayText(),
                fontSize = 13.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = dateFormatter.format(date),
                fontSize = 15.sp,
                color =
                if (isSelected) colorResource(R.color.example_7_yellow) else
                    Color.White,
                fontWeight = FontWeight.Bold,
            )
            Column(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),)
            {
                Text(
                    text = "사용자",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
                Text(
                    text = "대12~",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
            }




        }
//        if (isSelected) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    //.height(50.dp)
//                    .background(colorResource(R.color.example_7_yellow))
//                    .align(Alignment.BottomCenter),
//            ){
//                Text(text = "여기요기")
//
//            }
//        }
    }
}

//@Preview
//@Composable
//private fun Example5Preview() {
//    Example5Page()
//}
