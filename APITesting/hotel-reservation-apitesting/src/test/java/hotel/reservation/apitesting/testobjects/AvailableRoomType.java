package hotel.reservation.apitesting.testobjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableRoomType {
    private String roomTypeName;
    private String checkInDate;
    private String checkOutDate;
}