package UI;

import Data.DataException;
import Domain.GuestService;
import Domain.HostService;
import Domain.ReservationService;
import Domain.Result;
import Models.Guest;
import Models.Host;
import Models.Reservation;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private final HostService hostService;
    private final GuestService guestService;
    private final ReservationService reservationService;
    private final View view;

    public Controller(HostService hostService, GuestService guestService, ReservationService reservationService, View view) {
        this.hostService = hostService;
        this.guestService = guestService;
        this.reservationService = reservationService;
        this.view = view;
    }

    public void run() {
        view.displayHeader("Welcome to 'Don't Break My House'");
        try {
            runAppLoop();
        } catch (DataException ex) {
            view.displayHeader("Goodbye.");
        }
    }

    private void runAppLoop() throws DataException {
        MainMenuOption option;
        do {
            option = view.selectMainMenuOption();
            switch (option) {
                case VIEW_RESERVATIONS_FOR_HOST:
                    viewByHost();
                    break;
                case MAKE_RESERVATION:
                    makeReservation();
                    break;
                case EDIT_RESERVATION:
                    editReservation();
                    break;
                case CANCEL_RESERVATION:
                    cancelReservation();
                    break;
            }
        } while (option != MainMenuOption.EXIT);
    }

    private void viewByHost() { //show reservations for one host
        view.displayHeader(MainMenuOption.VIEW_RESERVATIONS_FOR_HOST.getMessage());
        String hostId = view.getHost();
        List<Reservation> reservations = reservationService.findByHostId(hostId);
        view.displayReservations(reservations);
        view.enterToContinue();
    }


    private void makeReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Host host = getHostState();
        if (host == null) {
            return;
        }
        Guest guest = getGuest();
        if (guest == null) {
            return;
        }
        Reservation reservation = view.makeReservation(host, guest);

        Result<Reservation> result = reservationService.add(reservation);
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            reservationService.calculateTotal(reservation.getStartDate(), reservation.getEndDate(), reservation.getHost().getHostId());
            view.summary(reservation);
            String successMessage = "Reservation Successfully create!";
            view.displayStatus(true, successMessage);
        }

    }

    private void editReservation() throws DataException {
        view.displayHeader(MainMenuOption.EDIT_RESERVATION.getMessage());
        Host host = getHostState();
        Guest guest = getGuest();



        Reservation reservation = getResByDate();
        reservation.setHost(host);
        reservation.setGuest(guest);

        Result<Reservation> result = reservationService.update(reservation);
        if(!result.isSuccess()){
            view.displayStatus(false, result.getErrorMessages());
        }
        else{
            view.summary(reservation);
            String successMessage = "Reservation Successfully create!";
            view.displayStatus(true, successMessage);
        }
    }

    private void cancelReservation() {
        view.displayHeader(MainMenuOption.CANCEL_RESERVATION.getMessage());
        //choose reservation
        //delete reservation by resID
        //success message
    }


    private Host getHostState() {

        String state = view.getHostState();
        List<Host> hosts = hostService.findByState(state);

        return view.viewHost(hosts);
    }

    private Guest getGuest() {
        int guestId = 0;
        List<Guest> guests = guestService.findById(guestId);
        return view.getGuestId(guests);
    }

    private Reservation getResByDate(){
        /*List<Reservation> hostId = reservationService.findByHostId(reservation.getHost().getHostId());*/

        /*List<Reservation> reservations = ;
        return view.getReservation(reservations);*/

        return null;
    }



}
