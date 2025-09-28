package com.example.apartmentcontrol.models;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Apartment extends Rooms {
    private String aptNumber;
    private String ownerName;
}


