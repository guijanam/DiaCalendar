package com.sonbum.diacalendar.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonbum.diacalendar.R
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import com.sonbum.diacalendar.ui.theme.DiaCalendarTheme


@Composable
fun SubScreen(selectedUserDateAndTurn: UserDateAndTurnListEntity) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ElevatedCard(
            modifier = Modifier
                .size(width = 400.dp, height = 250.dp),

//                .fillMaxWidth()
//                .height(250.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),



        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center

                ) {

                    Text(
                        text = "01월26일(금)",
                        fontSize = 30.sp,
                    )
                    Spacer(modifier = Modifier.width(100.dp))
                    Text(
                        text = "04:05",
                        fontSize = 20.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = "${selectedUserDateAndTurn.turn}",
                        fontSize = 30.sp,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "평휴",
                        fontSize = 20.sp,
                    )
                    Spacer(modifier = Modifier.width(50.dp))
                    Text(
                        text = "원래근무:43",
                        fontSize = 25.sp,
                    )
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ){
                    Text(
                        text = "출근 ",
                        fontSize = 30.sp,
                    )
                    Text(
                        text = "17:02",
                        fontSize = 30.sp,
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = "전반 ",
                        fontSize = 30.sp,
                    )
                    Text(
                        text = "17:02#20:24",
                        fontSize = 30.sp,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "2362",
                        fontSize = 20.sp,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                ) {
                    Text(
                        text = "후반 ",
                        fontSize = 30.sp,
                    )
                    Text(
                        text = "17:02-07:24",
                        fontSize = 30.sp,
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = "1924",
                        fontSize = 20.sp,
                    )
                }

            }
        }

    }


}
//
//@Preview
//@Composable
//fun MainScreenPreview() {
//    SubScreen()
//}
