package assisgnment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class ClassServer extends UnicastRemoteObject implements DcmsInterface {
    private File logFile = null;
    private HashMap<Character, LinkedList<Record>> recordData;
    private PublicParameters.Location location;
    private int TRCount = 0, SRCount = 0;

    protected ClassServer(PublicParameters.Location loc) throws RemoteException,IOException {
        this.location = loc;
        logFile = new File(loc + ".txt");
        if (!logFile.exists()){
            logFile.createNewFile();
        }
        recordData = new HashMap<Character, LinkedList<Record>>();
    }

    public void exportServer() throws Exception{
        Registry registry = LocateRegistry.createRegistry(location.getPort());
        registry.bind(location.toString(), this);
    }


    @Override
    public String createTRecord(String firstName, String lastName, String address,
                                String phone, PublicParameters.Specialization specialization,
                                PublicParameters.Location location) throws RemoteException, IOException {
        Record TRecord = new TeacherRecord(firstName, lastName, address, phone, specialization, location);
        String Log1 = "Write a new Teacher Record, the record ID is" + TRecord.getRecordId();
        this.writeToLog(Log1);
        System.out.println(Log1);
        char key = lastName.charAt(0);
        if (recordData.get(key).equals(null) || !recordData.containsKey(key)){
            recordData.put(key, new LinkedList<Record>());
        }
        if (recordData.get(key).add(TRecord)){
            String log2 = "Write new Teacher Record Successfult" + "the key is" +key;
            this.writeToLog(log2);
            System.out.println(log2);
            TRCount++;
            return log2;
        }
        return "Fail to create TRecord";

    }

    @Override
    public String createSRecord(String firstName, String lastName,
                                PublicParameters.CoursesRegistered coursesRegistered,
                                PublicParameters.Status status, Date statusDate)
            throws IOException, RemoteException {
        Record record = new StudentRecord(firstName, lastName, coursesRegistered, status, statusDate);
        String log1 = "Write a new Student Record, the record ID is" + record.getRecordId();
        this.writeToLog(log1);
        System.out.println(log1);
        char key = lastName.charAt(0);
        if (!recordData.containsKey(key) || recordData.get(key).equals(null))
            recordData.put(key, new LinkedList<Record>());
        if (recordData.get(key).add(record)){
            String log2 = "Write new Student Record Successfule" + "the key is" + key;
            this.writeToLog(log2);
            System.out.println(log2);
            SRCount++;
            return log2;
        }
        return "Fail to create SRecord";
    }

    @Override
    public String getRecordCounts() throws IOException, RemoteException {
        String message = "";
        for (ClassServer classServer : ServerManagerSystem.serverArrayList) {
            message = classServer.getLocation().toString() + getTotalCount() + "\n";
        }
        this.writeToLog("The count number in server" + message);
        System.out.println("The count number in server" + message);
        return message;
    }

    @Override
    public String editRecord(String recordID, String fieldName, String newValue)
            throws IOException, RemoteException {
        return null;
    }

    public synchronized void writeToLog(String message) throws IOException{
        FileWriter fileWriter = new FileWriter(logFile, true);
        fileWriter.write(message + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    public int getTotalCount(){
        return getTRCount() + getSRCount();
    }

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public HashMap<Character, LinkedList<Record>> getRecordData() {
        return recordData;
    }

    public void setRecordData(HashMap<Character, LinkedList<Record>> recordData) {
        this.recordData = recordData;
    }

    public PublicParameters.Location getLocation() {
        return location;
    }

    public void setLocation(PublicParameters.Location location) {
        this.location = location;
    }

    public int getTRCount() {
        return TRCount;
    }

    public void setTRCount(int TRCount) {
        this.TRCount = TRCount;
    }

    public int getSRCount() {
        return SRCount;
    }

    public void setSRCount(int SRCount) {
        this.SRCount = SRCount;
    }
}
