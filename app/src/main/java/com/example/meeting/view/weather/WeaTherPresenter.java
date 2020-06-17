package com.example.meeting.view.weather;

import android.app.AlertDialog;
import android.location.Location;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.applandeo.materialcalendarview.EventDay;
import com.example.meeting.AppContainer;
import com.example.meeting.L;
import com.example.meeting.data.AppDataManger;
import com.example.meeting.data.model.Alarm;
import com.example.meeting.model.Scheduler;
import com.example.meeting.model.Weather;
import com.example.meeting.network.DataCallback;
import com.example.meeting.network.LatXLngY;
import com.example.meeting.network.LocationUtils;
import com.example.meeting.network.VolleyResult;
import com.example.meeting.network.VolleyService;
import com.example.meeting.utils.AlarmUtils;
import com.example.meeting.utils.LocationProvider;
import com.example.meeting.utils.rx.RXHelper;
import com.example.meeting.utils.rx.RxCall;
import com.example.meeting.utils.rx.RxTaskCall;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

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

public class WeaTherPresenter implements WeaTherContract.WeaTherPresenter {

    private WeaTherContract.View mView;
    private FirebaseAuth auth;
    private DatabaseReference calendarDatabaseReference;
    private DatabaseReference commonCalendarDatabaseReference;
    private CompositeDisposable compositeDisposable;
    private AppDataManger appDataManager;

    public WeaTherPresenter(WeaTherContract.View view,
                            CompositeDisposable compositeDisposable,
                            AppContainer appContainer) {
        this.mView = view;
        this.compositeDisposable = compositeDisposable;
        this.appDataManager = appContainer.appDataManger;
    }


    @Override
    public void init() {
        mView.showLoading(true);
        new LocationProvider().getLocation(mView.getActivity(), new LocationProvider.LocationResultCallback() {

            @Override
            public void gotLocation(Location location) {
                if (location != null) {

                    final LatXLngY latXLngY = LocationUtils.convertGRID_GPS(location.getLatitude(), location.getLongitude());
                    L.e(":::latXLngY : " + latXLngY.toString());

                    getGeoCorder(location, new DataCallback() {
                        @Override
                        public void success(Object obj) {
                            String address = (String) obj;
                            mView.setAddress(address);
                        }
                    });
                    getCurrentTemp(latXLngY, obj -> {
                        mView.showLoading(false);
                        Weather weather = (Weather) obj;
                        if (weather != null) {
                            mView.setWeatherInfo(weather);
                        }
                    });
                } else {
                    mView.showMessage("다시 시도해 주세요.");
                    mView.showLoading(false);
                }
            }
        });

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
//                        mView.setUserName(result.name);
                    }
                }, error -> {
                    L.e("[getUser] Fail " + error.getMessage());
//                    mView.setUserName("");
                }));
    }


    private void getGeoCorder(Location location, final DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    JSONArray ja = response.getJSONArray("results");
                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject c = ja.getJSONObject(i);
                        String address = c.getString("formatted_address");
                        L.i("::::address " + address);
                        callback.success(address);
                        break;
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {
                L.e(":::Server Internal Error  : " + error.getMessage());
            }
        }, mView.getActivity());
        volleyService.getGeocoder(location);
    }


    private void getCurrentTemp(LatXLngY location, final DataCallback callback) {
        VolleyService volleyService = new VolleyService(new VolleyResult() {
            @Override
            public void notifySuccess(String type, JSONObject response) {
                try {
                    L.e("::::response : " + response);
                    Weather weather = new Weather();
                    JSONObject root = response.getJSONObject("response");
                    JSONObject body = root.getJSONObject("body");
                    JSONObject items = body.getJSONObject("items");
                    JSONArray jsonArray = items.getJSONArray("item");
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject weatherJson = jsonArray.getJSONObject(i);
                            if (weatherJson.has("category")) {
                                String category = weatherJson.getString("category");
                                String baseDate = weatherJson.getString("baseDate");
                                switch (category) {
                                    case "PTY":
                                        weather.setPTY(weatherJson.getString("obsrValue"));
                                        break;
                                    case "REH":
                                        weather.setREH(weatherJson.getString("obsrValue"));
                                        break;
                                    case "RN1":
                                        weather.setRN1(weatherJson.getString("obsrValue"));
                                        break;
                                    case "T1H":
                                        weather.setT1H(weatherJson.getString("obsrValue"));
                                        break;
                                    case "UUU":
                                        weather.setUUU(weatherJson.getString("obsrValue"));
                                        break;
                                    case "VEC":
                                        weather.setVEC(weatherJson.getString("obsrValue"));
                                        break;
                                    case "VVV":
                                        weather.setVVV(weatherJson.getString("obsrValue"));
                                        break;
                                    case "WSD":
                                        weather.setWSD(weatherJson.getString("obsrValue"));
                                        break;
                                }

                            }
                        }
                        callback.success(weather);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void notifyError(VolleyError error) {
                L.e(":::Server Internal Error  : " + error.getMessage());
            }
        }, mView.getActivity());
        volleyService.getCurrentTemp(location);
    }


}
