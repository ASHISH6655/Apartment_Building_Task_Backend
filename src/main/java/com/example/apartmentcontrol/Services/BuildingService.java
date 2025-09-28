package com.example.apartmentcontrol.Services;

import com.example.apartmentcontrol.models.Apartment;
import com.example.apartmentcontrol.models.Building;
import com.example.apartmentcontrol.models.CommonRoom;
import com.example.apartmentcontrol.models.Rooms;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class BuildingService {

    private final List<Building> buildings = new CopyOnWriteArrayList<>();

    // initialize main building on startup
    @PostConstruct
    public void init() {
        Building b = new Building();
        b.setId(UUID.randomUUID());
        b.setRequestedTemperature(25.0); // per requirement

        Apartment apt101 = new Apartment();
        apt101.setId(UUID.randomUUID());
        apt101.setAptNumber("101");
        apt101.setOwnerName("Owner 101");
        apt101.setTemperature(round(randomTemp()));

        Apartment apt102 = new Apartment();
        apt102.setId(UUID.randomUUID());
        apt102.setAptNumber("102");
        apt102.setOwnerName("Owner 102");
        apt102.setTemperature(round(randomTemp()));

        CommonRoom gym = new CommonRoom();
        gym.setId(UUID.randomUUID());
        gym.setCommonType("Gym");
        gym.setTemperature(round(randomTemp()));

        CommonRoom library = new CommonRoom();
        library.setId(UUID.randomUUID());
        library.setCommonType("Library");
        library.setTemperature(round(randomTemp()));

        b.getRooms().addAll(List.of(apt101, apt102, gym, library));
        buildings.add(b);

        recalculateBuilding(b);
    }

    private double randomTemp() {
        return 10.0 + Math.random() * 30.0; // 10-40
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public List<Building> getAllBuildings() {
        return buildings;
    }

    public Building getMainBuilding() {
        if (buildings.isEmpty()) throw new RuntimeException("No buildings");
        return buildings.get(0);
    }

    public Building getBuilding(UUID id) {
        return buildings.stream().filter(b -> b.getId().equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("Building not found"));
    }

    public void addRoom(UUID buildingId, Rooms room) {
        Building b = getBuilding(buildingId);
        room.setId(UUID.randomUUID());
        if (room.getTemperature() == 0.0) room.setTemperature(round(randomTemp()));
        b.getRooms().add(room);
        recalculateBuilding(b);
    }

    public void removeRoom(UUID buildingId, UUID roomId) {
        Building b = getBuilding(buildingId);
        b.getRooms().removeIf(r -> r.getId().equals(roomId));
        recalculateBuilding(b);
    }

    public void updateRequestedTemperature(UUID buildingId, double requested) {
        Building b = getBuilding(buildingId);
        b.setRequestedTemperature(requested);
        recalculateBuilding(b);
    }

    public void recalculateBuilding(Building b) {
        double requested = b.getRequestedTemperature();
        double threshold = b.getCloseEnoughThreshold();
        for (Rooms r : b.getRooms()) {
            double temp = r.getTemperature();
            if (Math.abs(temp - requested) <= threshold) {
                r.setHeatingEnabled(false);
                r.setCoolingEnabled(false);
            } else if (temp < requested) {
                r.setHeatingEnabled(true);
                r.setCoolingEnabled(false);
            } else {
                r.setHeatingEnabled(false);
                r.setCoolingEnabled(true);
            }
        }
    }

    // manual recalc from controller
    public void recalcById(UUID buildingId) {
        Building b = getBuilding(buildingId);
        recalculateBuilding(b);
    }
}
