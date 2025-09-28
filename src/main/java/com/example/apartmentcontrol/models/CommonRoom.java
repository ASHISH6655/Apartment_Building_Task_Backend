package com.example.apartmentcontrol.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonRoom extends Rooms {
    private String commonType; // Gym, Library, Laundry
}
