package UI;

public enum MainMenuOption {
    EXIT(0, "Exit"),
    VIEW_RESERVATIONS_FOR_HOST(1, "View Reservations For a Host"),
    VIEW_HOSTS(2, "View Hosts by State"),
    MAKE_RESERVATION(3, "Make a Reservation"),
    EDIT_RESERVATION(4, "Edit a Reservation"),
    CANCEL_RESERVATION(5, "Cancel a Reservation");

    private int value;
    private String message;

    private MainMenuOption(int value, String message){
        this.value = value;
        this.message = message;
    }

    public static MainMenuOption fromValue(int value) {
        for (MainMenuOption option : MainMenuOption.values()) {
            if (option.getValue() == value) {
                return option;
            }
        }
        return EXIT;
    }
    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }


}
