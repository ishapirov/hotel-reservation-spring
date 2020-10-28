package hotel.reservation.apitesting.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class MyObjectMapper extends ObjectMapper{

    public MyObjectMapper(){
        super();
        this.setDateFormat(new StdDateFormat());
    }
}
