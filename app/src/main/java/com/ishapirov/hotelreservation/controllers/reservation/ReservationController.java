package com.ishapirov.hotelreservation.controllers.reservation;

import com.ishapirov.hotelapi.generalexceptions.NotImplementedException;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationResponse;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationUpdate;
import com.ishapirov.hotelapi.reservationservice.domain.admin.CancelReservationForCustomer;
import com.ishapirov.hotelapi.reservationservice.exceptions.*;
import com.ishapirov.hotelapi.roomservice.exceptions.RoomNotFoundException;
import com.ishapirov.hotelapi.reservationservice.domain.CancelReservation;
import com.ishapirov.hotelapi.reservationservice.ReservationService;
import com.ishapirov.hotelapi.reservationservice.domain.BookRoom;
import com.ishapirov.hotelapi.reservationservice.domain.ReservationInformation;
import com.ishapirov.hotelapi.reservationservice.domain.admin.BookRoomForCustomer;
import com.ishapirov.hotelapi.userservice.exceptions.CustomerNotFoundException;
import com.ishapirov.hotelreservation.domain.Reservation;
import com.ishapirov.hotelreservation.domain.Room;
import com.ishapirov.hotelreservation.domain.User;
import com.ishapirov.hotelreservation.repositories.ReservationRepository;
import com.ishapirov.hotelreservation.repositories.RoomRepository;
import com.ishapirov.hotelreservation.repositories.RoomTypeRepository;
import com.ishapirov.hotelreservation.repositories.UserRepository;
import com.ishapirov.hotelreservation.util.DomainToApiMapper;
import com.ishapirov.hotelreservation.util.HotelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class ReservationController implements ReservationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private HotelUtil hotelUtil;

    @Autowired
    private DomainToApiMapper domainToApiMapper;

    @Override
    public Page<ReservationInformation> getReservations(Integer pageNumber, Integer size, String username) {
        if(username == null ){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean hasUserRole = authentication.getAuthorities().stream()
                    .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
            throw new NotImplementedException("This operation is not yet supported");
        }
        Pageable pageable = PageRequest.of(pageNumber,size);
        Page<Reservation> reservationPage = reservationRepository.findAllByUser_Username(username,pageable);
        return domainToApiMapper.getReservationInformationPage(reservationPage,pageable);
    }

    @Override
    public ReservationInformation getReservation(Integer reservationNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(!reservation.isPresent())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        Reservation userReservation = reservation.get();
        if(!userDetails.getUsername().equals(userReservation.getUser().getUserSecurity().getUsername()))
            throw new ReservationUsernamesDoNotMatchException("The username on the reservation does not match the username of the logged in user");
        return domainToApiMapper.getReservationInformation(userReservation);
    }

    @Override
    public ReservationInformation bookReservation(BookRoom bookRoom) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        BookRoomForCustomer bookRoomForCustomer = new BookRoomForCustomer(username,bookRoom);
        return bookRoomForCustomer(bookRoomForCustomer);
    }

    @Override
    public ReservationInformation updateReservation(Integer reservationNumber, ReservationUpdate reservationUpdate) {
        throw new NotImplementedException("This operation is not yet supported");
    }

    @Override
    public ReservationInformation cancelReservation(CancelReservation cancelReservation) {
        Optional<Reservation> getReservation = reservationRepository.findByReservationNumber(cancelReservation.getReservationNumber());
        if(getReservation.isEmpty())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        if(reservationRepository.deleteByReservationNumber(cancelReservation.getReservationNumber()) != 1)
            throw new ReservationCreationError("Error while creating reservation");
        Reservation reservation = getReservation.get();
        if(reservation.isCancelled())
            throw new ReservationAlreadyCancelledException("This reservation has already been cancelled");
        reservation.setCancelled(true);
        reservationRepository.save(reservation);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    public ReservationResponse getReservationResponse(Integer reservationNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(!reservation.isPresent())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        Reservation userReservation = reservation.get();
        if(!userDetails.getUsername().equals(userReservation.getUser().getUserSecurity().getUsername()))
            throw new ReservationUsernamesDoNotMatchException("The username on the reservation does not match the username of the logged in user");
        return domainToApiMapper.getReservationResponse(userReservation);
    }

    @Override
    @Transactional
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteReservation(Integer reservationNumber) {
        Optional<Reservation> reservation = reservationRepository.findByReservationNumber(reservationNumber);
        if(reservation.isEmpty())
            throw new ReservationNotFoundException("A reservation with the given reservation number was not found");
        if(reservationRepository.deleteByReservationNumber(reservationNumber) != 1)
            throw new ReservationCreationError("Error while creating reservation");
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReservationInformation bookRoomForCustomer(BookRoomForCustomer bookRoomForCustomer) {
        Optional<User> customer = userRepository.findByUsername(bookRoomForCustomer.getUsername());
        Optional<Room> room = roomRepository.findByRoomNumber(bookRoomForCustomer.getRoomNumber());
        if(customer.isEmpty())
            throw new CustomerNotFoundException("A customer with the given id was not found");
        if(room.isEmpty())
            throw new RoomNotFoundException("A room with the given room number was not found");
        hotelUtil.validateDates(bookRoomForCustomer.getCheckInDate(),bookRoomForCustomer.getCheckOutDate());
        List<Reservation> reservations = reservationRepository.findByRoomAndCancelled(room.get(),false);
        if(!hotelUtil.isDateAvailable(reservations, bookRoomForCustomer.getCheckInDate(), bookRoomForCustomer.getCheckOutDate())){
            throw new ReservationOverlapException("The room has already been reserved for the given date interval");
        }
        Reservation reservation = new Reservation();
        reservation.setUser(customer.get());
        reservation.setRoom(room.get());
        reservation.setCheckInDate(bookRoomForCustomer.getCheckInDate());
        reservation.setCheckOutDate(bookRoomForCustomer.getCheckOutDate());
        reservationRepository.save(reservation);
        return domainToApiMapper.getReservationInformation(reservation);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReservationInformation cancelRoomForCustomer(CancelReservationForCustomer cancelReservation) {
        throw new NotImplementedException("This operation is not yet supported");
    }


}
