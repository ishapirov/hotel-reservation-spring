package com.ishapirov.hotelapi.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelPage<T> {
    private List<T> content;
    private Integer totalPages;
    private boolean hasNextPage;
}
