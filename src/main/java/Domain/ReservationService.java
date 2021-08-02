package Domain;

import Data.DataException;
import Data.GuestRepo;
import Data.HostRepo;
import Data.ReservationRepo;
import Models.Guest;
import Models.Host;
import Models.Reservation;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private final ReservationRepo reservationRepo;
    private final HostRepo hostRepo;
    private final GuestRepo guestRepo;


    public ReservationService(ReservationRepo reservationRepo, HostRepo hostRepo, GuestRepo guestRepo) {
        this.reservationRepo = reservationRepo;
        this.hostRepo = hostRepo;
        this.guestRepo = guestRepo;
    }


    public List<Reservation> findByHostId(String hostId) {
        Map<String, Host> hostMap = hostRepo.findAll().stream().collect(Collectors.toMap(i -> i.getHostId(), i -> i));

        Map<Integer, Guest> guestMap = guestRepo.findAll().stream().collect(Collectors.toMap(i -> i.getGuestId(), i -> i));

        List<Reservation> result = reservationRepo.findByHostId(hostId);
        for (Reservation reservation : result) {
            reservation.setHost(hostMap.get(reservation.getHost().getHostId()));
            reservation.setGuest(guestMap.get(reservation.getGuest().getGuestId()));
        }
        return result;
    }

    public List<Reservation> findByResId(int resId, String hostId) {
        return reservationRepo.findByHostId(hostId).stream().filter(r -> r.getResId() == resId).collect(Collectors.toList());
    }

    public Result<Reservation> add(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            return result;
        }
        reservation.setTotal(calculateTotal(reservation.getStartDate(), reservation.getEndDate(), reservation.getHost().getHostId()));
        result.setPayload(reservationRepo.add(reservation));
        return result;
    }

    public Result<Reservation> update(Reservation reservation) throws DataException {
        Result<Reservation> result = validate(reservation);
        if (!result.isSuccess()) {
            result.addErrorMessage("Invalid Input");
        }
        Reservation existing = reservationRepo.findByResId(reservation.getHost().getHostId(), reservation.getResId());
        if (existing == null) {
            result.addErrorMessage("Reservation Id not found.");
            return result;
        }
        boolean success = reservationRepo.update(reservation);
        if (!success) {
            result.addErrorMessage("Error");
        }

        return result;

    }

    public Result<Reservation> deleteById(String hostId, int resId) throws DataException {
        Result<Reservation> result = new Result<>();
        Reservation reservation = reservationRepo.findByResId(hostId, resId);
        validateDomain(reservation, result);

        if (!result.isSuccess()) {
            return null;
        }

        boolean success = reservationRepo.deleteById(reservation.getHost().getHostId(), reservation.getResId());
        if (!success) {
            result.addErrorMessage("Could not find Reservation ID.");
        }

        return result;


    }


    private Result<Reservation> validate(Reservation reservation) {

        Result<Reservation> result = validateNulls(reservation);
        if (!result.isSuccess()) {
            return result;
        }

        validateFields(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }

        validateChildrenExist(reservation, result);

        validateNoOverlap(reservation, result);
        if (!result.isSuccess()) {
            return result;
        }


        return result;
    }

    private Result<Reservation> validateNulls(Reservation reservation) {
        Result<Reservation> result = new Result<>();

        if (reservation == null) {
            result.addErrorMessage("Nothing to Save.");
        }

        assert reservation != null;
        if (reservation.getHost() == null) {
            result.addErrorMessage("Host is required");
        }

        if (reservation.getGuest() == null) {
            result.addErrorMessage("Guest is required");
        }

        if (reservation.getStartDate() == null) {
            result.addErrorMessage("Start Date is required");
        }

        if (reservation.getEndDate() == null) {
            result.addErrorMessage("End Date is required");
        }

        return result;
    }

    private void validateFields(Reservation reservation, Result<Reservation> result) {
        if (reservation.getStartDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Start Date cannot be in the past.");
        }

        if (reservation.getStartDate().isAfter(reservation.getEndDate())) {
            result.addErrorMessage("Start Date must come before the End Date.");
        }
    }


    private void validateChildrenExist(Reservation reservation, Result<Reservation> result) {
        if (reservation.getHost().getHostId() == null
                || hostRepo.findById(reservation.getHost().getHostId()) == null) {
            result.addErrorMessage("Host does not exist.");
        }

        if (guestRepo.findById(reservation.getGuest().getGuestId()) == null) {
            result.addErrorMessage("Guest does not exist.");
        }
    }

    private void validateNoOverlap(Reservation reservation, Result<Reservation> result) {


        List<Reservation> reservations = reservationRepo.findByHostId(reservation.getHost().getHostId());

        for (Reservation r : reservations) {


            if (!(r.getStartDate().compareTo(reservation.getEndDate()) >= 0
                    || r.getEndDate().compareTo(reservation.getStartDate()) <= 0)) {
                result.addErrorMessage("Dates overlap existing bookings for this Host.");
            }
        }

    }

    private void validateDomain(Reservation reservation, Result<Reservation> result) {
        if (reservation.getEndDate().isBefore(LocalDate.now())) {
            result.addErrorMessage("Cannot cancel a past reservation.");
        }
    }

    public BigDecimal calculateTotal(LocalDate startDate, LocalDate endDate, String hostId) {
        Host host = hostRepo.findById(hostId);

        int weekendDays = 0;

        int weekDays = 0;

        LocalDate dateCheck = startDate;
        do {
            if (dateCheck.getDayOfWeek() == DayOfWeek.SATURDAY
                    || dateCheck.getDayOfWeek() == (DayOfWeek.SUNDAY)) {
                weekendDays++;
            } else {
                weekDays++;
            }
            dateCheck = dateCheck.plusDays(1);
        }
        while (dateCheck.isBefore(endDate));


        BigDecimal total = (host.getStandardRate().multiply(new BigDecimal(weekDays)))
                .add(host.getWeekendRate().multiply(new BigDecimal(weekendDays)));
        return total;


    }


}








