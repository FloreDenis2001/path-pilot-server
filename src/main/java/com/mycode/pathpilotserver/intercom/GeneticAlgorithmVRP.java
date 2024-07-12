package com.mycode.pathpilotserver.intercom;

import com.mycode.pathpilotserver.packages.models.Package;
import com.mycode.pathpilotserver.vehicles.models.Vehicle;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithmVRP {
    private final List<Package> packages;
    private final List<Vehicle> vehicles;

    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int generations;

    @Getter
    private List<List<List<Package>>> bestRoutes;

    public GeneticAlgorithmVRP(List<Package> packages, List<Vehicle> vehicles) {
        this.packages = packages;
        this.vehicles = vehicles;
    }

    public void setPopulationSize(int size) {
        this.populationSize = size;
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    public void setCrossoverRate(double rate) {
        this.crossoverRate = rate;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public void initializePopulation() {
        bestRoutes = new ArrayList<>();
        Random random = new Random();
        int numVehicles = vehicles.size();
        int numPackages = packages.size();

        for (int i = 0; i < populationSize; i++) {
            List<Package> shuffledPackages = new ArrayList<>(packages);
            Collections.shuffle(shuffledPackages);
            List<List<Package>> routes = new ArrayList<>();

            int routeSize = (int) Math.ceil((double) numPackages / numVehicles);

            for (int j = 0; j < numVehicles; j++) {
                int startIndex = j * routeSize;
                int endIndex = Math.min(startIndex + routeSize, numPackages);
                List<Package> route = new ArrayList<>(shuffledPackages.subList(startIndex, endIndex));
                routes.add(route);
            }

            bestRoutes.add(routes);
        }
    }

    public void evolvePopulation() {
        Random random = new Random();

        for (int generation = 0; generation < generations; generation++) {
            List<RouteFitness> routeFitnessList = evaluatePopulation(bestRoutes);
            routeFitnessList.sort((f1, f2) -> Double.compare(f1.getDistance(), f2.getDistance()));

            List<List<List<Package>>> selectedRoutes = selectRoutes(routeFitnessList);

            List<List<List<Package>>> newPopulation = crossover(selectedRoutes, random);
            mutate(newPopulation);

            bestRoutes = new ArrayList<>(newPopulation);
        }
    }

    private List<RouteFitness> evaluatePopulation(List<List<List<Package>>> population) {
        List<RouteFitness> routeFitnessList = new ArrayList<>();
        for (List<List<Package>> routes : population) {
            double distance = calculateTotalDistance(routes);
            RouteFitness routeFitness = new RouteFitness(routes, distance);
            routeFitnessList.add(routeFitness);
        }
        return routeFitnessList;
    }

    private List<List<List<Package>>> selectRoutes(List<RouteFitness> routeFitnessList) {
        List<List<List<Package>>> selectedRoutes = new ArrayList<>();
        for (int i = 0; i < populationSize / 2; i++) {
            selectedRoutes.add(routeFitnessList.get(i).getRoutes());
        }
        return selectedRoutes;
    }

    private List<List<List<Package>>> crossover(List<List<List<Package>>> selectedRoutes, Random random) {
        List<List<List<Package>>> newPopulation = new ArrayList<>();
        for (int i = 0; i < selectedRoutes.size(); i++) {
            List<List<Package>> parent1 = selectedRoutes.get(random.nextInt(selectedRoutes.size()));
            List<List<Package>> parent2 = selectedRoutes.get(random.nextInt(selectedRoutes.size()));

            List<List<Package>> child1 = new ArrayList<>();
            List<List<Package>> child2 = new ArrayList<>();

            if (random.nextDouble() < crossoverRate) {
                for (int j = 0; j < parent1.size(); j++) {
                    List<Package> route1 = new ArrayList<>(parent1.get(j));
                    List<Package> route2 = new ArrayList<>(parent2.get(j));
                    List<Package> newRoute1 = new ArrayList<>();
                    List<Package> newRoute2 = new ArrayList<>();

                    int crossoverPoint = random.nextInt(route1.size());

                    for (int k = 0; k < crossoverPoint; k++) {
                        newRoute1.add(route1.get(k));
                        newRoute2.add(route2.get(k));
                    }

                    for (Package pack : route2) {
                        if (!newRoute1.contains(pack)) {
                            newRoute1.add(pack);
                        }
                    }

                    for (Package pack : route1) {
                        if (!newRoute2.contains(pack)) {
                            newRoute2.add(pack);
                        }
                    }

                    child1.add(newRoute1);
                    child2.add(newRoute2);
                }
            } else {
                child1.addAll(parent1);
                child2.addAll(parent2);
            }

            newPopulation.add(child1);
            newPopulation.add(child2);
        }
        return newPopulation;
    }

    private void mutate(List<List<List<Package>>> population) {
        Random random = new Random();
        for (List<List<Package>> routes : population) {
            if (random.nextDouble() < mutationRate) {
                int routeIndex1 = random.nextInt(routes.size());
                int routeIndex2 = random.nextInt(routes.size());
                if (routeIndex1 != routeIndex2) {
                    List<Package> route1 = routes.get(routeIndex1);
                    List<Package> route2 = routes.get(routeIndex2);
                    if (!route1.isEmpty() && !route2.isEmpty()) {
                        int i = random.nextInt(route1.size());
                        int j = random.nextInt(route2.size());
                        Collections.swap(route1, i, j);
                    }
                }
            }
        }
    }

    private double calculateTotalDistance(List<List<Package>> routes) {
        double totalDistance = 0;
        for (List<Package> route : routes) {
            if (route.size() > 0) {
                for (int i = 0; i < route.size() - 1; i++) {
                    totalDistance += calculateDistance(route.get(i), route.get(i + 1));
                }

                totalDistance += calculateDistance(route.get(route.size() - 1), route.get(0));
            }
        }
        return totalDistance / 1000;
    }

    private double calculateDistance(Package p1, Package p2) {
        double lat1 = p1.getShipment().getDestinationAddress().getCityDetails().getLat();
        double lon1 = p1.getShipment().getDestinationAddress().getCityDetails().getLng();
        double lat2 = p2.getShipment().getDestinationAddress().getCityDetails().getLat();
        double lon2 = p2.getShipment().getDestinationAddress().getCityDetails().getLng();

        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
