package com.alejandro.OpenEarth.serviceImpl;

import com.alejandro.OpenEarth.entity.House;
import com.alejandro.OpenEarth.service.GeoPolygonService;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("geoPolygonService")
public class GeoPolygonServiceImpl implements GeoPolygonService {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    public List<House> filterHousesInPolygon(Object geoJson, List<House> houses) {
        Geometry geometry = parseGeoJsonToGeometry((Map<String, Object>) geoJson);
        List<House> filtered = new ArrayList<>();

        for (House house : houses) {
            Point point = geometryFactory.createPoint(new Coordinate(house.getLongitude(), house.getLatitude()));
            if (geometry.contains(point)) {
                filtered.add(house);
            }
        }

        return filtered;
    }

    private Geometry parseGeoJsonToGeometry(Map<String, Object> geojson) {
        String type = (String) geojson.get("type");
        List<?> coordinates = (List<?>) geojson.get("coordinates");

        if ("Polygon".equalsIgnoreCase(type)) {
            return createPolygon((List<List<List<Double>>>) coordinates);
        } else if ("MultiPolygon".equalsIgnoreCase(type)) {
            return createMultiPolygon((List<List<List<List<Double>>>>) coordinates);
        }else if ("Point".equalsIgnoreCase(type)) {
            List<Double> pointCoords = (List<Double>) coordinates;
            return geometryFactory.createPoint(new Coordinate(pointCoords.get(0), pointCoords.get(1)));
        }

        throw new UnsupportedOperationException("Unsupported GeoJSON type: " + type);
    }

    private LinearRing createLinearRing(List<List<Double>> coordinates) {
        Coordinate[] coords = coordinates.stream()
                .map(c -> new Coordinate(c.get(0), c.get(1)))
                .toArray(Coordinate[]::new);

        return geometryFactory.createLinearRing(coords);
    }

    private Polygon createPolygon(List<List<List<Double>>> rings) {
        LinearRing shell = createLinearRing(rings.get(0));
        LinearRing[] holes = new LinearRing[rings.size() - 1];

        for (int i = 1; i < rings.size(); i++) {
            holes[i - 1] = createLinearRing(rings.get(i));
        }

        return geometryFactory.createPolygon(shell, holes);
    }

    private MultiPolygon createMultiPolygon(List<List<List<List<Double>>>> polygons) {
        Polygon[] polygonArray = new Polygon[polygons.size()];

        for (int i = 0; i < polygons.size(); i++) {
            polygonArray[i] = createPolygon(polygons.get(i));
        }

        return geometryFactory.createMultiPolygon(polygonArray);
    }

}
