package hotel.reservation.apitesting.testobjects;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookRoom {
    
    private Integer roomNumber;
    private String checkInDate;
    private String checkOutDate;
}

