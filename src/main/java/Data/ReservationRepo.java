package Data;

import Models.Reservation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ReservationRepo {
    List<Reservation> findByHostId(String hostId);

    Reservation findByResId(String hostId, int resId) throws DataException;

    Reservation add(Reservation reservation) throws DataException;

    boolean update(Reservation reservation) throws DataException;

    boolean deleteById(String hostId, int resId) throws DataException;
}
