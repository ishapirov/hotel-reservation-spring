package com.ishapirov.hotelreservation.hotel_objects;

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

    // public boolean checkIfAvailable(Date otherDateCheckIn, Date otherDateCheckOut) {
    //     	for (DatesBookedInterval dates : datesBooked.values()){
    //     		if(otherDateCheckIn.compareTo(dates.getDateCheckOut()) > 0 || otherDateCheckOut.compareTo(dates.getDateCheckIn()) < 0)
    //     		return true;
    //     	}
    //     	return false;
    //     }
    
    //     public void updateDates(Integer userID,DatesBookedInterval datesBookedInterval) {
    //     	this.datesBooked.put(userID,datesBookedInterval);
    //     }
    
    //     public void clearDateInterval(int userID) {
    //     	this.datesBooked.remove(userID);
    //     }
}
