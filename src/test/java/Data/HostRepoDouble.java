package Data;

import Models.Host;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class HostRepoDouble implements HostRepo {
    public static final Host HOST = selectHost();
    private final ArrayList<Host> hosts = new ArrayList<>();

    public HostRepoDouble() {
        hosts.add(HOST);
    }

    private static Host selectHost() {
        Host HOST = new Host();
        HOST.setHostId("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        HOST.setLastName("Yearnes");
        HOST.setEmail("eyearnes0@sfgate.com");
        HOST.setPhone("(806) 1783815");
        HOST.setAddress("3 Nova Trail");
        HOST.setCity("Amarillo");
        HOST.setState("TX");
        HOST.setPostCode("79182");
        HOST.setStandardRate(new BigDecimal(340));
        HOST.setWeekendRate(new BigDecimal(425));
        return HOST;
    }


    @Override
    public List<Host> findAll() {
        return hosts;
    }

    @Override
    public Host findById(String hostId) {
        return findAll().stream()
                .filter(i -> i.getHostId().equalsIgnoreCase(hostId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Host findByState(String state) {
        return findAll().stream().filter(s -> s.getState().equalsIgnoreCase(state))
                .findFirst().orElse(null);
    }

    @Override
    public Host findByLastName(String name) {
        return findAll().stream().filter(n -> n.getLastName().equalsIgnoreCase(name))
                .findFirst().orElse(null);

    }


}
