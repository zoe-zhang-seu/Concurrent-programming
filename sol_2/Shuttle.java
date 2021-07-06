/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
/**
 * A single bi-direction shuttle continually, at intervals, tries to transfer vials
 * forward transmission direction: Carousel C3 ---> Inspection acception bay.
 * backward transmission direction:  inspection bay ---> Carousel C3
 */

public class Shuttle extends VaccineHandlingThread
{
    protected InspectionCarousel insCarousel;
    protected Carousel carousel;

    protected volatile Vial shuttleVial;//the vial at shuttle
    final private static String indentation = "                  ";

    /**
     * Create a new forward shuttle that produce vial for inspection
         * Direction: carousel [C3] -> inspection [x]
         * role : act as a producer for inspection
     */
    public Shuttle(Carousel carousel, InspectionCarousel insCarousel)
    {
        super();
        this.carousel = carousel;
        this.insCarousel = insCarousel;

        //initialize the backward shuttle is empty
        this.shuttleVial = null;
    }

    /**
     * Loop indefinitely trying to get vials from the carousel
     * situation 1: C3 is empty && inpection is empty --> wait
     * situation 2: C3 has vial, inspection is empty
     *      2-1 : vial does not finish scanning --> wait
     *      2-2 :vial finished scanned and not need transfer
     *      2-3 : vial is defective and not inspected yet  ---> transmission process
     *      2-4 : vial is inspected, do nothing
     * situation 3: C3 and inspection has vial
     * situation 4: C3 empty and inspection has vial
     *      4-1: vial is not inspected
     *      4-2: vial is inspected but C3 is not empty
     *      4-3: vial is inspected and transferring
     */
    public void run()
    {
        while (!isInterrupted())
        {
            try
            {
                    //situation 1:
                    //check if the C3, shuttle and Inspection are empty, wait until vial enter
                    isWholeEmpty();

                    //Situation 2: C3 has vial, inspection is empty
                    ForwardTransferProcess();

                    //i delete the backward transfer
                    //because if and only if the vial in inspection carousel[0] vial is normal, inspected,not tagged "(-i-)"
                    //then the vial will be sent in the backward transmission, during the task description, there is no non-defective
                    //vial will be sent to the inspection bay

                    //let some time go
                    int onceShuttleTime = Params.SHUTTLE_TIME;
                    sleep(onceShuttleTime);

                    //Situation 2-4 : vial is inspected, do nothing

            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Forward Transmission terminated");
    }

    /**
     * when there is no vial at the system (C3, shuttle, inspection)
     * wait unitl the vial is provided
     * @throws InterruptedException
     */
    public synchronized void isWholeEmpty() throws InterruptedException
    {
        while((insCarousel.compartment[0] ==null) && (carousel.compartment[2]==null) && (shuttleVial ==null))
        {
            wait();//both side is empty wait the C3 has been provided vial
        }
    }


    /**
     * Aim: Transfer vial from C3 to Inspection Carousel position [0]
     * process:
     *      1. while inspection has vial, shuttle is used wait shuttle is released
     *      2. take C3 vial from carousel -> put on the shuttle
     *      2. transfer
     *      4. take vial from the shuttle -> put on the  Inspection Carousel position [0]
     *  Situation 1 : vial does not finish scanning
     *  Situation 2 :vial finished scanned and not need transfer
     *      normal->stay at C3, not shuttle
     *  Situation 3 : vial is defective and not inspected yet
     *      transfer
     * @throws InterruptedException
     */
    public synchronized void ForwardTransferProcess() throws InterruptedException
    {
        while(carousel.compartment[2] == null)
        {
            wait();
        }

        while(!carousel.compartment[2].isScanned())
        {
            wait();
        }

        if(carousel.compartment[2].scanned && carousel.compartment[2].isDefective() && !carousel.compartment[2].isInspected())
        {
            while(shuttleVial != null){wait();}

            //take C3 vial from carousel -> put on the shuttle
            System.out.println(indentation + carousel.compartment[2] + " [ c3"  + " -> S  ]");
            putShuttleVial(carousel.getC3Vial());

            //transferring

            //take vial from the shuttle -> put on the inspection bay position
            insCarousel.putVial(getShuttleVial());

        }
        // carousel vial and inspection vial change, notify all
        notifyAll();
    }




    /**
     * Aim: put this input vial at the shuttle, when the shuttle is empty
     * when the shuttle has vial, wait for the shuttle is empty to give input vial space
     * @param vial: the vial will be put
     * @throws InterruptedException
     */
    public synchronized void putShuttleVial(Vial vial) throws InterruptedException
    {
        //the shuttle has vial, wait it release space
        while(shuttleVial != null)
        {
            wait();
        }
        //let input vial on the shuttle
        this.shuttleVial = vial;
        //shuttle status changed, notify all
        notifyAll();
    }

    /**
     * Aim: take the vial from the shuttle
     * Process:
     *      1. when the shuttle is empty, wait for vial is on the shuttle
     *      2. take vial from shuttle
     *      3. clean the shuttle data
     *      4. return the shuttled vial and notify other threads that shuttle status has changed
     * @return
     * @throws InterruptedException
     */
    public synchronized Vial getShuttleVial() throws InterruptedException
    {
        //when the shuttle is empty, wait for vial is on the shuttle
        while(shuttleVial == null)
        {
            wait();
        }
        //take vial from shuttle
        Vial vial = shuttleVial;

        //clean the shuttle data
        reinitializeShuttle();

        //return the shuttled vial and notify other threads that shuttle status has changed
        notifyAll();
        return vial;
    }

    /**
     * Aim: clean the shuttle data
     * situation:
     *      when the shuttle finish one transmission, release shuttle space
     *      used when the vial is put on the destination
     */
    public synchronized void reinitializeShuttle()
    {
        this.shuttleVial = null;
    }

    public synchronized void checkC3hasVial()
    {
        if(carousel.compartment[2] != null)
        {
            notifyAll();
        }
    }
}
