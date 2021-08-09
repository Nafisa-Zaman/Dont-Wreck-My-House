package Data;

import Models.Reservation;
import Models.Guest;
import Models.Host;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ReservationFileRepo implements ReservationRepo {
    private static final String HEADER = "id,start_date,end_date,guest_id,total";
    private final String directory;



    public ReservationFileRepo(String directory) {
        this.directory = directory;
    }

    @Override
    public List<Reservation> findByHostId(String hostId) {
        ArrayList<Reservation> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath(hostId)))) {
            reader.readLine();

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] fields = line.split(",", -1);
                if (fields.length == 5) {
                    result.add(deserialize(fields, hostId));
                }
                else{return null;}
            }


        }catch (IOException ex){
            //don't throw on read
        }
        return result;
    }

    @Override
    public Reservation findByResId(String hostId, int resId) throws DataException{
     for (Reservation reservation : findByHostId(hostId)){
         if(reservation.getResId() == resId){
             return reservation;
         }
     }
     return null;
    }





    @Override
    public Reservation add(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getHostId());
        int resId = all.stream().mapToInt(Reservation::getResId).max().orElse(0) + 1;
        reservation.setResId(resId);
        all.add(reservation);
        writeAll(all, reservation.getHost().getHostId());
        return reservation;

    }

    @Override
    public boolean update(Reservation reservation) throws DataException {
        List<Reservation> all = findByHostId(reservation.getHost().getHostId());

            for (int i = 0; i < all.size(); i++) {
                if (all.get(i).getResId() == reservation.getResId()) {
                    all.set(i, reservation);
                    writeAll(all, reservation.getHost().getHostId());
                    return true;
                }
            }
            return false;

    }

    @Override
    public boolean deleteById(String hostId, int resId)  throws DataException {
        List<Reservation> all = findByHostId(hostId);
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getResId() == resId) {
                all.remove(i);
                writeAll(all, hostId);
                return true;
            }
        }

        return false;

    }


    private String getFilePath(String hostId) {
        return Paths.get(directory, hostId + ".csv").toString();
    }

    private void writeAll(List<Reservation> reservations, String hostId ) throws DataException {
        try (PrintWriter writer = new PrintWriter(getFilePath(hostId))) {
            writer.println(HEADER);

            for (Reservation reservation : reservations) {
                writer.println(serialize(reservation));
            }
        } catch (FileNotFoundException ex) {
            throw new DataException(ex);
        }
    }

    private String serialize(Reservation reservation) {
        return String.format("%s,%s,%s,%s,%s",
                reservation.getResId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getGuest().getGuestId(),
                reservation.getTotal());
    }

    private Reservation deserialize(String[] fields, String hostId) {
        Reservation result = new Reservation();
        result.setResId(Integer.parseInt(fields[0]));
        result.setStartDate(LocalDate.parse(fields[1]));
        result.setEndDate(LocalDate.parse(fields[2]));
        result.setTotal(new BigDecimal(fields[4]));


        Host host = new Host();
        host.setHostId(hostId);
        result.setHost(host);

        Guest guest = new Guest();
        guest.setGuestId(Integer.parseInt(fields[3]));
        result.setGuest(guest);

        return result;

    }

}
