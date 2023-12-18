package com.sonbum.diacalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean,
    colors: List<Color> = emptyList(),
    isToday: Boolean,
    onClick: (CalendarDay) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(
                width = 0.3.dp,
                color = Color.LightGray,
                shape = RectangleShape
            )
            .padding(all = 0.dp)
            .background(
                color = when {
                    isSelected -> colorResource(R.color.example_1_selection_color)
                    isToday -> colorResource(id = R.color.example_5_text_grey_light)
                    else -> Color.Transparent
                },
            )
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),
        contentAlignment = Alignment.TopStart,//날짜 위치
    ) {
        //날짜 토,일 색깔 바꾸기
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) colorResource(R.color.white) else colorResource(R.color.black)
            DayPosition.InDate, DayPosition.OutDate -> colorResource(R.color.example_5_text_grey_light)



        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
        )
        Column(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            for (color in colors) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(color),
                )
            }
            DiaView()
            MemoView()
        }
    }
}

@Composable
fun DiaView(

) {

    Text(
        text = "Dia",
        color = Color.Blue,
    )
}

@Composable
fun MemoView() {
    Text(
        text = "Memo",
        color = Color.Green,
    )
}
