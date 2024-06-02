package com.eventapp.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EventDTO {

    private Long id;
    private String title;
    private String description;
    private Date date;
    private String location;
    private int availableSeats;

}
