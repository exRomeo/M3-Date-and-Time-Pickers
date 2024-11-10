package com.example.datetimepickers.date_picker_diaog

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
sealed class DateConstraints : SelectableDates {

    protected val epochDay = LocalDate.now()
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant().toEpochMilli()


    companion object {
        private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000
    }

    /**
     * [AllDates] will allow all dates to be selectable
     * */
    data object AllDates : DateConstraints() {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean = true
    }

    /**
     * [FutureDates] will allow only future dates to be selectable
     * @property todayInclusive if true, today will be selectable
     * */
    data class FutureDates(private val todayInclusive: Boolean) : DateConstraints() {

        private val earliestSelectableDate = epochDay + if (todayInclusive) 0 else ONE_DAY_IN_MILLIS

        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= earliestSelectableDate
        }

    }


    /**
     * [PastDates] will allow only past dates to be selectable
     * @property todayInclusive if true, today will be selectable
     * */
    data class PastDates(private val todayInclusive: Boolean) : DateConstraints() {

        private val latestSelectableDate = epochDay + if (todayInclusive) ONE_DAY_IN_MILLIS else 0

        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
            utcTimeMillis <= latestSelectableDate
    }

    /**
     * [DatesAfter] will allow only dates after the given date to be selectable
     * @property date the date after which all dates will be selectable
     * @property inclusive if true, the given date will be selectable
     * */
    data class DatesAfter(private val date: LocalDate, private val inclusive: Boolean = false) :
        DateConstraints() {
        private val earliestSelectableDate =
            date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli() + if (inclusive) 0 else ONE_DAY_IN_MILLIS

        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis >= earliestSelectableDate
    }

    /**
     * [DatesBefore] will allow only dates before the given date to be selectable
     * @property date the date before which all dates will be selectable
     * @property inclusive if true, the given date will be selectable
     * */
    data class DatesBefore(private val date: LocalDate, private val inclusive: Boolean = false) :
        DateConstraints() {
        private val latestSelectableDate =
            date.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli() + if (inclusive) ONE_DAY_IN_MILLIS else 0

        override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= latestSelectableDate
    }


    /**
     * [DatesBetween] will allow only dates between the given dates Range to be selectable
     * @property startDate the start date of the range
     * @property endDate the end date of the range
     * */
    data class DatesBetween(
        private val startDate: LocalDate,
        private val endDate: LocalDate,
    ) : DateConstraints() {
        init {
            require(startDate.isBefore(endDate)) {
                "startDate should be before endDate"
            }
        }

        private val startDateMillis =
            startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()

        private val endDateMillis =
            endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()

        override fun isSelectableDate(utcTimeMillis: Long): Boolean =
            utcTimeMillis in startDateMillis..endDateMillis
    }
}