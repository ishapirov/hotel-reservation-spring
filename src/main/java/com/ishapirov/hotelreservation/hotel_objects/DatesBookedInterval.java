package com.ishapirov.hotelreservation.hotel_objects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "datesbooked")
public class DatesBookedInterval implements Serializable {
    @Id
    @Column(name = "reservation_id")
    private Integer reservationID;
    private Date dateCheckIn;
    private Date dateCheckOut;

    @OneToOne
    @MapsId
    private Reservation reservation;
}
