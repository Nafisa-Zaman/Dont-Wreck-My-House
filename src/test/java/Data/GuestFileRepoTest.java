package Data;

import Models.Guest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static org.junit.jupiter.api.Assertions.*;

class GuestFileRepoTest {

    GuestFileRepo repo = new GuestFileRepo(TEST_PATH);
    static final String SEED_PATH = "./data/guests-seed.csv";
    static final String TEST_PATH = "./data/guests-test.csv";

    @BeforeEach
    void setUp() throws IOException{
        Path seedPath = Paths.get(SEED_PATH);
        Path testPath = Paths.get(TEST_PATH);
        Files.copy(seedPath, testPath, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void shouldFindAll(){
        assertEquals(1000, repo.findAll().size());
    }

    @Test
    void shouldFindById(){
        Guest guest = repo.findById(6);
        assertNotNull(guest);
    }

    @Test
    void shouldFindByState(){
        Guest guest = repo.findByState("FL");
        assertNotNull(guest);
    }

    @Test
    void shouldFindByLastName(){
        Guest guest = repo.findByLastName("Lomas");
        assertNotNull(guest);
    }

    @Test
    void shouldNotFindMissing(){
        Guest nah = repo.findById(2000);
        assertNull(nah);
    }


}