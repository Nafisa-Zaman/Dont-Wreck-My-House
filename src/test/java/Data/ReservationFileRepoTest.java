package Data;

import Models.Guest;
import Models.Host;
import Models.Reservation;
import jdk.jfr.DataAmount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.Local;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationFileRepoTest {
    static final String SEED_FILE_PATH = "./data/reservations_test/2e72f86c-b8fe-4265-b4f1-304dea8762db-seed.csv";
    static final String TEST_FILE_PATH = "./data/reservations_test/2e72f86c-b8fe-4265-b4f1-304dea8762db.csv";
    static final String TEST_FILE_DIRECTORY = "./data/reservations_test";

    final LocalDate startDate = LocalDate.of(2021, 7, 30);
    final LocalDate endDate = LocalDate.of(2021, 8, 10);

    final String hostId = "2e72f86c-b8fe-4265-b4f1-304dea8762db";

    ReservationFileRepo repo = new ReservationFileRepo(TEST_FILE_DIRECTORY);


    @BeforeEach
    void setUp() throws IOException {
        Path seedPath = Paths.get(SEED_FILE_PATH);
        Path testPath = Paths.get(TEST_FILE_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    void shouldFindByHostId() {
        List<Reservation> reservations = repo.findByHostId(hostId);
        assertEquals(12, reservations.size());

    }


    @Test
    void shouldNotFindMissing() {
        List<Reservation> result = repo.findByHostId("nope");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldAdd() throws DataException {
        Reservation reservation = new Reservation();
        reservation.setResId(13);
        reservation.setStartDate(startDate);
        reservation.setEndDate( endDate);
        reservation.setTotal(new BigDecimal("500"));

        Guest guest = new Guest();
        guest.setGuestId(777);
        reservation.setGuest(guest);

        Host host = new Host();
        host.setHostId("ABCD-EFGH-KLMN");
        reservation.setHost(host);

        Reservation actual= repo.add(reservation);
        assertNotNull(actual);
        assertEquals(13, actual.getResId());
    }


    @Test
    void shouldUpdateExisting() throws DataException{
        Reservation reservation = new Reservation();
        reservation.setResId(7);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setTotal(new BigDecimal("777"));

        Host host = new Host();
        host.setHostId(hostId);
        reservation.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(777);
        reservation.setGuest(guest);

        boolean actual = repo.update(reservation);


        assertTrue(actual);
        assertEquals(7, reservation.getResId());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());


    }

    @Test
    void shouldNotUpdateMissing() throws DataException{
        Host host = new Host();
        host.setHostId(hostId);

        Guest guest = new Guest();
        guest.setGuestId(70);

        Reservation reservation = new Reservation();
        reservation.setResId(777);
        reservation.setHost(host);
        reservation.setGuest(guest);

        boolean actual = repo.update(reservation);
        assertFalse(actual);
    }

    @Test
    void shouldDeleteExisting() throws DataException {
        boolean actual = repo.deleteById(hostId, 2);
        assertTrue(actual);

        Reservation r = repo.findByResId(hostId, 2) ;
        assertNull(r);

    }

    @Test
    void shouldNotDeleteMissing() throws DataException{
        boolean actual = repo.deleteById(hostId, 777);
        assertFalse(actual);
    }


}