package com.example.apartmentcontrol.scheduler;

import com.example.apartmentcontrol.Services.BuildingService;
import com.example.apartmentcontrol.models.Building;
import com.example.apartmentcontrol.models.Rooms;
import org.springframework.scheduling.annotation.Scheduled;

public class TemperatureScheduler {

    private final BuildingService buildingService;

    public TemperatureScheduler(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    // every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void tick() {
        Building b = buildingService.getMainBuilding();

        for (Rooms r : b.getRooms()) {
            if (r.isHeatingEnabled()) {
                r.setTemperature(round(r.getTemperature() + 0.2));
            } else if (r.isCoolingEnabled()) {
                r.setTemperature(round(r.getTemperature() - 0.25));
            }
        }

        // after drift, recalc flags
        buildingService.recalculateBuilding(b);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

}
