package Domain;

import Data.GuestRepo;
import Models.Guest;

import java.util.List;
import java.util.stream.Collectors;

public class GuestService {
    private final GuestRepo repo;

    public GuestService(GuestRepo repo) {
        this.repo = repo;
    }

    public List<Guest> findById(int guestId) {
        return repo.findAll().stream().filter(i -> i.getGuestId() == guestId).collect(Collectors.toList());

    }

    public List<Guest> findByLastName(String name){
        return repo.findAll().stream().filter(n -> n.getLastName().startsWith(name)).collect(Collectors.toList());
    }

    public List<Guest> findByState(String state){
        return repo.findAll().stream().filter(s -> s.getState().equals(state)).collect(Collectors.toList());
    }
}
