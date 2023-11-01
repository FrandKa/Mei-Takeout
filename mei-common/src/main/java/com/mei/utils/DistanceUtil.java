package com.mei.utils;

import com.mei.pojo.Location;

/**
 * @program: sky-take-out
 * @description: 根据经纬度值计算两个坐标的距离
 * @author: Mr.Ka
 * @create: 2023-11-01 09:35
 **/

public class DistanceUtil {
    private DistanceUtil(){}

    public static double getDistance(Location l1, Location l2) {
        double lat1 = l1.getLat(); // 第一个坐标的纬度
        double lon1 = l1.getLng(); // 第一个坐标的经度
        double lat2 = l2.getLat(); // 第二个坐标的纬度
        double lon2 = l2.getLng();  // 第二个坐标的经度

        return calculateHaversineDistance(lat1, lon1, lat2, lon2);
    }

    public static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度从度数转换为弧度
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        System.out.println("lon1 = " + lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);
        System.out.println("lon2 = " + lon2);

        // Haversine公式
        double dlon = lon2 - lon1;
        System.out.println("dlon = " + dlon);
        double dlat = lat2 - lat1;
        double a = Math.sin(dlat/2) * Math.sin(dlat/2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(dlon/2) * Math.sin(dlon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double r = 6371; // 地球的半径（单位：千米）
        System.out.println("c = " + c);
        return r * c;
    }
}
