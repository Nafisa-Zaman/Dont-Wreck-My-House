package Data;

import Models.Host;

import java.util.List;

public interface HostRepo {
    List<Host> findAll();

    Host findById(String hostId);

    Host findByState(String state);

    Host findByLastName(String name);


}
