package com.mycode.pathpilotserver.graph.models;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.mycode.pathpilotserver.intercom.maps.DirectionsService;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import static com.mycode.pathpilotserver.utils.Utile.API_KEY_GOOGLE;
import static com.mycode.pathpilotserver.utils.Utile.API_URL;


@Getter
public class CountryGraphBuilder {
    private GeoApiContext context;
    private Map<String, Double> distanceCache;

    private final DirectionsService directionsService = new DirectionsService();

    private final Graph<String, DefaultWeightedEdge> graph;



    public CountryGraphBuilder() {
        context = new GeoApiContext.Builder().apiKey(API_KEY_GOOGLE).build();
        distanceCache = new HashMap<>();
        graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public void buildGraph() throws JSONException {
        JSONArray counties = fetchCounties();

        if (counties == null) {
            throw new JSONException("Failed to fetch counties");
        }

        for (int i = 0; i < counties.length(); i++) {
            JSONObject county = counties.getJSONObject(i);
            graph.addVertex(county.getString("nume"));
        }

        for (int i = 0; i < counties.length(); i++) {
            for (int j = i + 1; j < counties.length(); j++) {
                String origin = counties.getJSONObject(i).getString("nume") + ", Romania";
                String destination = counties.getJSONObject(j).getString("nume") + ", Romania";
                String cacheKey = origin + "->" + destination;

                double distanceInKm;
                if (distanceCache.containsKey(cacheKey)) {
                    distanceInKm = distanceCache.get(cacheKey);
                } else {
//                    try {
//                        DirectionsResult result = directionsService.getDirections(origin, destination);
//                        distanceInKm = result.routes[0].legs[0].distance.inMeters / 1000.0;
//                        distanceCache.put(cacheKey, distanceInKm);
//                        Random random = new Random();
//                        int distance = random.nextInt(801) + 100;
//                    } catch (ApiException | InterruptedException | IOException e) {
//                        e.printStackTrace();
//                        continue;
//                    }

                    Random random = new Random();
                    distanceInKm = random.nextDouble(801) + 100;
                }

                DefaultWeightedEdge edge = graph.addEdge(counties.getJSONObject(i).getString("nume"), counties.getJSONObject(j).getString("nume"));
                if (edge != null) {
                    graph.setEdgeWeight(edge, distanceInKm);
                }
            }

        }


    }

    private static JSONArray fetchCounties() {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }
                scanner.close();
                return new JSONArray(inline.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
