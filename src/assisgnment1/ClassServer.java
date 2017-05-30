package assisgnment1;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class ClassServer extends UnicastRemoteObject implements DcmsInterface {
    protected ClassServer() throws RemoteException {
    }

    @Override
    public String createTRecord(String firstName, String lastName, String address, String phone, PublicParameters.Specialization specialization, PublicParameters.Location location) throws RemoteException, IOException {
        return null;
    }

    @Override
    public String createSRecord(String firstName, String lastName, PublicParameters.CoursesRegistered coursesRegistered, PublicParameters.Status status, Date statusDate) throws IOException, RemoteException {
        return null;
    }

    @Override
    public String getRecordCounts() throws IOException, RemoteException {
        return null;
    }

    @Override
    public String editRecord(String recordID, String fieldName, String newValue) throws IOException, RemoteException {
        return null;
    }
}
