package com.example.apartmentcontrol.Controller;

import com.example.apartmentcontrol.Services.BuildingService;
import com.example.apartmentcontrol.models.Apartment;
import com.example.apartmentcontrol.models.Building;
import com.example.apartmentcontrol.models.CommonRoom;
import com.example.apartmentcontrol.models.Rooms;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/buildings")
@CrossOrigin(origins = "http://localhost:3000")
public class BuildingController {

    private final BuildingService service;

    public BuildingController(BuildingService service) {
        this.service = service;
    }

    @GetMapping("/main")
    public ResponseEntity<Map<String, Object>> getMainId() {
        Building b = service.getMainBuilding();
        return ResponseEntity.ok(Map.of("id", b.getId().toString()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuilding(@PathVariable String id) {
        Building b = service.getBuilding(UUID.fromString(id));
        return ResponseEntity.ok(b);
    }

    @PostMapping("/{id}/requestedTemperature")
    public ResponseEntity<Building> setRequested(@PathVariable String id, @RequestBody Map<String, Number> body) {
        double requested = body.get("requested").doubleValue();
        service.updateRequestedTemperature(UUID.fromString(id), requested);
        return ResponseEntity.ok(service.getBuilding(UUID.fromString(id)));
    }

    @PostMapping("/{id}/rooms")
    public ResponseEntity<Building> addRoom(@PathVariable String id, @RequestBody Map<String, Object> body) {
        UUID bid = UUID.fromString(id);
        String kind = (String) body.get("kind"); // "apartment" or "common"
        Rooms room;
        if ("apartment".equalsIgnoreCase(kind)) {
            Apartment a = new Apartment();
            a.setAptNumber((String) body.get("aptNumber"));
            a.setOwnerName((String) body.get("ownerName"));
            // optional temperature input
            if (body.get("temperature") != null) a.setTemperature(((Number) body.get("temperature")).doubleValue());
            room = a;
        } else {
            CommonRoom c = new CommonRoom();
            c.setCommonType((String) body.get("commonType"));
            if (body.get("temperature") != null) c.setTemperature(((Number) body.get("temperature")).doubleValue());
            room = c;
        }
        service.addRoom(bid, room);
        return ResponseEntity.ok(service.getBuilding(bid));
    }

    @DeleteMapping("/{id}/rooms/{roomId}")
    public ResponseEntity<Building> removeRoom(@PathVariable String id, @PathVariable String roomId) {
        UUID bid = UUID.fromString(id);
        UUID rid = UUID.fromString(roomId);
        service.removeRoom(bid, rid);
        return ResponseEntity.ok(service.getBuilding(bid));
    }

    @PostMapping("/{id}/recalculate")
    public ResponseEntity<Building> manualRecalc(@PathVariable String id) {
        UUID bid = UUID.fromString(id);
        service.recalcById(bid);
        return ResponseEntity.ok(service.getBuilding(bid));
    }
}
