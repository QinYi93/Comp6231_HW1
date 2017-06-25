package comp6231;

/**
 * This class define all parameter, configuration file.
 * @author Yi Qin
 * @date ${date}
 */
public interface PublicParameters {

	/**
	 * This class location contains port number, 1 location only can have 1 server port
	 * @author Yi Qin
	 * @date Jun 6, 2017
	 */
	
    enum Location{
        MTL(SERVER_PORT_MTL),
        LVL(SERVER_PORT_LVL),
        DDO(SERVER_PORT_DDO);

        private int port;

        Location(int port) {
            this.port = port;
        }

        public int getPort() {
            return port;
        }
    };

    enum Specialization {FRENCH, MATHS, SCIENCE};
    enum CoursesRegistered{MATHS, FRENCH, SCIENCE};
    enum Status {ACTIVE, INACTIVE};
    
    //server port cannot be change at run time
    final int SERVER_PORT_MTL = 7000;
    final int SERVER_PORT_LVL = 7001;
    final int SERVER_PORT_DDO = 7002;

}
