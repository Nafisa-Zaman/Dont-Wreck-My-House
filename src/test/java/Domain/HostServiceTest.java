package Domain;

import Data.DataException;
import Data.HostRepoDouble;
import Models.Host;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HostServiceTest {
    HostService service = new HostService(new HostRepoDouble());

    @Test
    void shouldFindById() throws DataException{
        List<Host> result = service.findById(HostRepoDouble.HOST.getHostId());
        assertNotNull(result);
    }

    @Test
    void shouldNotFindMissing(){
        List<Host> host = service.findById("fake");
        assertTrue(host.isEmpty());

    }

    @Test
    void shouldFindByLastName() throws DataException{
        List<Host> result = service.findByLastName("Yearnes");
        assertNotNull(result);
    }

    @Test
    void shouldFindByState() throws DataException{
        List<Host> result = service.findByState("TX");
        assertNotNull(result);
    }



}