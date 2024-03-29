package comp6231;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.text.ParseException;

import comp6231.PublicParameters.CoursesRegistered;
import comp6231.PublicParameters.Location;
import comp6231.PublicParameters.Specialization;
import comp6231.PublicParameters.Status;

public class ClientRunner {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ManagerClient mtlManager1 = new ManagerClient(Location.MTL);
		ManagerClient mtlManager2 = new ManagerClient(Location.MTL);
		ManagerClient mtlManager3 = new ManagerClient(Location.MTL);
		ManagerClient lvlManager1 = new ManagerClient(Location.LVL);
		ManagerClient lvlManager2 = new ManagerClient(Location.LVL);
		ManagerClient ddoManager1 = new ManagerClient(Location.DDO);
		ManagerClient ddoManager2 = new ManagerClient(Location.DDO);
		
		// test functionality
				mtlManager1.createSRecord("Student1", "Student1", CoursesRegistered.FRENCH, Status.ACTIVE, "2017-05-1");
				mtlManager1.createTRecord("Teacher1", "Teacher1", "ABC street", "123-456-7890", Specialization.FRENCH , Location.MTL);
				mtlManager2.createSRecord("Student2", "Student2", CoursesRegistered.MATHS, Status.INACTIVE, "2017-05-1");
				mtlManager2.createTRecord("Teacher2", "Teacher2", "ABC street", "123-456-7890", Specialization.MATHS , Location.MTL);
				
				lvlManager1.createSRecord("Student3", "Student3", CoursesRegistered.FRENCH, Status.ACTIVE, "2017-05-1");
				lvlManager1.createTRecord("Teacher3", "Teacher3", "ABC street", "123-456-7890", Specialization.SCIENCE , Location.LVL);
				lvlManager2.createSRecord("Student4", "Student2", CoursesRegistered.SCIENCE, Status.ACTIVE, "2017-05-1");
				lvlManager2.createTRecord("Teacher4", "Teacher2", "ABC street", "123-456-7890", Specialization.FRENCH , Location.LVL);
				
				ddoManager1.createSRecord("Student5", "Student5", CoursesRegistered.FRENCH, Status.ACTIVE, "2017-05-1");
				ddoManager1.createTRecord("Teacher5", "Teacher5", "ABC street", "123-456-7890", Specialization.FRENCH , Location.DDO);
			
				mtlManager1.getRecordCounts();
				lvlManager2.getRecordCounts();
				ddoManager2.getRecordCounts();

				ddoManager1.EditRecord("SR10008", "status date", "2010-01-01");
				ddoManager1.EditRecord("SR10008", "status", "inactive");
				ddoManager1.EditRecord("SR10008", "course", "maths");
				ddoManager1.EditRecord("SR10008", "course", "french");
				
				lvlManager1.EditRecord("TR10005", "phone", "098-765-4321");
				lvlManager1.EditRecord("TR10005", "address", "concordia street");
				lvlManager1.EditRecord("TR10005", "location", "ddo");
				
				// test concurrency
				mtlManager1.getRecordCounts();
				mtlManager1.EditRecord("SR10000", "status date", "2010-01-01");
				mtlManager3.createSRecord("Student6", "Student6", CoursesRegistered.MATHS, Status.ACTIVE, "2017-05-1");
				mtlManager2.EditRecord("SR10000", "course", "MATHS");
				mtlManager3.EditRecord("SR10000", "status", "INACTIVE");

				
				lvlManager2.getRecordCounts();
				lvlManager1.EditRecord("SR10004", "status date", "2010-01-01");
				lvlManager2.EditRecord("TR10007", "address", "SCIENCE street");
				
				ddoManager1.EditRecord("SR10008", "status date", "2010-01-01");
				ddoManager2.createSRecord("Student3", "Student3", CoursesRegistered.FRENCH, Status.ACTIVE, "2017-05-1");
				ddoManager2.getRecordCounts();
	}
	
	//
