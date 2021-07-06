/** 
 * An inspector holds and inspects vials of vaccine for defects.
 */
public class Inspector {
	
	// the vial being inspected
	protected Vial vial;
	
	// true if inspection underway
	protected boolean inspecting;
	
	/**
	 * Create a new inspector
	 */
	public Inspector(){
		super();
		vial = null;
		inspecting = false;
	}
	
	/**
	 * Inspect a vial.
	 */
	public synchronized void inspect() throws InterruptedException {
		
		// wait for a vial to arrive
		while (vial == null) {
			wait();
		}
		
		// inspect vial
		inspecting = true;
		Thread.sleep(Params.INSPECT_TIME);
		
		if (vial.isDefective()) {
			vial.setTagged();
		}
		
		inspecting = false;
		vial.setInspected();
		
		// notify all processes we are done inspecting
		notifyAll();
	}
	
	/** 
	 * Give a vial to the inspector
	 */
	public synchronized void putVial(Vial vial) throws OverloadException {
		if (this.vial != null) {
			throw new OverloadException("Vial already with inspector");
		}
		this.vial = vial;
		notifyAll();
	}
	
	/**
	 * Get a vial from the inspector.
	 * @return the vial
	 */
	public synchronized Vial getVial() throws InterruptedException{
		
		// wait until there is a vial that has been inspected
		while (vial != null && inspecting) {
			wait();
		}
		
		Vial result = vial;
		vial = null;
		notifyAll();
		return result;
	}
	
	/**
	 * @return true if inspector currently holds a vial.
	 */
	public boolean hasVial() {
		return vial != null;
	}
	
	public String toString() {
		return vial == null ? "null" : vial.toString();
	}
}
