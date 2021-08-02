package Data;

import Models.Host;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class HostFileRepoTest {
    HostFileRepo repo = new HostFileRepo(TEST_PATH);
    static final String SEED_PATH = "./data/hosts-seed.csv";
    static final String TEST_PATH = "./data/hosts-test.csv";

    @BeforeEach
    void setUp() throws IOException{
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }


    @Test
    void shouldFindAll(){
        assertEquals(1000 , repo.findAll().size());
    }

    @Test
    void shouldFindById(){
        Host host = repo.findById("3edda6bc-ab95-49a8-8962-d50b53f84b15");
        assertNotNull(host);
    }

    @Test
    void shouldFindByState(){
        Host host = repo.findByState("TX");
        assertNotNull(host);

    }


    @Test
    void shouldFindByLastName(){
        Host host = repo.findByLastName("Yearnes");
        assertNotNull(host);

    }


    @Test
    void shouldNotFindMissing() {
        Host nope = repo.findById("abcde12345");
        assertNull(nope);
    }



    }

