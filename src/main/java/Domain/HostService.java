package Domain;

import Data.HostRepo;
import Models.Host;

import java.util.List;
import java.util.stream.Collectors;

public class HostService {
    private final HostRepo repo;

    public HostService(HostRepo repo){
        this.repo = repo;
    }

    public List<Host> findById(String hostId){
        return repo.findAll().stream().filter(h -> h.getHostId().equals(hostId)).collect(Collectors.toList());
    }

    public List<Host> findByLastName(String name){
        return repo.findAll().stream().filter(n -> n.getLastName().startsWith(name)).collect(Collectors.toList());
    }

    public List<Host> findByState(String state){
        return repo.findAll().stream().filter(s -> s.getState().equals(state)).collect(Collectors.toList());
    }


}
