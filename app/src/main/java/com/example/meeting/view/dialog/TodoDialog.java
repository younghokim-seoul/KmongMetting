package com.example.meeting.view.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.meeting.R;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.utils.DisplayUtils;
import com.example.meeting.utils.MessageUtils;
import com.example.meeting.view.widget.OnSingleClickListener;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodoDialog {

    @BindView(R.id.apply_btn)
    Button btnApply;

    @BindView(R.id.et_todo)
    EditText etTodo;

    @BindView(R.id.tv_start_time)
    TextView tvStartTime;

    @BindView(R.id.tv_end_time)
    TextView tvEndTime;


    SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat mYYMMFormat = new SimpleDateFormat("yyyyMM");


    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;
    private LocalDate mSelectedLocalDate;
    private TodoDialog.OnClickListener listener;


    public TodoDialog(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.view_dialog_insert, null);
        ButterKnife.bind(this, inflatedView);


        btnApply.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                hideKeyboard();
                if (mSelectedLocalDate == null) {
                    MessageUtils.showLongToastMsg(context, "시작 날짜를 입력해주세요.");
                    return;
                }

                if (listener != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, mSelectedLocalDate.getYear());
                    calendar.set(Calendar.MONTH, mSelectedLocalDate.getMonthOfYear() - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, mSelectedLocalDate.getDayOfMonth());

                    Alarm alarm = new Alarm();
                    alarm.title = etTodo.getText().toString();
                    alarm.pushAt = mSelectedLocalDate.toString();
                    alarm.pushTimeMils = calendar.getTimeInMillis();

                    Scheduler scheduler = new Scheduler();
                    scheduler.setName(etTodo.getText().toString());
                    scheduler.setExpireTimeMils(calendar.getTimeInMillis());
                    scheduler.setRootKey(mYYMMFormat.format(calendar.getTime()));
                    scheduler.setSubkey(mDateFormat.format(calendar.getTime()));


                    listener.onSubmit(null, dialog, alarm, scheduler);
                }
            }
        });
    }


    @OnClick(R.id.view_start_date_container)
    void onClickDateSetting() {
        Calendar calendar = Calendar.getInstance();


        TimePickerDialog dialog = new TimePickerDialog(context, (view, hourOfDay, minute) -> tvStartTime.setText(hourOfDay + ":" + minute), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

        dialog.show();
    }

    public void show() {
        int defaultSidePadding = (int) DisplayUtils.convertDpToPixel(context, 5);
        this.dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(inflatedView, defaultSidePadding, defaultSidePadding, defaultSidePadding, defaultSidePadding);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if (context != null && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    @OnClick(R.id.deny_btn)
    void onClickedDeny(View view) {
        if (listener != null) {
            listener.onDeny(view, dialog);
        }
    }

    public TodoDialog setDialogButtonClickListener(OnClickListener val) {
        this.listener = val;
        return this;
    }

    public interface OnClickListener {
        void onDeny(View view, AlertDialog dialog);

        void onSubmit(View view, AlertDialog dialog, Alarm alarm, Scheduler scheduler);
    }

    public void hideKeyboard() {
        if (dialog.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) Objects.requireNonNull(context).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
