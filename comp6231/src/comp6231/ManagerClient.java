package comp6231;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import comp6231.PublicParameters.CoursesRegistered;
import comp6231.PublicParameters.Location;
import comp6231.PublicParameters.Status;

/**
 * This class access server depends on location.
 * Save the log file in corresponding location
 * @author Yi Qin
 * @date 2017-05-28
 */
public class ManagerClient{

    protected static int managerBaseID = 1000;
    private String managerID;
    private File log;
    private Registry registry;//each manger only have 1 registry, cannot linked to 2 servers
    private DcmsInterface intrfc;
    
    public ManagerClient(PublicParameters.Location location) {
        managerID = location.toString() + managerBaseID;
        log = new File(managerID + ".txt");
        if (!log.exists()){
            try {
                log.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
			registry = LocateRegistry.getRegistry(location.getPort());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        managerBaseID++;
    }

    public String getManagerID() {
        return managerID;
    }
    
    /**
      * This method each manager has its own log.
      * @param str
      * @throws IOException
     */
    public void writeToLog (String str) throws IOException{
    	 	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 	    Date date = new Date();
        FileWriter writer = new FileWriter(log , true);
        writer.write(format.format(date) + " " + str + "\n");
        writer.flush();
        writer.close();
    }
    
    /**
     * 
      * This method
      * 
      * @param firstName
      * @param lastName
      * @param address
      * @param phone
      * @param specialization
      * @param location
      * @throws NotBoundException
      * @throws IOException
     */
    public void createTRecord(String firstName, String lastName, String address,
                                String phone, PublicParameters.Specialization specialization, PublicParameters.Location location) throws NotBoundException, IOException {
		intrfc = (DcmsInterface)registry.lookup(managerID.substring(0,3));
		String reply = intrfc.createTRecord(firstName, lastName, address, phone, specialization, location);
		System.out.println(reply);
		writeToLog(reply);
	}
    
    /**
     * 
      * This method manager side call Server createSRecord through interface
      * 
      * @param firstName
      * @param lastName
      * @param coursesRegistered
      * @param status
      * @param string
      * @throws NotBoundException
      * @throws IOException
     * @throws ParseException 
     */
    public void createSRecord(String firstName, String lastName
    		, CoursesRegistered coursesRegistered, Status status, String date) throws NotBoundException, IOException, ParseException{
		intrfc = (DcmsInterface)registry.lookup(managerID.substring(0,3));
		Date statusDates = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		String reply = intrfc.createSRecord(firstName, lastName
				, coursesRegistered, status, statusDates);
		System.out.println(reply);
		writeToLog(reply);
	}
    /**
     * 
      * This method manager side call Server getRecordCounts through interface
      * 
      * @throws Exception
     */
    public void getRecordCounts() throws Exception{
		intrfc = (DcmsInterface)registry.lookup(managerID.substring(0,3));
		String reply = intrfc.getRecordCounts();
		System.out.println(reply);
		writeToLog(reply);
	}
    
    public void EditRecord(String recordID, String fieldName, String newValue) throws NotBoundException, IOException {
		intrfc = (DcmsInterface)registry.lookup(managerID.substring(0,3));
		String reply = intrfc.editRecord(recordID, fieldName, newValue);
		System.out.println(reply);
		writeToLog(reply);
	}

}
