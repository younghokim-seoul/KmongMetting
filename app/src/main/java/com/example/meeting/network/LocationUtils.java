package com.example.meeting.network;


public class LocationUtils {

    public static LatXLngY convertGRID_GPS(double lat_X, double lng_Y) {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        rs.setLat(lat_X);
        rs.setLng(lng_Y);
        double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = lng_Y * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;
        rs.setX(Math.floor(ra * Math.sin(theta) + XO + 0.5));
        rs.setY(Math.floor(ro - ra * Math.cos(theta) + YO + 0.5));
        return rs;
    }


    public static String convertAreaCode(String address) {
        String result = "";
        if (address.contains("서울")) {
            result = "1100000000";
            if (address.contains("종로구")) {
                result = "1100000000";
            } else if (address.contains("중구")) {
                result = "1100000000";
            } else if (address.contains("중구")) {
                result = "1100000000";
            } else if (address.contains("용산구")) {
                result = "1100000000";
            } else if (address.contains("성동구")) {
                result = "1100000000";
            } else if (address.contains("광진구")) {
                result = "1100000000";
            } else if (address.contains("동대문구")) {
                result = "1100000000";
            } else if (address.contains("중랑구")) {
                result = "1100000000";
            } else if (address.contains("성북구")) {
                result = "1100000000";
            } else if (address.contains("강북구")) {
                result = "1100000000";
            } else if (address.contains("도봉구")) {
                result = "1100000000";
            } else if (address.contains("노원구")) {
                result = "1100000000";
            } else if (address.contains("은평구")) {
                result = "1100000000";
            } else if (address.contains("서대문구")) {
                result = "1100000000";
            } else if (address.contains("마포구")) {
                result = "1100000000";
            } else if (address.contains("양천구")) {
                result = "1100000000";
            } else if (address.contains("강서구")) {
                result = "1100000000";
            } else if (address.contains("구로구")) {
                result = "1100000000";
            } else if (address.contains("금천구")) {
                result = "1100000000";
            } else if (address.contains("영등포구")) {
                result = "1100000000";
            } else if (address.contains("동작구")) {
                result = "1100000000";
            } else if (address.contains("관악구")) {
                result = "1100000000";
            } else if (address.contains("서초구")) {
                result = "1100000000";
            } else if (address.contains("강남구")) {
                result = "1100000000";
            } else if (address.contains("송파구")) {
                result = "1100000000";
            } else if (address.contains("강동구")) {
                result = "1100000000";
            }
        } else if (address.contains("부산")) {
            result = "2600000000";
        } else if (address.contains("대구")) {
            result = "2700000000";
        } else if (address.contains("인천")) {
            result = "2800000000";
        } else if (address.contains("광주광역시")) {
            result = "2900000000";
        } else if (address.contains("대전")) {
            result = "3000000000";
        } else if (address.contains("울산")) {
            result = "3100000000";
        } else if (address.contains("세종")) {
            result = "3600000000";
        } else if (address.contains("경기도")) {
            result = "4100000000";
            if (address.contains("만안구")) {
                result = "4117100000";
            }
        } else if (address.contains("강원도")) {
            result = "4200000000";
        } else if (address.contains("충청북도")) {
            result = "4300000000";
        } else if (address.contains("충청남도")) {
            result = "4413100000";
        } else if (address.contains("전라북도")) {
            result = " 4500000000";
        } else if (address.contains("전라남도")) {
            result = " 4600000000";
        } else if (address.contains("경상북도")) {
            result = " 4700000000";
        } else if (address.contains("경상남도")) {
            result = " 4800000000";
        }
        return result;
    }


}
