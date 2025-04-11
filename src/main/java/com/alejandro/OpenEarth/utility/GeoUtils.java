package com.alejandro.OpenEarth.utility;

public class GeoUtils {
    private static final double EARTH_RADIUS_KM = 6371.0;

    public static BoundingBox getBoundingBox(double lat, double lng, double distanceKm) {
        double latDelta = Math.toDegrees(distanceKm / EARTH_RADIUS_KM);
        double lngDelta = Math.toDegrees(distanceKm / EARTH_RADIUS_KM / Math.cos(Math.toRadians(lat)));

        return new BoundingBox(
                lat - latDelta, lat + latDelta,
                lng - lngDelta, lng + lngDelta
        );
    }

    public record BoundingBox(
            double minLat,
            double maxLat,
            double minLng,
            double maxLng
    ) {}
}
