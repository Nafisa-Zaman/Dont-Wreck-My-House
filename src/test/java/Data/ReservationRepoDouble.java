package Data;

import Models.Reservation;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ReservationRepoDouble implements ReservationRepo {
    final LocalDate startDate = LocalDate.of(2023, 8, 1);
    final LocalDate endDate = LocalDate.of(2023, 8, 7);

    private final ArrayList<Reservation> reservations = new ArrayList<>();

    public ReservationRepoDouble() {
        Reservation reservation = new Reservation();
        reservation.setHost(HostRepoDouble.HOST);
        reservation.setResId(13);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setGuest(GuestRepoDouble.GUEST);
        reservation.setTotal(new BigDecimal(2000));
        reservations.add(reservation);

        Reservation test = new Reservation();
        reservation.setHost(HostRepoDouble.HOST);
        reservation.setResId(14);
        reservation.setStartDate(LocalDate.of(2020, 5, 16));
        reservation.setEndDate(LocalDate.of(2020, 5, 20));
        reservation.setGuest(GuestRepoDouble.GUEST);
        reservation.setTotal(new BigDecimal(2000));
        reservations.add(test);


    }


    @Override
    public List<Reservation> findByHostId(String hostId) {
        return reservations.stream().filter(i -> i.getHost().getHostId().equals(hostId)).collect(Collectors.toList());
    }


    public Reservation findByResId(String hostId, int resId) throws DataException {
        for (Reservation r : reservations) {
            if (r.getResId() == resId) {
                return r;
            }
        }
        return null;
    }


    @Override
    public Reservation add(Reservation reservation) throws DataException {
        reservations.add(reservation);
        return reservation;

    }

    @Override
    public boolean update(Reservation reservation) throws DataException {

        return true;
    }

    @Override
    public boolean deleteById(String hostId, int resId) throws DataException {
        return true;
    }
}

