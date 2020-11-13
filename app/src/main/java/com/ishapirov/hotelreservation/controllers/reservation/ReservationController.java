package com.ishapirov.hotelreservation.controllers.reservation;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.pagination.HotelPage;
import com.ishapirov.hotelapi.services.reservation.domain.*;
import com.ishapirov.hotelapi.services.reservation.paramvalidation.ReservationsCriteria;
import com.ishapirov.hotelapi.services.reservation.ReservationService;
import com.ishapirov.hotelapi.services.reservation.domain.admin.BookReservationForUser;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.mapper.DomainToApiMapper;
import com.ishapirov.hotelreservation.retrievefilter.ReservationUtil;
import com.ishapirov.hotelreservation.retrievefilter.RoomUtil;
import com.ishapirov.hotelreservation.retrievefilter.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;


@RestController
@Validated
public class ReservationController implements ReservationService {
    @Autowired
    private ReservationUtil reservationUtil;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    private RoomUtil roomUtil;
    @Autowired
    private DomainToApiMapper domainToApiMapper;

    @Override
    public HotelPage<ReservationBasicInformation> getReservations(ReservationsCriteria reservationsCriteria) {
        if(reservationsCriteria.getUsername() == null ){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean hasUserRole = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
            throw new NotImplementedException("This operation is not yet supported");
        }
        Pageable pageable = PageRequest.of(reservationsCriteria.getPageNumber(),reservationsCriteria.getSize());
        Page<Reservation> reservationPage = reservationUtil.getReservationsByUsername(reservationsCriteria.getUsername(),pageable);
        return domainToApiMapper.getReservationInformationPage(reservationPage,pageable);
    }

    @Override
    public ReservationInformation getReservation(Integer reservationNumber) {
        Reservation reservation = reservationUtil.getReservationByReservationNumber(reservationNumber);
        return domainToApiMapper.getReservationInformation(reservation);
    }


    @Override
    public ReservationInformation bookReservation(BookReservation bookReservation) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookReservationForUser bookReservationForUser = new BookReservationForUser(username, bookReservation);
        return bookReservationForUser(bookReservationForUser);
    }

    @Override
    public ReservationInformation bookReservationForUser(BookReservationForUser bookReservationForUser) {
        Reservation reservation = reservationUtil.bookReservation(bookReservationForUser);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    @Transactional
    public ReservationInformation updateReservation(Integer reservationNumber, ReservationUpdate reservationUpdate) {
        Reservation reservation = reservationUtil.updateReservation(reservationNumber,reservationUpdate);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Integer reservationNumber) {
       reservationUtil.deleteReservation(reservationNumber);
    }

    @Override
    public ReservationInformation cancelReservation(CancelReservation cancelReservation) {
        Reservation reservation = reservationUtil.cancelReservation(cancelReservation);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    public ReservationInformation cancelReservationForUser(CancelReservation cancelReservation) {
        Reservation reservation = reservationUtil.cancelReservationForUser(cancelReservation);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    public ReservationResponse getReservationResponse(Integer reservationNumber) {
        Reservation reservation = reservationUtil.getReservationByReservationNumber(reservationNumber);
        return domainToApiMapper.getReservationResponse(reservation);
    }


}
