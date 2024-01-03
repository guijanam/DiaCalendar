package com.sonbum.diacalendar.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.magnifier
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sonbum.diacalendar.ui.theme.DiaCalendarTheme


@Composable
fun SubScreen() {
    Column (
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "01월26일(금)")
            Spacer(modifier = Modifier.width(100.dp))
            Text(text = "04:05")

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "43")
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = "평휴")
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = "원래근무:43")
        }
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(text = "출근")
            Text(text = "17:02")
            Text(text = "11:02")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "전반")
            Text(text = "17:02#20:24")
            Spacer(modifier = Modifier.width(100.dp))
            Text(text = "2362")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = "후반")
            Text(text = "17:02-07:24")
            Spacer(modifier = Modifier.width(100.dp))
            Text(text = "1924")
        }
        Text(text = "일정&메모")


        Text(text = "메모제목과 내용")


        Box(
            modifier = Modifier
                .fillMaxSize()
        ){
            Row {
                Text(text = "버튼1")
                Text(text = "버튼2")
                Text(text = "버튼3")
            }
        }

    }






}

@Preview
@Composable
fun MainScreenPreview() {
    SubScreen()
}
