package com.example.apartmentcontrol.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Building {
    private UUID id;
    private double requestedTemperature = 20.0;
    private double closeEnoughThreshold = 0.5;
    private List<Rooms> rooms = new ArrayList<>();
}
