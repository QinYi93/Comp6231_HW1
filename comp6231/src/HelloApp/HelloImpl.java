package HelloApp;

import org.omg.CORBA.ORB;

class HelloImpl extends HelloPOA {
	private ORB orb;
	/**
	 * @param orb the orb to set
	 */
	public void setOrb(ORB orb) {
		this.orb = orb;
	}

	/* (non-Javadoc)
	 * @see HelloApp.HelloOperations#sayHello()
	 */
	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		return "\nHello world!!\n";
	}

	/* (non-Javadoc)
	 * @see HelloApp.HelloOperations#shutdown()
	 */
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		orb.shutdown(false);
	}
}
