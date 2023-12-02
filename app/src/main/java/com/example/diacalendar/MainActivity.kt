package com.example.diacalendar

import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diacalendar.ui.theme.DiaCalendarTheme
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
import com.kizitonwose.calendar.core.previousMonth
import com.kizitonwose.calendar.core.yearMonth
import com.example.diacalendar.shared.displayText

import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiaCalendarTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreenView()
                }
            }
        }
    }
}

@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController) }
    ) {
        Box(Modifier.padding(it)){
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Calendar,
        BottomNavItem.Worklist,
        BottomNavItem.Analysis,
        BottomNavItem.Settings
    )

    androidx.compose.material.BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color(0xFF3F414E)
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource(id = item.title),
                        modifier = Modifier
                            .width(26.dp)
                            .height(26.dp)
                    )
                },
                label = {
                    Text(
                        stringResource(id = item.title),
                        fontSize = 9.sp
                    )
                },
                selectedContentColor = androidx.compose.material.MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                selected = currentRoute == item.screenRoute,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(
    val title: Int, val icon: Int, val screenRoute: String
) {
    object Calendar : BottomNavItem(R.string.text_calendar, R.drawable.ic_calendar, CALENDAR)
    object Worklist : BottomNavItem(R.string.text_worklist, R.drawable.baseline_directions_subway_24, WORKLIST)
    object Analysis : BottomNavItem(R.string.text_analysis, R.drawable.ic_clipbord, ANALYSIS)
    object Settings : BottomNavItem(R.string.text_settings, R.drawable.ic_settings, SETTINGS)
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.Calendar.screenRoute) {
        composable(BottomNavItem.Calendar.screenRoute) {
            CalendarScreen()
        }
        composable(BottomNavItem.Worklist.screenRoute) {
            WorkListScreen()
        }
        composable(BottomNavItem.Analysis.screenRoute) {
            AnalysisScreen()
        }
        composable(BottomNavItem.Settings.screenRoute) {
            SettingsScreen()
        }
    }
}


@Composable
fun CalendarScreen(horizontal: Boolean = true) {
    val today = remember { LocalDate.now() }
    val currentMonth = remember(today) { today.yearMonth }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    val selections = remember { mutableStateListOf<CalendarDay>() }
    val daysOfWeek = remember { daysOfWeek() }

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
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
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
//                monthFooter = { month ->
//                    val count = month.weekDays.flatten()
//                        .count { selections.contains(it) }
//                    MonthFooter(selectionCount = count)
//                },
            )
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
//            monthFooter = monthFooter,
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
//            monthFooter = monthFooter,
        )
    }
}

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
                fontSize = 12.sp,
                text = dayOfWeek.displayText(),
            )
        }
    }
}
//

//@Composable
//private fun MonthFooter(selectionCount: Int) {
//    Box(
//        Modifier
//            .fillMaxWidth()
//            .testTag("MonthFooter")
//            .background(colorResource(id = R.color.orange_800))
//            .padding(vertical = 10.dp),
//        contentAlignment = Alignment.Center,
//    ) {
//        val text = if (selectionCount == 0) {
//            stringResource(R.string.example_8_zero_selection)
//        } else {
//            pluralStringResource(R.plurals.example_8_selection, selectionCount, selectionCount)
//        }
//        Text(text = text)
//    }
//}

@Composable
private fun Day(
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
                width = 0.1.dp,
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
        val textColor = when (day.position) {
            // Color.Unspecified will use the default text color from the current theme
            DayPosition.MonthDate -> if (isSelected) colorResource(R.color.white) else colorResource(R.color.black)
            DayPosition.InDate, DayPosition.OutDate -> colorResource(R.color.example_5_text_grey_light)
        }
        Text(
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
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

    androidx.compose.material3.Text(
        text = "Dia",
        color = Color.Blue,
    )
}

@Composable
fun MemoView() {
    androidx.compose.material3.Text(
        text = "Memo",
        color = Color.Green,
    )
}

@Composable
fun WorkListScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material.MaterialTheme.colors.primaryVariant)
    ) {
        Text(
            text = stringResource(id = R.string.text_worklist),
            style = androidx.compose.material.MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AnalysisScreen() {
    var backEnable by remember { mutableStateOf(false) }
    var webView : WebView? = null

    AndroidView(
        modifier = Modifier,
        factory = {context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        backEnable = view!!.canGoBack()
                    }
                }
                settings.javaScriptEnabled = true
                loadUrl("https://roaring-daifuku-d5d674.netlify.app/")
                webView = this
            }
        }, update = {
            webView = it
        })
    BackHandler(enabled = backEnable) {
        webView?.goBack()
    }



}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material.MaterialTheme.colors.secondaryVariant)
    ) {
        Text(
            text = stringResource(id = R.string.text_settings),
            style = androidx.compose.material.MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiaCalendarTheme {
        MainScreenView()
    }
}