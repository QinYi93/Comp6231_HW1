package assisgnment1;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

/**
 * This class
 *
 * @author Yi Qin
 * @date ${date}
 */
public interface DcmsInterface extends Remote{
    public String createTRecord(String firstName, String lastName, String address,
                                String phone, PublicParameters.Specialization specialization, PublicParameters.Location location) throws RemoteException, IOException;
    public String createSRecord(String firstName, String lastName, PublicParameters.CoursesRegistered coursesRegistered,
                                PublicParameters.Status status, Date statusDate) throws IOException, RemoteException;
    public String getRecordCounts() throws IOException, RemoteException;
    public String editRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException;

}
