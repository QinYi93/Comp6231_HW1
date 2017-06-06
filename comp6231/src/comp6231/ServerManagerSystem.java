package comp6231;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class 
 *
 * @author Yi Qin
 * @date 2017-05-28
 */
public class ServerManagerSystem {
    public static ArrayList<ClassServer> serverArrayList= new ArrayList<ClassServer>();

    public ServerManagerSystem() {
    }

    public static void main(String[] args) {
        try {
            ClassServer mtl = new ClassServer(PublicParameters.Location.MTL);
            ClassServer lvl = new ClassServer(PublicParameters.Location.LVL);
            ClassServer ddo = new ClassServer(PublicParameters.Location.DDO);

            serverArrayList.add(mtl);
            serverArrayList.add(lvl);
            serverArrayList.add(ddo);

            System.out.println("Servers are up and running");
            
            // create registry, RMI binding
            try {
				mtl.exportServer();
				lvl.exportServer();
		        ddo.exportServer();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //UDP waiting request thread
			new Thread() {
				@Override
				public void run() {
				mtl.openUDPListener();
				}
			}.start();
			
			new Thread() {
				@Override
				public void run() {
				lvl.openUDPListener();
				}
			}.start();
			
			new Thread() {
				@Override
				public void run() {
				ddo.openUDPListener();
				}
			}.start();
         

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
