package com.sonbum.diacalendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.annotations.concurrent.Background
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import java.time.DayOfWeek

@Composable
fun Day(
    day: CalendarDay,
    userDateAndTurn: UserDateAndTurnListEntity?,
    isSelected: Boolean,
    colors: List<Color> = emptyList(),
    isToday: Boolean,
    onClick: (CalendarDay) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(
                width = 0.3.dp,
                color = colorResource(R.color.calendarBorder_color),
                shape = RectangleShape
            )
            .padding(all = 0.dp)
            .background(
                color = when {
                    isSelected -> colorResource(R.color.example_1_selection_color)
                    isToday -> colorResource(id = R.color.today_backgroundColor)
                    else -> Color.Transparent
                },
            )
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),
        //contentAlignment = Alignment.TopStart,//날짜 위치
    ) {
        //날짜 토,일 색깔 바꾸기
//        val textColor = when (day.position) {
//            // Color.Unspecified will use the default text color from the current theme
//            DayPosition.MonthDate -> if (isSelected) colorResource(R.color.white) else colorResource(R.color.black)
//            DayPosition.InDate, DayPosition.OutDate -> colorResource(R.color.outDate_color)
//        }

        val textColor = when (day.date.dayOfWeek) {
            DayOfWeek.SUNDAY -> colorResource(id = R.color.red)
            DayOfWeek.SATURDAY -> colorResource(id = R.color.blue)
            else -> colorResource(id = R.color.black)
        }

        Row (
            Modifier
                .fillMaxWidth()
                .padding(all = 0.dp)
                .background(colorResource(R.color.dateBack_color))
        )
        {
            //날짜
            Text(
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 15.sp,
            )

            Text(
                text = "공휴일",
                color = Color.Red,
                fontSize = 10.sp,
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(all = 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            DiaView(userDateAndTurn)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .padding(all = 0.dp),
            horizontalAlignment = Alignment.Start,
        ) {
//            for (color in colors) {
//                Box(
//                    Modifier
//                        .fillMaxWidth()
//                        .height(5.dp)
//                        .background(color),
//                )
//            }

            Text(
                text = "구글연동",
                color = Color.Black,
            )
            MemoView()
        }
    }
}



@Composable
fun DiaView(userDateAndTurn: UserDateAndTurnListEntity?) {

    val turnValue = userDateAndTurn?.turn ?: ""

    val textColor : (String) -> Int = { turn ->
        if (turn.contains("휴")) {
            R.color.red
        }  else if (turn.contains("~")){
            R.color.gray_off
        } else {
            R.color.black
        }
    }

    Text(
        text = userDateAndTurn?.turn ?: "Dia",
        color = colorResource(id = textColor(turnValue)),
        fontSize = 18.sp
    )
}

@Composable
fun MemoView() {
    Row() {
        ShapeCompose(shape = RectangleShape)
        Text(
            text = "Memo",
            color = Color.Green,
            //Background = Color.LightGray,
        )
    }

}
@Composable
fun ShapeCompose(shape: Shape){
    Box(
        modifier = Modifier
            .size(width = 4.dp, height = 15.dp)
            .background(Color.Red, shape)
    )
}
