package Domain;

import Data.DataException;
import Data.GuestRepoDouble;
import Models.Guest;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GuestServiceTest {
    GuestService service = new GuestService(new GuestRepoDouble());

    @Test
    void shouldFindById() throws DataException{
        List<Guest> result = service.findById(GuestRepoDouble.GUEST.getGuestId());
        assertNotNull(result);
    }

    @Test
    void shouldNotFindMissing() throws DataException{
        List<Guest> result = service.findById(5050505);
        assertTrue(result.isEmpty());

    }

    @Test
    void shouldFindByLastName() throws DataException{
        List<Guest> result = service.findByLastName("Lomas");
        assertNotNull(result);
    }

    @Test
    void shouldFindByState() throws DataException{
        List<Guest> result = service.findByState("NV");
        assertNotNull(result);
    }



}