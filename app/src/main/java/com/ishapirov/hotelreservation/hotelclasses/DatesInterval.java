package com.ishapirov.hotelreservation.hotelclasses;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@Data
@MappedSuperclass
public abstract class DatesInterval {
    @Column(name = "checkin")
    private Date dateCheckIn;
    @Column(name = "checkout")
    private Date dateCheckOut;

    public boolean isAvailable(Date otherDateCheckIn, Date otherDateCheckOut) {
        if((this.dateCheckIn.compareTo(otherDateCheckIn) >= 0 && this.dateCheckIn.compareTo(otherDateCheckOut) < 0) 
            || (this.dateCheckOut.compareTo(otherDateCheckIn) > 0 && this.dateCheckOut.compareTo(otherDateCheckOut) <= 0) 
            || (this.dateCheckIn.compareTo(otherDateCheckIn) <= 0 && this.dateCheckOut.compareTo(otherDateCheckOut) >= 0)){
            return false;
        }
        return true;
    }
}
