package com.example.meeting.view.main;

import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.MainActivity;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.utils.AlarmUtils;
import com.example.meeting.utils.DateUtils;
import com.example.meeting.utils.rx.RXHelper;
import com.example.meeting.utils.rx.RxCall;
import com.example.meeting.utils.rx.RxTaskCall;
import com.example.meeting.view.dialog.DetailPopup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.meeting.utils.DateUtils.getDateMils;

public class MainPresenter implements MainContract.MainPresenter {

    private MainContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference calendarDatabaseReference;
    private DatabaseReference commonCalendarDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;

    private SimpleDateFormat mYYMMFormat = new SimpleDateFormat("yyyyMM");
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd");
    private String currentYYMM;
    private String currentYYMMDD;
    public HashMap<Long, List<Scheduler>> schedulerMap = new HashMap<>();
    public HashMap<Long, List<Scheduler>> commonschedulerMap = new HashMap<>();
    List<EventDay> eventDays = new ArrayList<>();
    long currentDateMils;

    public MainPresenter(MainContract.View view,
                         CompositeDisposable compositeDisposable,
                         AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void init() {
        auth = FirebaseAuth.getInstance();
        calendarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Calendars");
        commonCalendarDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Common_Calendars");

        if (auth.getCurrentUser() != null) {
            getUser(auth.getCurrentUser().getUid());
        }

        setTimeInfo(null);

    }

    @Override
    public void getUser(String uid) {
        compositeDisposable.add(appDataManager.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    L.i("[getUser] Success");
                    if (result != null) {
                        L.i(":::call getUser " + result.toString());
                        mView.setUserName(result.name);
                    }
                }, error -> {
                    L.e("[getUser] Fail " + error.getMessage());
                    mView.setUserName("");
                }));
    }

    @Override
    public void logout() {
        compositeDisposable.add(appDataManager.deleteAlarm()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                }, error -> {

                }));
    }

    @Override
    public void setTimeSetting(Calendar cal) {
        mView.showTimeDialog(cal);
    }

    @Override
    public void setTimeInfo(Calendar cal) {
        Calendar calendar;
        if (cal == null) {
            calendar = Calendar.getInstance();
        } else {
            calendar = cal;
        }
        currentYYMM = mYYMMFormat.format(calendar.getTime());
        currentYYMMDD = mDateFormat.format(calendar.getTime());

//        getSchedulerData(auth.getCurrentUser().getUid());
        eventDays.clear();
        getServerSchedulerData(auth.getCurrentUser().getUid());
        getCommonServerSchedulerData(auth.getCurrentUser().getUid());
    }

    @Override
    public void setDetailCalendar(Calendar calendar) {
        currentDateMils = getDateMils(calendar);
        L.i(":::currentDateMils : " + currentDateMils);

        List<Scheduler> list = new ArrayList<>();

        Pair<HashMap<Long, List<Scheduler>>, HashMap<Long, List<Scheduler>>> pair
                = new Pair<>(schedulerMap, commonschedulerMap);
        if (pair.first != null) {
            List<Scheduler> first = pair.first.get(currentDateMils);
            if (first != null) {
                list.addAll(first);
            }
        }
        if (pair.second != null) {
            List<Scheduler> second = pair.second.get(currentDateMils);
            if (second != null) {
                L.i("::::second : " + second.size());
                list.addAll(second);
            }
        }
        L.i("::::: list : " + list.size());

        if (list.size() > 0) {
            StringBuilder builder = new StringBuilder();

            for (Scheduler scheduler : list) {
                builder.append("제목 : ").append(scheduler.getTitle()).append("\n");
                builder.append("할일 : ").append(scheduler.getTodo()).append("\n");
                builder.append("시간 : ").append(DateUtils.getConvertCurTime(scheduler.getExpireTimeMils())).append("\n");
                builder.append("\n");
            }
            new DetailPopup(mView.getActivity())
                    .setContentText(builder.toString())
                    .setConfirmButtonListener((view1, dialog) -> dialog.dismiss()).show();
        }

    }


    @Override
    public void onSubmit(AlertDialog dialog, Alarm alarm, Scheduler scheduler, com.example.meeting.data.model.Calendar calendar) {
//        calendar.seq = Objects.requireNonNull(calendarDatabaseReference.push().getKey());
        L.i("::::calendar : " + calendar.toString());
//        compositeDisposable.add(appDataManager.insertScheduler(calendar)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(() -> {
//                    L.i("[insert calendar] Success");
//                    dialog.dismiss();
//                    cacheAlarmTransactions(alarm);
//                    mView.showMessage("저장 되었습니다 !");
//                }, error -> {
//                    L.e("[calendar] Fail " + error.getMessage());
//                }));

        String seq = Objects.requireNonNull(calendarDatabaseReference.push().getKey());

        if (scheduler.isCommon()) {
            commonCalendarDatabaseReference
                    .child(scheduler.getRootKey())
                    .child(scheduler.getSubkey())
                    .child(seq).setValue(scheduler).addOnCompleteListener(task -> {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    mView.showMessage("단체 일정이 저장 되었습니다 !");
                    refresh();
                } else {
                    mView.showMessage("저장 실패하였습니다 !");
                }
            });
        } else {
            calendarDatabaseReference.child(auth.getCurrentUser().getUid())
                    .child(scheduler.getRootKey())
                    .child(scheduler.getSubkey())
                    .child(seq).setValue(scheduler).addOnCompleteListener(task -> {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    mView.showMessage("저장 되었습니다 !");
                    refresh();
                    cacheAlarmTransactions(alarm);
                } else {
                    mView.showMessage("저장 실패하였습니다 !");
                }
            });
        }

    }

    private void cacheAlarmTransactions(Alarm alarm) {
        compositeDisposable.add(appDataManager.insertAlarm(alarm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    L.i(":::::[DB Alarm Insert Success]");
                    AlarmUtils.addAlarm(mView.getActivity(), alarm);
                }, error -> {
                    L.e("::[Insert Alarm Error] " + error.getMessage());
                }));
    }

    private void getCommonServerSchedulerData(String uid) {
        Query query = commonCalendarDatabaseReference.child(currentYYMM);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mView.showLoading(false);
                    mView.setCalendarSwipeDisable(true);
                    return;
                }

                mView.setCalendarSwipeDisable(false);

                List<Scheduler> list = new ArrayList<>();

                for (DataSnapshot node_child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child : node_child.getChildren()) {
                        Scheduler scheduler = child.getValue(Scheduler.class);
                        list.add(scheduler);
                    }
                }

                filterScheduler(list, true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.showLoading(false);
                mView.setCalendarSwipeDisable(true);
            }
        });


    }


    private void getServerSchedulerData(String uid) {
        Query query = calendarDatabaseReference.child(uid).child(currentYYMM);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    mView.showLoading(false);
                    mView.setCalendarSwipeDisable(true);
                    return;
                }

                mView.setCalendarSwipeDisable(false);

                List<Scheduler> list = new ArrayList<>();

                for (DataSnapshot node_child : dataSnapshot.getChildren()) {
                    for (DataSnapshot child : node_child.getChildren()) {
                        Scheduler scheduler = child.getValue(Scheduler.class);
                        list.add(scheduler);
                    }
                }

                filterScheduler(list, false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.showLoading(false);
                mView.setCalendarSwipeDisable(true);
            }
        });


    }


    private void getSchedulerData(String uid) {
        L.i("::::[getSchedulerData] " + currentYYMM);
        mView.setCalendarSwipeDisable(false);
        compositeDisposable.add(appDataManager.getMonthSchedulers(uid, currentYYMM)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    L.i("[getSchedulerData] Success");
                    if (result != null) {
                        L.i(":::call getSchedulerData " + result.size());
//                        filterScheduler(result);
                    }
                }, error -> {
                    L.e("[getSchedulerData] Fail " + error.getMessage());
                    mView.setCalendarSwipeDisable(true);
                }));
    }

    private void filterScheduler(List<Scheduler> list, boolean isCommon) {
        RXHelper.runOnBackground(new RxTaskCall<List<EventDay>>() {
            @Override
            public List<EventDay> doInBackground() {
                return isCommon ? getCommonEventDays(list, true) : getEventDays(list, false);
            }

            @Override
            public void onResult(List<EventDay> result) {
                RXHelper.delay(150, new RxCall<Long>() {
                    @Override
                    public void onCall(Long data) {
                        mView.showLoading(false);
                        mView.setCalendarEventDay(eventDays);
                        mView.setCalendarSwipeDisable(true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mView.showLoading(false);
                        mView.setCalendarSwipeDisable(true);
                    }
                });
            }
        });
    }

    private List<EventDay> getEventDays(List<Scheduler> result, boolean isCommon) {
        schedulerMap.clear();
//        eventDays.clear();
        for (Scheduler item : result) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getExpireTimeMils());
            long key = getDateMils(calendar);
            List<Scheduler> list = schedulerMap.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(item);
            schedulerMap.put(key, list);
        }

        for (Map.Entry<Long, List<Scheduler>> set : schedulerMap.entrySet()) {
            List<Scheduler> list = schedulerMap.get(set.getKey());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(set.getKey());
            L.e("set.getKey() = " + set.getKey() + " date = " + mDateFormat.format(calendar.getTime())
                    + ", list = " + list.size());
            setEventDay(calendar, list, isCommon);
        }

        return eventDays;
    }


    private List<EventDay> getCommonEventDays(List<Scheduler> result, boolean isCommon) {
        commonschedulerMap.clear();
//        eventDays.clear();
        for (Scheduler item : result) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(item.getExpireTimeMils());
            long key = getDateMils(calendar);
            List<Scheduler> list = commonschedulerMap.get(key);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(item);
            commonschedulerMap.put(key, list);
        }

        for (Map.Entry<Long, List<Scheduler>> set : commonschedulerMap.entrySet()) {
            List<Scheduler> list = commonschedulerMap.get(set.getKey());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(set.getKey());
            L.e("set.getKey() = " + set.getKey() + " date = " + mDateFormat.format(calendar.getTime())
                    + ", list = " + list.size());
            setEventDay(calendar, list, isCommon);
        }

        return eventDays;
    }


    private void setEventDay(Calendar calendar, List<Scheduler> list, boolean isCommon) {
        if (list.size() == 1) {
            eventDays.add(new EventDay(calendar, String.format(list.get(0).getTitle()), isCommon));
        } else if (list.size() == 2) {
            StringBuilder builder = new StringBuilder();
            builder.append(list.get(0).getTitle()).append("\n");
            builder.append(list.get(1).getTitle()).append("\n");
            eventDays.add(new EventDay(calendar, builder.toString(), isCommon));
        } else if (list.size() > 2) {
            int lenth = list.size() - 1;
            StringBuilder builder = new StringBuilder();
            builder.append(list.get(0).getTitle()).append("\n");
            builder.append(list.get(1).getTitle()).append("\n");
            builder.append(String.format("+ %d", lenth - 1)).append("\n");
            eventDays.add(new EventDay(calendar, builder.toString(), isCommon));

        }
    }


    private void refresh() {
        eventDays.clear();
        getServerSchedulerData(auth.getCurrentUser().getUid());
        getCommonServerSchedulerData(auth.getCurrentUser().getUid());
    }


}
