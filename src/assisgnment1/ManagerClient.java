package assisgnment1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
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

import assisgnment1.PublicParameters.Location;

/**
 * This class
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class ManagerClient {

    protected static int managerBaseID = 1000;
    private String managerID;
    private File log;

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
        managerBaseID++;
    }

    public String getManagerID() {
        return managerID;
    }

    public void writeToLog (String str) throws IOException{
        FileWriter writer = new FileWriter(log , true);
        writer.write(str + "\n");
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {
        System.setSecurityManager(new RMISecurityManager());	
    
        int userChoice = 0;
        String userInput = "";
        Scanner scanner = new Scanner(System.in);

        HashMap<String, ManagerClient> managerList = new HashMap<String, ManagerClient>();

        showMenuLevel1();

        while (true){
            Boolean valid = false;
            //make sure user input an integer
            while (!valid){
                try {
                    userChoice = Integer.parseInt(scanner.nextLine());
                    valid = true;
                }catch (Exception e){
                    System.out.println("Invalid input, please enter an Integer");
                    valid = false;
                }
            }

            //handle manager selection
            switch (userChoice){
                case 1:
                    System.out.println("Please chose your location from: MTL, LVL, DDO");
                    userInput = scanner.nextLine().toUpperCase().trim();
                    if (userInput.equals("MTL") || userInput.equals("LVL") || userInput.equals("DDO")){
                        PublicParameters.Location location = PublicParameters.Location.valueOf(userInput);
                        ManagerClient client = new ManagerClient(location);
                        managerList.put(client.getManagerID(), client);
                        try {
                            client.writeToLog("Create new manager account. \n Your manager id is" + client.getManagerID());
                            System.out.println("Your account is created. Your manager id is" + client.getManagerID());
                            System.out.println("back to main menu");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("Invalid input, please enter MTL, LVL or DDO");
                        System.out.println("back to main menu");
                    }
                    showMenuLevel1();
                    break;
                case 2:
                    System.out.println("Please input your manager id");
                    String managerId = scanner.nextLine().toUpperCase().trim();
                    if(managerList.containsKey(managerId)){
                        ManagerClient client = managerList.get(managerId);
                        System.out.println("*** Welcome to class system, your managerID is\t" + managerId+"\t***");
                        try {
                            client.writeToLog(managerId + "login in sucessfully");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PublicParameters.Location location = PublicParameters.Location.valueOf(managerId.substring(0, 3));
                        try {
                            Registry registry = LocateRegistry.getRegistry(getPort(location));
                            DcmsInterface server = (DcmsInterface) registry.lookup(location.toString());
                            showMenuLeve2(scanner, server, client);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("Sorry, we could't find your account, please check again, back to main menu");
                        showMenuLevel1();
                    }
                    break;
                case 3:
                    System.out.println("Thank you, you will exit the system");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid input, please try again.");
            }

        }

    }

    public static void showMenuLevel1(){
        System.out.println("-------Welcome to Class Management System-------");
        System.out.println("Please select an option number");
        System.out.println("1. Register as new manager");
        System.out.println("2. Login as exist manager");
        System.out.println("3. Exit");
    }

    private static void showMenuLeve2(Scanner scanner, DcmsInterface server, ManagerClient client)
            throws ParseException, IOException{
        int userChoice;
        while (true){
            System.out.println("------menuLevel2--------");
            System.out.println("1.Create Teacher Record");
            System.out.println("2.Create Student Record");
            System.out.println("3.Get records counts");
            System.out.println("4.Edit record");
            System.out.println("5.Exit");
            userChoice = Integer.parseInt(scanner.nextLine());
            //Manager user selection
            switch (userChoice){
                case 1:
                    System.out.println("Create a new teacher record");
                    System.out.println("Please enter the firstName, lastName, address, phone, specialization, location");
                    System.out.println("Location must be MTL, LVL, DDO");
                    System.out.println("specialization must be FRENCH, MATHS, SCIENCE");
                    String firstName = scanner.nextLine().trim();
                    String lastName = scanner.nextLine().trim();
                    String address = scanner.nextLine().trim();
                    String phone = scanner.nextLine().replace("\\D+", "");
                    PublicParameters.Specialization specialization = PublicParameters.Specialization.valueOf(scanner.nextLine());
                    PublicParameters.Location location = PublicParameters.Location.valueOf(scanner.nextLine().substring(0,3));
                    client.writeToLog("Manager create new Teacher Record" + firstName + " " + lastName + " " + address + " "
                            +phone + " " + specialization + " " + location);
                    String message1 = server.createTRecord(firstName, lastName, address, phone, specialization, location);
                    client.writeToLog(message1);
                    System.out.println(message1);
                    break;
                case 2:
                    System.out.println("Create new student record");
                    System.out.println("Please enter firstName, lastName, courseRegistered, status, statusDates");
                    System.out.println("CourseRegistered must be MATHS, FRENCH, SCIENCE");
                    System.out.println("Status must be ACTIVE, INACTIVE");
                    System.out.println("Please input Date as format yyyy-mm-dd");
                    String SfirstName = scanner.nextLine().trim();
                    String SlastName = scanner.nextLine().trim();
                    PublicParameters.CoursesRegistered coursesRegistered = PublicParameters.CoursesRegistered.valueOf(scanner.nextLine());
                    PublicParameters.Status status = PublicParameters.Status.valueOf(scanner.nextLine());
                    Date statusDates = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine().toString());

                    client.writeToLog("Manager create new Student record" + SfirstName + " " + SlastName + " " + coursesRegistered +
                    " " + status + " " + statusDates);
                    String message2 = server.createSRecord(SfirstName, SlastName, coursesRegistered, status, statusDates);
                    client.writeToLog(message2);
                    System.out.println(message2);
                    break;
                case 3:
                    String message3 = server.getRecordCounts();
                    client.writeToLog("get Record counts is" + message3);
                    System.out.println(message3);
                    break;
                case 4:
                	System.out.println("Please input recordID, fieldName, newValue");
                	String recordID = scanner.nextLine().trim();
                	String fieldName = scanner.nextLine().trim();
                	String newValue = scanner.nextLine().trim();
                	String message4 = "Manager want to edit the record \t" + recordID 
                			+ "\t fieldName is \t" + fieldName + "\t to new value \t" + newValue;
                	client.writeToLog(message4);
                	System.out.println(message4);
                	String message5 = server.editRecord(recordID, fieldName, newValue);
                	client.writeToLog(message5);
                	System.out.println(message5);
                case 5:
                    System.out.println("Thank for login");
                    scanner.close();
                    client.writeToLog("Manager exit");
                    System.exit(0);
                default:
                    System.out.println("Invalid input, please try again");
            }

        }

    }

    public static int getPort(PublicParameters.Location location){
        if (location == Location.MTL)
            return 7000;
        if (location == Location.LVL)
            return 7001;
        if (location == Location.DDO)
            return 7002;
        return -1;
    }
}
