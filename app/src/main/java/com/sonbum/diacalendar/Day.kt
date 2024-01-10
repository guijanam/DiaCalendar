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
import androidx.compose.ui.text.font.FontWeight
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
    isToday: Boolean,
    onClick: (CalendarDay) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .border(
                width = 0.2.dp,
                color = colorResource(R.color.calendarBorder_color),
                shape = RectangleShape
            )
            .padding(all = 0.dp)
            .background(
                color = Color.Transparent,
//                when {
//                    isSelected -> colorResource(R.color.example_1_selection_color)
//                    isToday -> colorResource(id = R.color.today_backgroundColor)
//                    else -> Color.Transparent
//                },
            )
            // Disable clicks on inDates/outDates
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                showRipple = !isSelected,
                onClick = { onClick(day) },
            ),
        verticalArrangement = Arrangement.Top,//날짜 위치
    ) {


        val textColor = when (day.date.dayOfWeek) {
            DayOfWeek.SUNDAY -> colorResource(id = R.color.red)
            DayOfWeek.SATURDAY -> colorResource(id = R.color.blue)
            else -> colorResource(id = R.color.black)
        }
        if (day.position == DayPosition.MonthDate) {

            Row (
                Modifier
                    .fillMaxWidth()
                    .padding(all = 0.dp)
                    .background(
                        color = when {
                            isToday -> colorResource(id = R.color.today_backgroundColor)
                            else -> colorResource(id = R.color.dateBack_color)
                        },
                        )
            )
            {
                //날짜
                Text(
                    text = day.date.dayOfMonth.toString(),
                    color = textColor,
                    fontSize = 14.sp,
                )

//                Text(
//                    text = "공휴일",
//                    color = Color.Red,
//                    fontSize = 10.sp,
//                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(22.dp)
                    .padding(top = 1.dp, end = 0.dp),
                    //.background(Color.Yellow),
                contentAlignment = Alignment.TopCenter

            ) {
                DiaView(userDateAndTurn)
            }

//            Text(
//                text = "구글연동",
//                color = Color.Black,
//            )
//            MemoView()

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
        text = turnValue,
        color = colorResource(id = textColor(turnValue)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,

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
