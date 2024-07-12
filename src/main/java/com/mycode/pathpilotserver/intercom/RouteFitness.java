package com.mycode.pathpilotserver.intercom;

import com.mycode.pathpilotserver.packages.models.Package;
import lombok.Getter;

import java.util.List;

@Getter
public class RouteFitness {
    private final List<List<Package>> routes;
    private final double distance;

    public RouteFitness(List<List<Package>> routes, double distance) {
        this.routes = routes;
        this.distance = distance;
    }
}
