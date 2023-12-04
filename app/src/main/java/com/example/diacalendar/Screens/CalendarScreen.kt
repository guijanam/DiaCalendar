package com.example.diacalendar.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diacalendar.Day
import com.example.diacalendar.R
import com.example.diacalendar.SimpleCalendarTitle
import com.example.diacalendar.rememberFirstVisibleMonthAfterScroll
import com.example.diacalendar.shared.displayText
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.ContentHeightMode
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.yearMonth
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun CalendarScreen(horizontal: Boolean = true) {
    val today = remember { LocalDate.now() }
    val currentMonth = remember(today) { today.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val selections = remember { mutableStateListOf<CalendarDay>() }
    val daysOfWeek = remember { daysOfWeek() }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("사용자 설정", modifier = Modifier.padding(16.dp))
                Divider()
                NavigationDrawerItem(
                    label = { Text(text = "승무소 선택") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                NavigationDrawerItem(
                    label = { Text(text = "오늘근무 선택") },
                    selected = false,
                    onClick = { /*TODO*/ }
                )
                Divider()

            }
        },
    ) {
        Scaffold(
//            floatingActionButton = {
//                ExtendedFloatingActionButton(
//                    text = { Text("Show drawer") },
//                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
//                    onClick = {
//                        scope.launch {
//                            drawerState.apply {
//                                if (isClosed) open() else close()
//                            }
//                        }
//                    }
//                )
//            }
        ) { contentPadding ->
            // Screen content
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(id = R.color.red_800))//상단 년도 배경색
                        .padding(top = 2.dp),
                ) {
                    val state = rememberCalendarState(
                        startMonth = startMonth,
                        endMonth = endMonth,
                        firstVisibleMonth = currentMonth,
                        firstDayOfWeek = daysOfWeek.first(),
                        outDateStyle = OutDateStyle.EndOfGrid,
                    )
                    val coroutineScope = rememberCoroutineScope()
                    val visibleMonth = rememberFirstVisibleMonthAfterScroll(state)
                    // Draw light content on dark background.
                    CompositionLocalProvider(LocalContentColor provides darkColors().onSurface) {
                        SimpleCalendarTitle(
                            modifier = Modifier.padding(all = 1.dp),
                            currentMonth = visibleMonth.yearMonth,
                            goToDay = {
                                coroutineScope.launch {
                                    state.animateScrollToMonth(currentMonth)
                                }
                            },
                            goSetting = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
//                                coroutineScope.launch {
//                                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
//                                }
                            },
                        )
                        FullScreenCalendar(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(colorResource(id = R.color.white))//달력 배경색
                                .testTag("Calendar"),
                            state = state,
                            horizontal = horizontal,
                            dayContent = { day ->
                                Day(
                                    day = day,
                                    isSelected = selections.contains(day),
                                    isToday = day.position == DayPosition.MonthDate && day.date == today,
                                ) { clicked ->
                                    // 다중선택
                                    if (selections.contains(clicked)) {
                                        selections.remove(clicked)
                                    } else {
                                        selections.add(clicked)
                                    }
                                }
                            },
                            // The month body is only needed for ui test tag.
                            monthBody = { _, content ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("MonthBody"),
                                ) {
                                    content()
                                }
                            },
                            monthHeader = {
                                MonthHeader(daysOfWeek = daysOfWeek)
                            },

                            )
                    }
                }
            }
        }
    }



}

@Composable
private fun FullScreenCalendar(
    modifier: Modifier,
    state: CalendarState,
    horizontal: Boolean,
    dayContent: @Composable() (BoxScope.(CalendarDay) -> Unit),
    monthHeader: @Composable() (ColumnScope.(CalendarMonth) -> Unit),
    monthBody: @Composable() (ColumnScope.(CalendarMonth, content: @Composable () -> Unit) -> Unit),
) {
    if (horizontal) {
        HorizontalCalendar(
            modifier = modifier,
            state = state,
            calendarScrollPaged = true,
            contentHeightMode = ContentHeightMode.Fill,
            dayContent = dayContent,
            monthBody = monthBody,
            monthHeader = monthHeader,

            )
    } else {
        VerticalCalendar(
            modifier = modifier,
            state = state,
            calendarScrollPaged = true,
            contentHeightMode = ContentHeightMode.Fill,
            dayContent = dayContent,
            monthBody = monthBody,
            monthHeader = monthHeader,

            )
    }
}

//요일 표시
@Composable
private fun MonthHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        Modifier
            .fillMaxWidth()
            .testTag("MonthHeader")
            .background(colorResource(id = R.color.example_1_bg_secondary))
            .padding(all = 1.dp),
    ) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                text = dayOfWeek.displayText(),

            )
        }
    }
}