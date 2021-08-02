package Data;

import Models.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestRepoDouble implements GuestRepo {
    public final static Guest GUEST = selectGuest();
    private final ArrayList<Guest> guests = new ArrayList<>();

    public GuestRepoDouble(){
        guests.add(GUEST);
    }

    public static Guest selectGuest(){
        Guest GUEST = new Guest();
        GUEST.setGuestId(1);
        GUEST.setFirstName("Sullivan");
        GUEST.setLastName("Lomas");
        GUEST.setGuestEmail("slomas0@mediafire.com");
        GUEST.setGuestPhone("(702) 7768761");
        GUEST.setState("NV");
        return GUEST;
    }



    @Override
    public List<Guest> findAll() {
        return guests;
    }

    @Override
    public Guest findById(int guestId){
        return findAll().stream()
                .filter(i -> i.getGuestId() == (guestId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Guest findByState(String state){
        return findAll().stream().filter(s -> s.getState().equalsIgnoreCase(state))
                .findFirst().orElse(null);
    }

    @Override
    public Guest findByLastName(String name){
        return findAll().stream().filter(n -> n.getLastName().equalsIgnoreCase(name))
                .findFirst().orElse(null);

    }


}
