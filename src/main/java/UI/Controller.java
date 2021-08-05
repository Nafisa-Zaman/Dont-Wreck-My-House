package UI;

import Data.DataException;
import Data.ReservationFileRepo;
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
                case VIEW_HOSTS:
                    viewHosts();
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

    private void viewHosts(){
        view.displayHeader(MainMenuOption.VIEW_HOSTS.getMessage());
        String state = view.getHostState();
        List<Host> hosts = hostService.findByState(state);
        view.displayHeader("Host");
        view.viewHost(hosts);
        view.enterToContinue();
    }


    private void makeReservation() throws DataException {
        view.displayHeader(MainMenuOption.MAKE_RESERVATION.getMessage());
        Host host = getHost();
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
        String hostId =view.getHost();

        List<Reservation> reservations = reservationService.findByHostId(hostId);

        Reservation reservation = view.findReservation(reservations);
        if(reservation == null){
            return;}


        Result<Reservation> result = reservationService.update(reservation);
        if(!result.isSuccess()){
            view.displayStatus(false, result.getErrorMessages());
        }
        else{
            view.summary(reservation);
            String successMessage = "Reservation Successfully updated!";
            view.displayStatus(true, successMessage);
        }
    }

    private void cancelReservation() throws DataException {
        view.displayHeader(MainMenuOption.CANCEL_RESERVATION.getMessage());
        String hostId = view.getHost();
        List<Reservation> reservations = reservationService.findByHostId(hostId);

        Reservation reservation = view.findReservation(reservations);
        if(reservation == null){
            return;}

        Result<Reservation> result = reservationService.deleteById(reservation.getHost().getHostId(),reservation.getResId());
        if (!result.isSuccess()) {
            view.displayStatus(false, result.getErrorMessages());
        } else {
            String successMessage = "Reservation Successfully cancelled!";
            view.displayStatus(true, successMessage);
        }




        view.enterToContinue();
    }


    private Host getHost() {

        String id = view.getHost();
        List<Host> hosts = hostService.findById(id);

        return view.viewHost(hosts);
    }

    private Guest getGuest() {
        int guestId = 0;
        List<Guest> guests = guestService.findById(guestId);
        return view.getGuestId(guests);
    }





}
