package Data;

import Models.Guest;

import java.util.List;

public interface GuestRepo {
    List<Guest> findAll();

    Guest findById(int guestId);

    Guest findByState(String state);

    Guest findByLastName(String name);

}
