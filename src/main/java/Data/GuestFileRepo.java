package Data;

import Models.Guest;
import Models.Host;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GuestFileRepo implements GuestRepo {
    private static final String HEADER = "guest_id,first_name,last_name,email,phone,state";
    private final String filePath;

    public GuestFileRepo(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Guest> findAll() {
        ArrayList<Guest> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 6) {
                    result.add(deserialize(fields));
                }
            }
        } catch (IOException ex) {
            //don't thrpw yet
        }
        return result;
    }

    @Override
    public Guest findById(int guestId){
        return findAll().stream().filter(i -> i.getGuestId() == (guestId)).findFirst().orElse(null);
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




    private String serialize(Guest guest) {
        return String.format("%s,%s,%s,%s,%s,%s%n",
                guest.getGuestId(),
                guest.getFirstName(),
                guest.getLastName(),
                guest.getGuestEmail(),
                guest.getGuestPhone(),
                guest.getState());
    }


    private Guest deserialize(String[] fields) {
        Guest result = new Guest();
        result.setGuestId(Integer.parseInt(fields[0]));
        result.setFirstName(fields[1]);
        result.setLastName(fields[2]);
        result.setGuestEmail(fields[3]);
        result.setGuestPhone(fields[4]);
        result.setState(fields[5]);

        return result;

    }

    protected void writeAll(List<Guest> guests) throws DataException {
        try (PrintWriter writer = new PrintWriter(filePath)) {
            writer.println(HEADER);

            for (Guest guest : guests) {
                writer.println(serialize(guest));
            }

        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }


}
