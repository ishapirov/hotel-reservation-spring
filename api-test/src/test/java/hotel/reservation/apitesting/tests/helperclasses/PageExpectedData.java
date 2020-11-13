package hotel.reservation.apitesting.tests.helperclasses;

import com.ishapirov.hotelapi.services.room.domain.RoomInformation;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageExpectedData {
    private Integer totalPages;
    private boolean nextPage;
    private Integer size;
}
