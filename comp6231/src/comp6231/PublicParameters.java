package comp6231;

/**
 * This class
 *
 * @author Yi Qin
 * @date ${date}
 */
public interface PublicParameters {

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

    final int SERVER_PORT_MTL = 7000;
    final int SERVER_PORT_LVL = 7001;
    final int SERVER_PORT_DDO = 7002;

}
