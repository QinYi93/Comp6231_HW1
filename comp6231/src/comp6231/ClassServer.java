package comp6231;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
//    		System.setProperty("java.rmi.server.hostname","192.168.1.2");
        Registry registry = LocateRegistry.createRegistry(location.getPort());
        registry.bind(location.toString(), this);
    }

    public void openUDPListener(){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket(this.location.getPort());
            byte[] buffer = new byte[1000];
            while (true){
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);
                byte[] b = request.getData();
                String value = new String(b).trim();
                System.out.print(value);
                if (value.length() > 0 && value.equals("RecordCounts")){
                    this.writeToLog("New thread starts for:" + request.getData().toString());
                    new thread(aSocket, request, this);
                }
            }
        }catch (SocketException e) {

        }catch (IOException e){

        }finally {
            if (aSocket != null){
                aSocket.close();
            }
        }
    }

    static class thread extends Thread{
        DatagramSocket socket = null;
        DatagramPacket request = null;
        ClassServer server = null;

        String recordCount;

        public thread(DatagramSocket socket, DatagramPacket request, ClassServer server) throws IOException {
            this.socket = socket;
            this.request = request;
            this.server = server;

//            if (request.getData().toString().equals("RecordCounts")) {
				recordCount = server.getLocation().toString() + " " + server.getTotalCount() + " ";
				this.start();
//			}
        }

        @Override
        public void run() {
            DatagramPacket reply = new DatagramPacket(recordCount.getBytes(),
                    recordCount.getBytes().length, request.getAddress(), request.getPort());
            try {
                socket.send(reply);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public synchronized String createTRecord(String firstName, String lastName, String address,
                                String phone, PublicParameters.Specialization specialization,
                                PublicParameters.Location location) throws RemoteException, IOException {
        Record TRecord = new TeacherRecord(firstName, lastName, address, phone, specialization, location);
        String Log1 = "Write a new Teacher Record, the record ID is \t" + TRecord.getRecordId();
        this.writeToLog(Log1);
        System.out.println(Log1);
        char key = lastName.charAt(0);
        if (recordData.get(key) == null || !recordData.containsKey(key)){
            recordData.put(key, new LinkedList<Record>());
        }
        if (recordData.get(key).add(TRecord)){
            String log2 = "Write new Teacher Record Successful \t" + "the key is \t" +key;
            this.writeToLog(log2);
            System.out.println(log2);
            TRCount++;
            return log2;
        }
        return "Fail to create TRecord";

    }

    @Override
    public synchronized String createSRecord(String firstName, String lastName,
                                PublicParameters.CoursesRegistered coursesRegistered,
                                PublicParameters.Status status, Date statusDate)
            throws IOException, RemoteException {
        Record record = new StudentRecord(firstName, lastName, coursesRegistered, status, statusDate);
        String log1 = "Write a new Student Record, the record ID is \t" + record.getRecordId();
        this.writeToLog(log1);
        System.out.println(log1);
        char key = lastName.charAt(0);
        if (!recordData.containsKey(key) || recordData.get(key).equals(null))
            recordData.put(key, new LinkedList<Record>());
        if (recordData.get(key).add(record)){
            String log2 = "Write new Student Record Successful \t" + "the key is \t" + key;
            this.writeToLog(log2);
            System.out.println(log2);
            SRCount++;
            return log2;
        }
        return "Fail to create SRecord";
    }

    @Override
    public String getRecordCounts() throws IOException, RemoteException {
        this.writeToLog("try to count all record at \t" + location.toString());
        String output = this.location.toString() + " " + getTotalCount() + "  ";
        for (ClassServer classServer : ServerManagerSystem.serverArrayList) {
            if (classServer.location != this.getLocation()){
                output += requestRecordCounts(classServer) + "  ";
            }
        }
        return output;
    }

    public String requestRecordCounts(ClassServer server){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] message = "RecordCounts".getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = server.getLocation().getPort();
            DatagramPacket request = new DatagramPacket(message,
                    message.length, aHost, serverPort);
            aSocket.send(request);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);
            String str = new String(reply.getData()).trim();
            return str;

        }catch (SocketException e){
            System.out.println("Socket" + e.getMessage());
        }catch (IOException e){
            System.out.println("IO:" + e.getMessage());
        }finally {
            if (aSocket != null){
                aSocket.close();
            }
        }
        return null;
    }

    @Override
    public String editRecord(String recordID, String fieldName, String newValue)
            throws IOException{
        this.writeToLog("try to edit record for" + recordID);
        String type = recordID.substring(0,2).toUpperCase();
        //decides weather it is a Teacher record or Student record
        if (type.equals("TR")){
            String normalFieldName = fieldName.toLowerCase().trim();
            if (normalFieldName.equals("address") || normalFieldName.equals("phone")
                    || normalFieldName.equals("location")){
            	findRecordToEdit(recordID, fieldName, newValue, 'T');
                return "editRocord \t" + recordID + " \t Succeddfully";
            }
        }
        if (type.equals("SR")){
            String normalFieldName = fieldName.toLowerCase().trim();
            if (normalFieldName.equals("courseregistered") || normalFieldName.equals("status")
                    || normalFieldName.equals("status date")){
                findRecordToEdit(recordID, fieldName, newValue, 'S');
                return "editRocord \t" + recordID + " \t Succeddfully";
            }
        }
        return "fail to edit" + recordID;
    }

    public void writeToLog(String message) throws IOException{
        FileWriter fileWriter = new FileWriter(logFile, true);
        fileWriter.write(message + "\n");
        fileWriter.flush();
        fileWriter.close();
    }

    public synchronized void findRecordToEdit(String recordId, String fieldName, String newValue, char recordType) throws IOException{
        for (LinkedList<Record> recordLinkedList : recordData.values()) {
            LinkedList<Record> checkRecord = recordLinkedList;
            for (Record record : checkRecord) {
            	System.out.println("get information before editing");
                if(record.getRecordId().equals(recordId)){
                    if (recordType == 'T') {
                    	System.out.println("recordId is \t" + recordId + "\t address \t" 
                    + ((TeacherRecord)record).getAddress() + "\t phone\t"
                    + ((TeacherRecord)record).getPhone() + "\t location \t"
                    + ((TeacherRecord)record).getLocation()); 
                        switch (fieldName) {
                            case "address":
                                this.writeToLog("change recordID \t" + recordId
                                       + "\t address"+ "\t" + ((TeacherRecord)record).getAddress()
                                       + "\t to \t" + newValue);
                                System.out.println("change recordID \t" + recordId
                                        + "\t address \t" + ((TeacherRecord)record).getAddress()
                                        + "\t to \t" + newValue);
                                ((TeacherRecord)record).setAddress(newValue);
                                break;
                            case "phone":
                                this.writeToLog("change recordID \t" + recordId
                                        + "\t phone \t" + ((TeacherRecord)record).getPhone() 
                                        + "\t to \t" + newValue);
                                System.out.println("change recordID \t" + recordId
                                        + "\t address \t" + ((TeacherRecord)record).getPhone() 
                                        + "\t to \t" + newValue);
                                ((TeacherRecord)record).setPhone(newValue);
                                break;
                            case "location":
                                if (newValue.equals("MTL") || newValue.equals("LVL") || newValue.equals("DDO")){
                                    this.writeToLog("change recordID \t" + recordId
                                            + "\t location \t" + ((TeacherRecord)record).getLocation() 
                                            + "\t to \t" + newValue);
                                    System.out.println("change recordID \t" + recordId
                                            + "\t location \t" + ((TeacherRecord)record).getLocation() 
                                            + "\t to \t" + newValue);
                                    ((TeacherRecord)record).setLocation(newValue);
                                }
                                break;
                            default:
                                System.out.println("Invalid input!");
                        }
                        System.out.println("After edit recordId is \t" + recordId + "\t address \t" 
                                + ((TeacherRecord)record).getAddress() + "\t phone\t"
                                + ((TeacherRecord)record).getPhone() + "\t location \t"
                                + ((TeacherRecord)record).getLocation()); 
                    }
                    if (recordType == 'S'){
                    	System.out.println("recordId is \t" + recordId + "\t courseregistered \t" 
                                + ((StudentRecord)record).getCoursesRegistered() + "\t status\t"
                                + ((StudentRecord)record).getStatus() + "\t status date \t"
                                + ((StudentRecord)record).getStatusDate()); 
                        switch (fieldName){
                            case "courseregistered":
                                String message1 = "change recordID" + recordId
                                        + "courseregistered" + ((StudentRecord)record).getCoursesRegistered()
                                        + "to" + newValue;
                                this.writeToLog(message1);
                                System.out.println(message1);
                                ((StudentRecord)record).setCoursesRegistered(PublicParameters.CoursesRegistered.valueOf(newValue));
                                break;
                            case "status":
                                String message2 = "change recordID" + recordId
                                        + "status" + ((StudentRecord)record).getStatus()
                                        + "to" + newValue;
                                this.writeToLog(message2);
                                System.out.println(message2);
                                ((StudentRecord)record).setStatus(PublicParameters.Status.valueOf(newValue));
                                break;
                            case "status date":
                                String message3 = "change recordID" + recordId
                                        + "status date" + ((StudentRecord)record).getStatusDate()
                                        + "to" + newValue;
                                writeToLog(message3);
                                System.out.println(message3);
                                ((StudentRecord)record).setStatusDate(newValue);
                                break;
                            default:
                                System.out.println("Invalid input!");
                        }
                     	System.out.println("After editing -recordId is \t" + recordId + "\t courseregistered \t" 
                                + ((StudentRecord)record).getCoursesRegistered() + "\t status\t"
                                + ((StudentRecord)record).getStatus() + "\t status date \t"
                                + ((StudentRecord)record).getStatusDate()); 
                    }
                }
            }
        }
    }

    /**
     * the communication between two servers in order to add or remove a record
      */
    public void requestCreateRecord(ClassServer server, Record record){
        DatagramSocket aSocket = null;
        try {
            aSocket = new DatagramSocket();
            byte[] message = "CreateRecord".getBytes();
            InetAddress aHost = InetAddress.getByName("localhost");
            int serverPort = server.getLocation().getPort();
            DatagramPacket request = new DatagramPacket(message, message.length, aHost, serverPort);
            aSocket.send(request);

            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            aSocket.receive(reply);

            System.out.println(reply.getData().toString());

        }catch (SocketException e){
            System.out.println("Socket" + e.getMessage());

        }catch (IOException e){
            System.out.println("IO:" +e.getMessage());

        }finally {
            if (aSocket != null)
                aSocket.close();
        }


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
