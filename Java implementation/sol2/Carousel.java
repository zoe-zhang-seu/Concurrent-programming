/**
 * The carousel holds vials of vaccine and rotates them from the compartment
 * at position 0, through to the scanner compartment, where they are 
 * scanned and potentially removed by a shuttle for further inspection, 
 * through to the final compartment where they are taken off the carousel.
 */
public class Carousel {

    // the items in the carousel segments
    protected Vial[] compartment;

    // the length of this carousel
    protected int compartmentCount;

    // the carousel's scanner
    protected Scanner scanner;
    
    // the carousel type (for output)
    protected String type;
    
    // the location of the scanner
    protected int scannerPos = 2;

    // to help format output trace
    final private static String indentation = "                  ";

    /**
     * Create a new, empty carousel, initialised to be empty.
     */
    public Carousel(Scanner scanner, int size, String type) {
    	this.compartmentCount = size;
        compartment = new Vial[compartmentCount];
        for (int i = 0; i < compartment.length; i++) {
            compartment[i] = null;
        }
        this.scanner = scanner;
        this.type = type;
    }

    /**
     * Insert a vial into the carousel.
     * 
     * @param vial
     *            the vial to insert into the carousel.
     * @param index
     *            the place to put the vial
     * @throws InterruptedException
     *            if the thread executing is interrupted.
     */
    public synchronized void putVial(Vial vial, int index)
            throws InterruptedException {

    	// while there is another vial in the way, block this thread
        while (compartment[index] != null) {
            wait();
        }

        // insert the element at the specified location
        compartment[index] = vial;

        // make a note of the event in output trace
        System.out.println(this.type + " " + vial + " inserted");

        // notify any waiting threads that the carousel has changed
        notifyAll();
    }

    /**
     * Take a vial from the scanner position of the carousel
     * 
     * @return the vial in the inspection position
     * @throws InterruptedException
     * 				if the thread executing is interrupted.
     */
    public synchronized Vial getVial() throws InterruptedException {
    	
    	// the vial to be returned
    	Vial vial;
    	
    	while (compartment[scannerPos] == null) {
    		wait();
    	}
    	
    	// grab the tagged vial
    	vial = compartment[scannerPos];
    	compartment[scannerPos] = null;
    	  	
    	System.out.print(indentation);
    	System.out.println(this.type + " " + vial + " [ c" + (scannerPos + 1) + " -> S  ]");
    	
    	// notify any waiting threads that the carousel has changed
    	notifyAll();
    	return vial;
    }
        
    /**
     * Remove a vial from the final compartment of the carousel
     * 
     * @return the removed vial
     * @throws InterruptedException
     *             if the thread executing is interrupted
     */
    public synchronized Vial getFinalCompartment() throws InterruptedException {

    	// the vial to be removed
        Vial vial;

        // while there is no vial at the end of the carousel, block this thread
        while (compartment[compartment.length - 1] == null) {
            wait();
        }

        // get the next item
        vial = compartment[compartment.length-1];
        compartment[compartment.length - 1] = null;

        // make a note of the event in output trace
        System.out.print(indentation + indentation);
        System.out.println(this.type + " " + vial + " removed");

        // notify any waiting threads that the carousel has changed
        notifyAll();
        return vial;
    }

    /** 
     * Return a vial to the scanner position of the carousel
     * 
     * @throws InterruptedException
     * 				if the thread executing is interrupted.
     */
    public synchronized void handBack(Vial vial) throws InterruptedException {
    	while (compartment[scannerPos] != null) {
    		wait();
    	}
    	
    	// place the vial back on the carousel
    	compartment[scannerPos] = vial;
    	
    	System.out.print(indentation);
    	System.out.println(this.type + " " + vial + " [  S -> c" + (scannerPos + 1) + " ]");
    	
    	// notify any waiting threads that the carousel has changed
    	notifyAll();
    }
    
    /**
     * Move the carousel along one segment
     * 
     * @throws OverloadException
     *             if a vial is rotated beyond the final compartment.
     * @throws InterruptedException
     *             if the thread executing is interrupted.
     */
    public synchronized void rotate() 
            throws InterruptedException, OverloadException {
        // if there is in the final compartment, or the carousel is empty,
        // or a vial needs to be removed for inspection, do not move the carousel
        while (isEmpty() || 
        		compartment[compartment.length-1] != null ||
        		scanner != null && scanner.alarm()) {
            wait();
        }

        // double check that a vial cannot be rotated beyond the final compartment
        if (compartment[compartment.length-1] != null) {
            String message = "vial rotated beyond final compartment";
            throw new OverloadException(message);
        }

        // move the elements along, making position 0 null
        for (int i = compartment.length-1; i > 0; i--) {
            if (this.compartment[i-1] != null) {
                System.out.println(
                		indentation + this.type + " " +
                		this.compartment[i-1] +
                        " [ c" + (i) + " -> c" + (i+1) +" ]");
            }
            compartment[i] = compartment[i-1];
        }
        compartment[0] = null;

        if (scanner != null && compartment[scannerPos] != null) {
        	scanner.scan(compartment[scannerPos]);
        }
        
        // notify any waiting threads that the carousel has changed
        notifyAll();
    }
 
    /**
     * Check whether the carousel is currently empty.
     * @return true if the carousel is currently empty, otherwise false
     */
    private boolean isEmpty() {
        for (int i = 0; i < compartment.length; i++) {
            if (compartment[i] != null) {
                return false;
            }
        }
        return true;
    }
    
    public String toString() {
        return java.util.Arrays.toString(compartment);
    }

}
