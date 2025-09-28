package com.example.apartmentcontrol.models;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class Rooms {
    private UUID id;
    private double temperature;
    private boolean heatingEnabled;
    private boolean coolingEnabled;
}
