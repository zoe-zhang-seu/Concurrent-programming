/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
/**
 * Aim: An inspection bay to test the quality of vial
 * the defective vial will be given tags
 * ("i") represents inspected and ("t") represents tagged
 */

public class Inspection extends VaccineHandlingThread
{
    final private static String indentation = "                  ";

    protected volatile Vial ins_vial;// vial at inspection position

    /**Constructor
     * Create a new Inspection bay which has empty vial
     */
    public Inspection()
    {
        super();
        this.ins_vial = null;
    }

    /**
     * Loop indefinitely trying to inspected the provided vial
     */
    public void run()
    {
        while(!isInterrupted())
        {
            try
            {
                inspectionProcess();

                // inspection time is INSPECT_TIME, sleep this thread to mimic the time period
                int inspectionTime = Params.INSPECT_TIME;
                sleep(inspectionTime);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Inspection terminated");
    }

    /**situation 1: inspection bay has empty vial, wait until vial is provided
     * Situation 2: Inspection bay has vial && not inspected
     * Function: inspect the vial at this position and give tag
     * Processs:
     *      1.set tags "i" "t"
     *      2.mimic the time period
     *      3.after this done,notify other threads
     * @throws InterruptedException
     */
    public synchronized void inspectionProcess() throws InterruptedException
    {
            while(ins_vial == null) // inspection point is empty
            {
                wait();// situation 1: inspection bay has empty vial, wait until vial is provided
            }

            if(!ins_vial.isTagged())//Situation 2: Inspection bay has vial && not inspected
            {
                // set tags ("i") && ("t")
                ins_vial.setInspected();
                ins_vial.setTagged();

            }
            //notify the backward transmission
            notifyAll();
    }

    /**
     * Put vial at the inspection bay position
     * change the hasVial tag as true
     * set this vial
     * @param vial the vial required to put
     */
    public synchronized void putInsPositionVial(Vial vial) throws InterruptedException
    {
        while(ins_vial != null)// while inspection space is used, wait for it become available
        {
            wait();
        }
        this.ins_vial = vial;

        System.out.println(indentation + ins_vial + " [  S"  + " -> I  ]");
        notifyAll();
    }

    /**
     * Function: take the vial from inspection bay
     * process:
     *          1. while inspection has no vial, wait
     *          2. take the vial from inspection, clean the inspection site data
     *          3. return the shuttled vial
     * Situation:  used by the backward transmission will take inspected vial from inspection bay
     * @return vial, the vial which finished inpsection
     */
    public synchronized Vial getInsPositionVial() throws InterruptedException
    {
        while(ins_vial == null)//when this position has no available vial, wait for it come
        {
            wait();
        }
        Vial vial = ins_vial;
        System.out.println(indentation + ins_vial + " [  I"  + " -> S  ]");
        reinitializeInspection();// clean the data, represents taking vial from inspection bay
        notifyAll();
        return vial;
    }

    /**
     * Situation:Used after the backward transmission take inspected vial from inspection bay
     * Function: clean the inspection bay data, prepare for the next new inspection
     */
    public synchronized void reinitializeInspection()
    {
        this.ins_vial = null;
    }

}
