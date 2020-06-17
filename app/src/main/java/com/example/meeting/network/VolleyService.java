package com.example.meeting.network;

import android.content.Context;
import android.location.Location;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.meeting.L;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class VolleyService {

    private static String KEY_WEATHER_METEOROLOGICAL_API_URL = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/";
    private static String KEY_WEATHER_CURRENT_TEMP_BODY = "ForecastGrib?serviceKey=";
//    private static String KEY_WEATHER_CURRENT_TEMP_BODY = "ForecastSpaceData?serviceKey=";
    private static String KEY_SERVER_TOKEN = "xhDtPXt9wyLH3nA1E1EHfDoOyjiSS0mg6df0fxgjNgXx4Nnup45AnaS23rKyNihhgVLUYp9aLEBKi45No%2B7tcQ%3D%3D";

    private VolleyResult resultCallback = null;
    private Context mContext = null;


    private Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            resultCallback.notifySuccess(null, response);
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            resultCallback.notifyError(error);
        }
    };

    public VolleyService(VolleyResult resultCallback, Context context) {
        this.resultCallback = resultCallback;
        mContext = context;
    }


    public void getGeocoder(Location location) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String output = "json";
        String parameter = "latlng=" + location.getLatitude() + "," + location.getLongitude() + "&language=ko" + "&key=" + "AIzaSyAggFaSQsTk8_Rsk_znu9QYMD7agMtoZVA";
        final String url = "https://maps.googleapis.com/maps/api/geocode/"
                + output + "?" + parameter;


        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };
        requestQueue.add(req);
    }

    public void getCurrentTemp(LatXLngY location) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String[] baseTime = getHourBaseTimeParms(mContext);
        String url = KEY_WEATHER_METEOROLOGICAL_API_URL + KEY_WEATHER_CURRENT_TEMP_BODY + KEY_SERVER_TOKEN + "&base_date=" + baseTime[0] + "&base_time=" + baseTime[1] + "&nx=" + (int) location.getX()
                + "&ny=" + (int) location.getY() + "&numOfRows=" + "20" + "&pageNo=" + "1" + "&_type=json";

        L.e("::::::getCurrentTemp url : " + url);
        Request<JSONObject> req = new Request<JSONObject>(Request.Method.GET, url, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallback.notifyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String json = new String(
                            response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(json), HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JsonSyntaxException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                resultCallback.notifySuccess("", response);
            }
        };

        requestQueue.add(req);

    }

    private String[] getHourBaseTimeParms(Context context) {
        String[] validSpaceBaseTime = new String[]{"0000", "0100", "0200", "0300", "0400", "0500", "0600", "0700", "0800", "0900", "1000"
                , "1100", "1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900", "2000", "2100", "2200", "2300"};
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmm", Locale.getDefault());
        String[] temp = sdf.format(new Date()).split(" ");
        String baseDate = temp[0];
        String baseTime = temp[1];
        String[] resultBaseTime = new String[2];

        String result = null;
        int baseTimeValue = Integer.parseInt(baseTime);
        L.e(":::baseTimeValue : " + baseTimeValue);
        for (String t : validSpaceBaseTime) {
            int tValue = Integer.parseInt(t) + 40; // 특정 시간의 10분 이후에 api 제공
            if (baseTimeValue > tValue) {
                result = t;
            } else {
                break;
            }
        }
        if (result != null) {
            baseTime = result;
        }
        resultBaseTime[0] = baseDate;
        resultBaseTime[1] = baseTime;

        return resultBaseTime;
    }


}
