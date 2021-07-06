/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */

/**
 * The carousel holds vials of vaccine and rotates them,
 * position 0 is designed for receiving the inspection vial
 * position 1 is designed for collect the tagged vial from this carousel
 */
public class InspectionCarousel
{
    // the items in the inspection carousel segments
    protected Vial[] compartment;
    // to help format output trace
    final private static String indentation = "                  ";

    /**
     * Create a new, empty carousel, initialised to be empty.
     */
    public InspectionCarousel()
    {
        compartment = new Vial[Params.INSPECTION_CAROUSEL_SIZE];
        for (int i = 0; i < compartment.length; i++)
        {
            compartment[i] = null;
        }
    }

    /**
     * Insert a vial into the carousel position [0].
     * while this position is not empty, wait for this space to release
     * Then, put the vial in position [0]
     * print the event
     * notify others that the carousel situation is changed
     * @param vial
     *            the vial to insert into the carousel.
     * @throws InterruptedException
     *            if the thread executing is interrupted.
     */
    public synchronized void putVial(Vial vial)
            throws InterruptedException
    {

    	// while there is another vial in the position[0], block this thread
        while (compartment[0] != null)
        {
            wait();
        }

        // insert the element at the specified location
        compartment[0] = vial;

        // make a note of the event in output trace
        System.out.println(indentation + vial + " [  S"  + " -> I1 ]");

        // notify any waiting threads that the carousel state has changed
        notifyAll();
    }



    /**
     * Remove a vial from the final compartment of the carousel
     * when position [1] has no vial, wait for the vial
     * then get the vial can print the result
     * @return the removed vial
     * @throws InterruptedException
     *             if the thread executing is interrupted
     */
    public synchronized Vial getVial() throws InterruptedException
    {
    	// the vial to be removed
        Vial vial;
        // while there is no vial in the final compartment, block this thread
        while (compartment[compartment.length - 1] == null)
        {
            wait();
        }
        // get the vial
        vial = compartment[compartment.length-1];
        compartment[compartment.length - 1] = null;

        // make a note of the event in output trace
        System.out.print(indentation + indentation);
        System.out.println(vial + " collected");

        // notify any waiting threads that the carousel has changed
        notifyAll();
        return vial;
    }


    /**
     * Rotate the carousel one position.
     * when the position [0] vial is not inspection, wait for the inspection period finish
     * "I0" represents the inspection carousel position 0 to put and recive inspection vial
     * "I1" represents the inspection carousel position 1 for quality manager to collect vial
     * @throws OverloadException
     *             if a vial is rotated beyond the final compartment.
     * @throws InterruptedException
     *             if the thread executing is interrupted.
     */
    public synchronized void rotate() 
            throws InterruptedException, OverloadException
    {
        // if there is in the final compartment, or the carousel is empty,
        // or a vial needs to be removed for inspection, do not move the carousel
        while (isEmpty() || 
        		compartment[compartment.length-1] != null)
        {
            wait();
        }
        // double check that a vial cannot be rotated beyond the final compartment
        if (compartment[compartment.length-1] != null)
        {
            String message = "vial rotated beyond final compartment";
            throw new OverloadException(message);
        }
        // move the elements along, making position 0 null

        //when the inspection is not finished yet//---> THIS IS NEW ADDED
        while(!compartment[0].isTagged())
        {
            wait();
        }

        //print the event, "I0" represents the inspection carousel position 0 to put and recive inspection vial,
        // "I1" represents the inspection carousel position 1 for quality manager to collect vial
        for (int i = compartment.length-1; i > 0; i--)
        {
            if (this.compartment[i-1] != null)
            {
                System.out.println(
                		indentation +
                		this.compartment[i-1] +
                        " [ I" + (i) + " -> I" + (i+1) +" ]");
            }
            //if it reaches C3 (i+1=3) then use a method to scan ther vial
            compartment[i] = compartment[i-1];
        }
        compartment[0] = null;
        // notify any waiting threads that the carousel has changed
        notifyAll();
    }

    /**
     * Check whether the carousel is currently empty.
     * @return true if the carousel is currently empty, otherwise false
     */
    private boolean isEmpty() {
        for (int i = 0; i < compartment.length; i++)
        {
            if (compartment[i] != null)
            {
                return false;
            }
        }
        return true;
    }
    
    public String toString()
    {
        return java.util.Arrays.toString(compartment);
    }

    /**Situation 1: inspection bay has no vial and wait for the vial insertion
     * Situation 2: Inspection bay has vial && not inspected
     * Function: inspect the vial at this position and give tag
     * Process:
     *      1.set tags "i" "t"
     *      2.mimic the time period
     *      3.after this done,notify other threads
     * @param
     * @throws InterruptedException
     */
    public synchronized void inspection() throws InterruptedException
    {
        while(compartment[0] == null) // inspection point is empty
        {
            wait();// situation 1: inspection bay has empty vial, wait until vial is provided
        }
        if(!compartment[0].isTagged())
        {
            // set tags ("i") && ("t")
            compartment[0].setInspected();
            compartment[0].setTagged();
        }
        // notify any waiting threads that the vial has changed
        notifyAll();
    }

}
