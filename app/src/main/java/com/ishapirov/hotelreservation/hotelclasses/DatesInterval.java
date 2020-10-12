package com.ishapirov.hotelreservation.hotelclasses;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class DatesInterval {
    @Column(name = "checkin")
    private Date checkInDate;
    @Column(name = "checkout")
    private Date checkOutDate;

    public boolean isAvailable(Date otherDateCheckIn, Date otherDateCheckOut) {
        if((this.checkInDate.compareTo(otherDateCheckIn) >= 0 && this.checkInDate.compareTo(otherDateCheckOut) < 0)
            || (this.checkOutDate.compareTo(otherDateCheckIn) > 0 && this.checkOutDate.compareTo(otherDateCheckOut) <= 0)
            || (this.checkInDate.compareTo(otherDateCheckIn) <= 0 && this.checkOutDate.compareTo(otherDateCheckOut) >= 0)){
            return false;
        }
        return true;
    }
}