//  public static void main(String[] args) {
//      System.setSecurityManager(new RMISecurityManager());	
//  
//      int userChoice = 0;
//      String userInput = "";
//      Scanner scanner = new Scanner(System.in);
//
//      HashMap<String, ManagerClient> managerList = new HashMap<String, ManagerClient>();
//
//      showMenuLevel1();
//
//      while (true){
//          Boolean valid = false;
//          //make sure user input an integer
//          while (!valid){
//              try {
//                  userChoice = Integer.parseInt(scanner.nextLine());
//                  valid = true;
//              }catch (Exception e){
//                  System.out.println("Invalid input, please enter an Integer");
//                  valid = false;
//              }
//          }
//
//          //handle manager selection
//        
//          switch (userChoice){
//              case 1:
//                  System.out.println("Please chose your location from: MTL, LVL, DDO");
//                  userInput = scanner.nextLine().toUpperCase().trim();
//                  if (userInput.equals("MTL") || userInput.equals("LVL") || userInput.equals("DDO")){
//                      PublicParameters.Location location = PublicParameters.Location.valueOf(userInput);
//                      ManagerClient client = new ManagerClient(location);
//                      managerList.put(client.getManagerID(), client);
//                      try {
//                          client.writeToLog("Create new manager account. \n Your manager id is \t" + client.getManagerID());
//                          System.out.println("Your account is created. Your manager id is \t" + client.getManagerID());
//                          System.out.println("back to main menu");
//                      } catch (IOException e) {
//                          e.printStackTrace();
//                      }
//                  }else {
//                      System.out.println("Invalid input, please enter MTL, LVL or DDO");
//                      System.out.println("back to main menu");
//                  }
//                  showMenuLevel1();
//                  break;
//              case 2:
//                  System.out.println("Please input your manager id");
//                  String managerId = scanner.nextLine().toUpperCase().trim();
//                  if(managerList.containsKey(managerId)){
//                      ManagerClient client = managerList.get(managerId);
//                      System.out.println("*** Welcome to class system, your managerID is\t" + managerId+"\t***");
//                      try {
//                          client.writeToLog(managerId + "login in sucessfully");
//                      } catch (IOException e) {
//                          e.printStackTrace();
//                      }
//                      PublicParameters.Location location = PublicParameters.Location.valueOf(managerId.substring(0, 3));
//                      try {
//                          Registry registry = LocateRegistry.getRegistry(getPort(location));
//                          DcmsInterface server = (DcmsInterface) registry.lookup(location.toString());
//                          showMenuLeve2(scanner, server, client);
//                      } catch (Exception e) {
//                          e.printStackTrace();
//                      }
//                  }else {
//                      System.out.println("Sorry, we could't find your account, please check again, back to main menu");
//                      showMenuLevel1();
//                  }
//                  break;
//              case 3:
//                  System.out.println("Thank you, you will exit the system");
//                  scanner.close();
//                  System.exit(0);
//              default:
//                  System.out.println("Invalid input, please try again.");
//          }
//        }
//      }
//
//  public static void showMenuLevel1(){
//      System.out.println("-------Welcome to Class Management System-------");
//      System.out.println("Please select an option number");
//      System.out.println("1. Register as new manager");
//      System.out.println("2. Login as exist manager");
//      System.out.println("3. Exit");
//  }
//
//  private static void showMenuLeve2(Scanner scanner, DcmsInterface server, ManagerClient client)
//          throws ParseException, IOException{
//      int userChoice;
//      while (true){
//          System.out.println("------menuLevel2--------");
//          System.out.println("1.Create Teacher Record");
//          System.out.println("2.Create Student Record");
//          System.out.println("3.Get records counts");
//          System.out.println("4.Edit record");
//          System.out.println("5.Exit");
//          userChoice = Integer.parseInt(scanner.nextLine());
//          //Manager user selection
//          switch (userChoice){
//              case 1:
//                  System.out.println("Create a new teacher record");
//                  System.out.println("Please enter the firstName, lastName, address, phone, specialization, location");
//                  System.out.println("Location must be MTL, LVL, DDO");
//                  System.out.println("specialization must be FRENCH, MATHS, SCIENCE");
//                  String firstName = scanner.nextLine().trim();
//                  String lastName = scanner.nextLine().trim();
//                  String address = scanner.nextLine().trim();
//                  String phone = scanner.nextLine().replace("\\D+", "");
//                  PublicParameters.Specialization specialization = PublicParameters.Specialization.valueOf(scanner.nextLine().toUpperCase());
//                  PublicParameters.Location location = PublicParameters.Location.valueOf(scanner.nextLine().toUpperCase().substring(0, 3));
//                  client.writeToLog("Manager create new Teacher Record \t" + firstName + " " + lastName + " " + address + " "
//                          +phone + " " + specialization + " " + location);
//                  String message1 = server.createTRecord(firstName, lastName, address, phone, specialization, location);
//                  client.writeToLog(message1);
//                  System.out.println(message1);
//                  break;
//              case 2:
//                  System.out.println("Create new student record");
//                  System.out.println("Please enter firstName, lastName, courseRegistered, status, statusDates");
//                  System.out.println("CourseRegistered must be MATHS, FRENCH, SCIENCE");
//                  System.out.println("Status must be ACTIVE, INACTIVE");
//                  System.out.println("Please input Date as format yyyy-mm-dd");
//                  String SfirstName = scanner.nextLine().trim();
//                  String SlastName = scanner.nextLine().trim();
//                  PublicParameters.CoursesRegistered coursesRegistered = PublicParameters.CoursesRegistered.valueOf(scanner.nextLine().toUpperCase());
//                  PublicParameters.Status status = PublicParameters.Status.valueOf(scanner.nextLine().toUpperCase());
//                  Date statusDates = new SimpleDateFormat("yyyy-MM-dd").parse(scanner.nextLine().toString());
//
//                  client.writeToLog("Manager create new Student record \t" + SfirstName + " " + SlastName + " " + coursesRegistered +
//                  " " + status + " " + statusDates);
//                  String message2 = server.createSRecord(SfirstName, SlastName, coursesRegistered, status, statusDates);
//                  client.writeToLog(message2);
//                  System.out.println(message2);
//                  break;
//              case 3:
//                  String message3 = server.getRecordCounts();
//                  client.writeToLog("get Record counts is \t" + message3);
//                  System.out.println(message3);
//                  break;
//              case 4:
//              	    System.out.println("please input recordID, fieldName(TR is restricted to address, phone, location)(SR is restricted to courseregistered, status, status date), newValue to edit");
//              	    String recordID = scanner.nextLine().toLowerCase().trim();
//              	    String fieldName = scanner.nextLine().toLowerCase().trim();
//              	    String newValue = scanner.nextLine().toLowerCase().trim();
//              	    String message4 = "Edit the RecordID is\t" + recordID + "\t fieldName \t" 
//              	    + fieldName + "\t to the new value \t" + newValue;
//              	    client.writeToLog(message4);
//              	    System.out.println(message4);
//              	    String message5 = server.editRecord(recordID, fieldName, newValue);
//              	    client.writeToLog(message5);
//              	    System.out.println(message5);
//              case 5:
//                  System.out.println("Thank for login");
//                  scanner.close();
//                  client.writeToLog("Manager exit");
//                  System.exit(0);
//              default:
//                  System.out.println("Invalid input, please try again");
//          }
//
//      }
//
//  }
//
//  public static int getPort(PublicParameters.Location location){
//      if (location == Location.MTL)
//          return 7000;
//      if (location == Location.LVL)
//          return 7001;
//      if (location == Location.DDO)
//          return 7002;
//      return -1;
//  }
	

}
