package com.applandeo.materialcalendarview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.RestrictTo;
import androidx.annotation.StringRes;

import com.applandeo.materialcalendarview.utils.DateUtils;

import java.util.Calendar;

/**
 * This class represents an event of a day. An instance of this class is returned when user click
 * a day cell. This class can be overridden to make calendar more functional. A list of instances of
 * this class can be passed to CalendarView object using setEvents() method.
 * <p>
 * Created by Mateusz Kornakiewicz on 23.05.2017.
 */

public class EventDay {
    private Calendar mDay;
    private Object mDrawable;
    private String mTitle;
    private int mLabelColor;
    private boolean mIsDisabled;
    private boolean isSetScheduler;

    /**
     * @param day Calendar object which represents a date of the event
     */
    public EventDay(Calendar day) {
        mDay = day;
    }

    /**
     * @param day      Calendar object which represents a date of the event
     * @param drawable Drawable resource which will be displayed in a day cell
     */
    public EventDay(Calendar day, @DrawableRes int drawable) {
        DateUtils.setMidnight(day);
        mDay = day;
        mDrawable = drawable;
    }

    public EventDay(Calendar day, boolean drawable) {
        DateUtils.setMidnight(day);
        mDay = day;
        isSetScheduler = drawable;
    }

    /**
     * @param day      Calendar object which represents a date of the event
     * @param drawable Drawable which will be displayed in a day cell
     */
    public EventDay(Calendar day, Drawable drawable) {
        DateUtils.setMidnight(day);
        mDay = day;
        mDrawable = drawable;
    }

    /**
     * @param day        Calendar object which represents a date of the event
     * @param drawable   Drawable resource which will be displayed in a day cell
     * @param labelColor Color which will be displayed as label text color a day cell
     */
    public EventDay(Calendar day, @DrawableRes int drawable, int labelColor) {
        DateUtils.setMidnight(day);
        mDay = day;
        mDrawable = drawable;
        mLabelColor = labelColor;
    }

    public EventDay(Calendar day, String title) {
        DateUtils.setMidnight(day);
        mDay = day;
        mTitle = title;
        isSetScheduler = true;
    }
    /**
     * @param day        Calendar object which represents a date of the event
     * @param drawable   Drawable which will be displayed in a day cell
     * @param labelColor Color which will be displayed as label text color a day cell
     */
    public EventDay(Calendar day, Drawable drawable, int labelColor) {
        DateUtils.setMidnight(day);
        mDay = day;
        mDrawable = drawable;
        mLabelColor = labelColor;
    }


    /**
     * @return An image resource which will be displayed in the day row
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Object getImageDrawable() {
        return mDrawable;
    }

    /**
     * @return Color which will be displayed as row label text color
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public int getLabelColor() {
        return mLabelColor;
    }


    /**
     * @return Calendar object which represents a date of current event
     */
    public Calendar getCalendar() {
        return mDay;
    }


    public String getTitle() {
        return mTitle;
    }


    /**
     * @return Boolean value if day is not disabled
     */
    public boolean isEnabled() {
        return !mIsDisabled;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void setEnabled(boolean enabled) {
        mIsDisabled = enabled;
    }

    public boolean isSetScheduler() {
        return isSetScheduler;
    }
}
