package assisgnment1;

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





        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
