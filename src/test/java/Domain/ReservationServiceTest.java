package Domain;

import Data.DataException;
import Data.GuestRepoDouble;
import Data.HostRepoDouble;
import Data.ReservationRepoDouble;
import Models.Guest;
import Models.Host;
import Models.Reservation;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationServiceTest {
    ReservationService service = new ReservationService(
            new ReservationRepoDouble(),
            new HostRepoDouble(),
            new GuestRepoDouble());


    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepoDouble.HOST);
        reservation.setResId(13);
        reservation.setStartDate(LocalDate.of(2021, 8, 1));
        reservation.setEndDate(LocalDate.of(2021, 8, 7));
        reservation.setGuest(GuestRepoDouble.GUEST);
        reservation.setTotal(new BigDecimal(2000));

        Result<Reservation> result = service.add(reservation);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
    }

    @Test
    void shouldNotAddWhenHostNotFound() throws DataException {
        Host host = new Host();
        host.setHostId("123456789");

        Guest guest = new Guest();
        guest.setGuestId(33);

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setResId(40);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2021, 8, 1));
        reservation.setEndDate(LocalDate.of(2021, 8, 7));
        reservation.setTotal(new BigDecimal(450));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());

    }

    @Test
    void shouldNotAddWhenGuestNotFound() throws DataException {
        Host host = new Host();
        host.setHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Guest guest = new Guest();
        guest.setGuestId(2000);

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());


    }

    @Test
    void shouldNotAddWhenDatesOverlap() throws DataException {
        Host host = new Host();
        host.setHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Guest guest = new Guest();
        guest.setGuestId(8);

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2021, 11, 1));
        reservation.setEndDate(LocalDate.of(2021, 11, 8));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenDateInPast() throws DataException {
        Host host = new Host();
        host.setHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");

        Guest guest = new Guest();
        guest.setGuestId(8);

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2020, 11, 1));
        reservation.setEndDate(LocalDate.of(2020, 11, 8));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldNotAddWhenFieldsNull() throws DataException {


        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.of(2021, 11, 1));
        reservation.setEndDate(LocalDate.of(2021, 11, 8));

        Result<Reservation> result = service.add(reservation);
        assertFalse(result.isSuccess());
    }


    @Test
    void shouldUpdate() throws DataException {
        Host host = new Host();
        host.setHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");


        Guest guest = new Guest();
        guest.setGuestId(1);

        Reservation reservation = new Reservation();
        reservation.setResId(13);
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2023, 7, 3));
        reservation.setEndDate(LocalDate.of(2023, 7, 15));
        reservation.setTotal(new BigDecimal(150));

        Result<Reservation> result = service.update(reservation);
        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotUpdateMissing() throws DataException {
        Host host = new Host();
        host.setHostId("no");

        Guest guest = new Guest();
        guest.setGuestId(0);

        Reservation reservation = new Reservation();
        reservation.setResId(777);
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(LocalDate.of(2021, 8, 3));
        reservation.setEndDate(LocalDate.of(2021, 8, 15));
        reservation.setTotal(new BigDecimal(150));

        Result<Reservation> result = service.update(reservation);
        assertFalse(result.isSuccess());


    }

    @Test
    void shouldDeleteById() throws DataException {
        Host host = new Host();
        host.setHostId(HostRepoDouble.HOST.getHostId());

        Reservation reservation = new Reservation();
        reservation.setResId(13);
        reservation.setHost(host);

        Result<Reservation> result = service.deleteById(reservation.getHost().getHostId(), reservation.getResId());
        assertTrue(result.isSuccess());


    }

    @Test
    void shouldNotDeleteMissing() throws DataException {
        Host host = new Host();
        host.setHostId(HostRepoDouble.HOST.getHostId());

        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setResId(500);
        Result<Reservation> result = service.deleteById(reservation.getHost().getHostId(), reservation.getResId());
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotDeletePastReservation() throws DataException {
        Reservation reservation = new Reservation();
        Host host = new Host();
        host.setHostId(HostRepoDouble.HOST.getHostId());

        reservation.setHost(host);
        reservation.setResId(14);

        Result<Reservation> result = service.deleteById(reservation.getHost().getHostId(), reservation.getResId());
        assertNull(result);


    }


}