package UI;

import Data.DataException;
import Models.Guest;
import Models.Host;
import Models.Reservation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class View {
    private final ConsoleIO io;

    public View(ConsoleIO io) {
        this.io = io;
    }

    public MainMenuOption selectMainMenuOption() {
        displayHeader("Main Menu");

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (MainMenuOption option : MainMenuOption.values()) {
            io.printf("%s. %s%n", option.getValue(), option.getMessage());
            min = Math.min(min, option.getValue());
            max = Math.max(max, option.getValue());
        }
        String message = String.format("Select [%s-%s]: ", min, max);
        return MainMenuOption.fromValue(io.readInt(message, min, max));
    }

    public String getHost(){
        return io.readString("Search Host by Id:");

    }

    public String getHostState(){
        return io.readString("Seach Host by State");
    }



    public Host viewHost(List<Host> hosts){
        if(hosts.size() == 0){
            io.println("No Hosts found.");
            return null;
        }
        int index = 1;
        for (Host host : hosts.stream().limit(25).collect(Collectors.toList())) {
            io.printf("%s: Surname: %s,  Email: %s,  Address: %s-%s-%s, State: %s, Phone: %s, Rate: £%s, Weekend Rate: £%s%n",
                    index++,
                    host.getLastName(),
                    host.getEmail(),
                    host.getAddress(),
                    host.getCity(),
                    host.getPostCode(),
                    host.getState(),
                    host.getPhone(),
                    host.getStandardRate(),
                    host.getWeekendRate());
        }
        index--;

        if (hosts.size() > 25) {
            io.println("More than 25 hosts found. Showing first 25. Please refine your search.");
        }
        io.println("0: Exit");
        String message = String.format("Select a host by their index [0-%s]: ", index);

        index = io.readInt(message, 0, index);
        if (index <= 0) {
            return null;
        }
        return hosts.get(index - 1);
    }


    public Guest getGuestId(List<Guest> guests){
        int guestId = io.readInt("Guest Id");
        Guest guest = guests.stream().filter(g -> g.getGuestId() == guestId).findFirst().orElse(null);

        if(guest == null){
            displayStatus(false, String.format("No Guest with Id %s found.", guestId));
        }

        return guest;

    }

    public Reservation findReservation(List<Reservation> reservations) throws DataException{
        displayReservations(reservations);

        if(reservations.size() == 0){
            return null;
        }

        int resId = io.readInt("Reservation Id: ");
        for (Reservation r : reservations){
            if(r.getResId() == resId){
                return r;
            }
        }
        System.out.println("Reservation not found");
        return null;
    }

    public Reservation makeReservation(Host host, Guest guest){
        Reservation reservation = new Reservation();
        reservation.setHost(host);
        reservation.setGuest(guest);
        reservation.setStartDate(io.readLocalDate("Start Date [mm/dd/yyyy]: "));
        reservation.setEndDate(io.readLocalDate("End Date [mm/dd/yyyy]: "));
        return reservation;
    }

    public void summary(Reservation reservation){
        displayHeader("Summary");
        System.out.println("Start Date: " + reservation.getStartDate());
        System.out.println("End Date: " + reservation.getEndDate());
        System.out.println("Total: " + reservation.getTotal());
        System.out.println("Confirm? : ");
        io.readBoolean("[y/n]");

    }

    public Reservation update(List<Reservation> reservations) throws DataException{
        Reservation reservation = findReservation(reservations);
        if(reservation != null){
            update(reservation);
        }
        return reservation;
    }

    private Reservation update(Reservation reservation){
        LocalDate startDate = io.readLocalDate("Start Date: [mm/dd/yyyy]");

        if(startDate.isAfter(LocalDate.now())){
            reservation.setStartDate(startDate);
        }

        LocalDate endDate = io.readLocalDate("End Date: [mm/dd/yyyy]");

        if(endDate.isAfter(startDate) || endDate.isEqual(startDate)){
            reservation.setEndDate(endDate);
        }
        return reservation;


    }

    public void displayReservations(List<Reservation> reservations){
        if(reservations == null || reservations.isEmpty()){
            io.println("No Reservations found.");
        }
        for (Reservation reservation:reservations){
            io.printf("Res ID: %s, Guest ID: %s  Guest: %s %s  Email: %s  Dates: %s - %s Total: £%.2f%n",
                    reservation.getResId(),
                    reservation.getGuest().getGuestId(),
                    reservation.getGuest().getFirstName(),
                    reservation.getGuest().getLastName(),
                    reservation.getGuest().getGuestEmail(),
                    reservation.getStartDate(),
                    reservation.getEndDate(),
                    reservation.getTotal());
        }
    }


    public void enterToContinue() {
        io.readString("Press [Enter] to continue.");
    }

    // display only
    public void displayHeader(String message) {
        io.println("");
        io.println(message);
        io.println("=".repeat(message.length()));
    }

    public void displayException(Exception ex) {
        displayHeader("A critical error occurred:");
        io.println(ex.getMessage());
    }

    public void displayStatus(boolean success, String message) {
        displayStatus(success, List.of(message));
    }

    public void displayStatus(boolean success, List<String> messages) {
        displayHeader(success ? "Success" : "Error");
        for (String message : messages) {
            io.println(message);
        }
    }

    public void displayHosts(List<Host> hosts){
        if(hosts.size() == 0){
            io.println("No Hosts Found");
        }

        for (Host host : hosts){
            io.printf(" Surname: %s,  Email: %s,  Address: %s-%s-%s, State: %s, Phone: %s, Rate: £%s, Weekend Rate: £%s",
                    host.getLastName(),
                    host.getEmail(),
                    host.getAddress(),
                    host.getCity(),
                    host.getPostCode(),
                    host.getState(),
                    host.getPhone(),
                    host.getStandardRate(),
                    host.getWeekendRate());
        }
    }


}
