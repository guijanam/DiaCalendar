package com.sonbum.diacalendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sonbum.diacalendar.shared.displayText

import java.time.YearMonth

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    goToDay: () -> Unit,
    goSetting: () -> Unit,
) {
    Row(
        modifier = modifier.height(34.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.baseline_settings_backup_restore_24),
            contentDescription = "GoToday",
            onClick = goToDay,

        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Medium,
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = "휴일근무",
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = "휴무",
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Medium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        CalendarNavigationIcon(
            icon = painterResource(id = R.drawable.twotone_settings_24),
            contentDescription = "Setting",
            onClick = goSetting,
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        painter = icon,
        contentDescription = contentDescription,
    )
}
