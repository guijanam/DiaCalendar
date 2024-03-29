package com.sonbum.diacalendar.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors

import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonbum.diacalendar.Day
import com.sonbum.diacalendar.SimpleCalendarTitle
import com.sonbum.diacalendar.rememberFirstVisibleMonthAfterScroll
import com.sonbum.diacalendar.shared.displayText
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
import com.kizitonwose.calendar.core.yearMonth
import com.sonbum.diacalendar.R
import com.sonbum.diacalendar.Realm.UserDateAndTurnListEntity
import com.sonbum.diacalendar.ViewModels.CalendarVM
import com.sonbum.diacalendar.ViewModels.WorkSettingVM
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(horizontal: Boolean = true,
                   workSettingVM : WorkSettingVM,
                   calendarVM: CalendarVM) {
    val today = remember { LocalDate.now() }
    val currentMonth = remember(today) { today.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val selections = remember { mutableStateListOf<CalendarDay>() }
    val daysOfWeek = remember { daysOfWeek() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    //var openBottomSheet by remember { mutableStateOf(false) }
    var selectedUserDateAndTurn by remember { mutableStateOf<UserDateAndTurnListEntity?>(null) }
    var userDateAndTurnListEntities = calendarVM.currentUserDateAndTurnList.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(

            )
            {
                Text("사용자 설정",
                    modifier = Modifier
                        .padding(10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )

                Divider()

                Text("승무소",
                    modifier = Modifier
                        .padding(15.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )
                DropdownCompany(workSettingVM)

                Divider()

                Text("오늘근무",
                    modifier = Modifier
                        .padding(15.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp
                )

                DropdownDiaselect(workSettingVM)

                Divider()
            }
        },
    ) {
        Scaffold(
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
                        .background(Color.LightGray)//상단 년도 배경색
                        .padding(top = 1.dp),
                ) {

                    // TODO: - 여기가 실행되어야만 다이아가 보임
                    userDateAndTurnListEntities.value.count()

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
                            modifier = Modifier.padding(all = 0.dp),
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

                                //day.date

                                val currentUserDateAndTurn = calendarVM.findUserDateAndTurn(day.date)

                                Day(
                                    day = day,
                                    userDateAndTurn = currentUserDateAndTurn,
                                    isSelected = selections.contains(day),
                                    isToday = day.position == DayPosition.MonthDate && day.date == today,
                                ) { clicked ->
//                                    openBottomSheet = true
                                    selectedUserDateAndTurn = currentUserDateAndTurn

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
            if(selectedUserDateAndTurn != null) {
                ModalBottomSheet(
                    modifier = Modifier,
                    tonalElevation = 10.dp,
                    onDismissRequest = {
                    selectedUserDateAndTurn = null},


                    sheetState = sheetState
                ) {

                    selectedUserDateAndTurn?.let {
                        SubScreen(it)
                    }

                    Button(onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                selectedUserDateAndTurn = null
                            }
                        }
                    }) {

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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownCompany(workSettingVM: WorkSettingVM) {

    val companytype = workSettingVM.fetchedCompanyNamesFlow.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val selectedOptionText = workSettingVM.selectedCompanyName.collectAsState()

    ExposedDropdownMenuBox(
        expanded = expanded ,
        onExpandedChange = {expanded = !expanded})
    {
        TextField(
            value = selectedOptionText.value,
            onValueChange = {
                workSettingVM.companySelected(it)
            },
            readOnly = true,
            //label = { Text(text = "소속승무소")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            companytype.value.forEach { selectedText ->
                DropdownMenuItem(onClick = {
                    workSettingVM.companySelected(selectedText)
                    expanded = false
                }) {
                    Text(text = selectedText)

                }
            }

        }


    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropdownDiaselect(workSettingVM: WorkSettingVM) {
    //선택한 승무소 diaselect 변수 만들기
    val diaselects = workSettingVM.fetcheddiaSelectListStateFlow.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val selectedOptionText = workSettingVM.selectedDia.collectAsState()

    ExposedDropdownMenuBox(
        expanded = expanded ,
        onExpandedChange = {expanded = !expanded})
    {
        TextField(
            value = selectedOptionText.value,
            onValueChange = {
                workSettingVM.diaSelected(it)
            },
            readOnly = true,
            //label = { Text(text = "오늘 근무")},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)},
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false })
        {
            diaselects.value.forEach { selectedText ->
                DropdownMenuItem(onClick = {
                    workSettingVM.diaSelected(selectedText)
                    expanded = false
                }) {
                    Text(text = selectedText)

                }
            }

        }


    }
}