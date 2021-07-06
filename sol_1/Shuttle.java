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
    protected Inspection inspection;
    protected Carousel carousel;

    protected volatile Vial shuttleVial;//the vial at shuttle
    final private static String indentation = "                  ";

    /**
     * Create a new forward shuttle that produce vial for inspection
         * Direction: carousel [C3] -> inspection [x]
         * role : act as a producer for inspection
     */
    public Shuttle(Carousel carousel, Inspection inspection)
    {
        super();
        this.carousel = carousel;
        this.inspection = inspection;

        this.shuttleVial = null;//initialize the backward shuttle is empty
      }




    /**
     * Loop indefinitely trying to get vials from the carousel
     * situation 1: C3 is empty && inpection is empty --> wait
     * situation 2: C3 has vial, inspection is empty
     *      2-1 : vial does not finish scanning --> wait
     *      2-2 :vial finished scanned and not need transfer
     *      2-3 : vial is defective and not inspected yet  ---> tranmission process
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
                //situation 1: C3 is empty && inpection is empty --> wait
                //check if the C3, shuttle and Inspection are empty, wait until vial enter
                isWholeEmpty();

                //Situation 2: C3 has vial, inspection is empty
                if((carousel.compartment[2] !=null) && (inspection.ins_vial ==null))
                {
                    ForwardTransferProcess();
                }
                //situation 3: C3 and inspection has vial
                waitRelease();

                //situation 4: C3 empty and inspection has vial
                if(inspection.ins_vial!=null && (carousel.compartment[2] ==null) )
                {
                    BackwardTransferProcess();
                }

                //let some time go
                int onceShuttleTime = Params.SHUTTLE_TIME;
                sleep(onceShuttleTime);
            } catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("Forward Transmission terminated");
    }

    /**
     * when there is no vial at the system (C3, shuttle, inspection)
     * wait until the vial is provided
     * @throws InterruptedException
     */
    public synchronized void isWholeEmpty() throws InterruptedException
    {
        while((inspection.ins_vial ==null) && (carousel.compartment[2] ==null) && (shuttleVial ==null))
        {
            wait();
        }
    }


    /**
     * when C3 and inspection site both have vial,
     * wait until one space is released to shuttle
     * @throws InterruptedException
     */
    public synchronized void waitRelease() throws InterruptedException
    {
        while((inspection.ins_vial!=null) && (carousel.compartment[2] != null))
        {
            wait();
        }
    }


    /**
     * Aim: Transfer vial from C3 to Inspection bay
     * process:
     *      1. while c3 is empty, wait
     *      2. while c3 vial has not finished scanning yet, wait
     *      3. while shuttle has vial, wait for release the space
     *      4. take C3 vial from carousel -> put on the shuttle
     *      5. transfer
     *      6. while Inspection has vial,wait
     *      7. take vial from the shuttle -> put on the inspection bay position
     * @throws InterruptedException
     */
    public synchronized void ForwardTransferProcess() throws InterruptedException
    {
        //when C3 is empty, wait for the vial reached c3 position
        while(carousel.compartment[2] == null){wait();}

        //if C3 vial does not finish scanning, just wait for it finished
        while(!carousel.compartment[2].isScanned()){wait();}

        //if C3 vial is required to inspect but not done yet, start the transmission
        if(carousel.compartment[2].scanned && carousel.compartment[2].isDefective()
                && !carousel.compartment[2].isInspected())
        {
            //while shuttle has vial, wait shuttle release space
            while(shuttleVial!= null)
            {
                wait();
            }

            //take C3 vial from carousel -> put on the shuttle
            System.out.println(indentation + carousel.compartment[2] + " [ c3"  + " -> S  ]");
            putShuttleVial(carousel.getC3Vial());

            //transferring

            //take vial from the shuttle -> put on the inspection bay position
            while(inspection.ins_vial !=null)
            {
                wait();
            }

            //vial has been taken from the shuttle, and put it in the inspection position
            inspection.putInsPositionVial(getShuttleVial());
        }
        // carousel vial and inspection vial change, notify all
        notifyAll();
    }

    /**
     * Aim:Transfer vial from Inspection bay to C3
     * process:
     *      1. while inspection is empty, wait
     *      2. while vial has not finished inspection , wait it finished
     *      3. while shuttle is full, wait for it release the space
     *      4. take vial from inspection -> put on the shuttle , clean the inspection data
     *      5. transfer
     *      6. while C3 is not empty, wait its position released to insert vial
     *      7. take vial from the shuttle -> put on the carousel C3 position, clean the shuttle data
     * @throws InterruptedException
     */
    public synchronized void BackwardTransferProcess() throws InterruptedException
    {
        while(inspection.ins_vial == null) {wait();}

        while(!inspection.ins_vial.isTagged()){wait();}

        while(shuttleVial!=null){wait();}
        // take the vial from the inspection point and put on the backward shuttle
        putShuttleVial(inspection.getInsPositionVial());
        inspection.reinitializeInspection(); //clean the data at inspection

        //transfer

        //while C3 has vial, wait that position release
        while(carousel.compartment[2] !=null){wait();}
        //take vial from the shuttle -> put on the carousel C3 position, clean the shuttle data
        carousel.putC3Vial(getShuttleVial());
        reinitializeShuttle();

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

    /**
     * if c3 has vial, notify shuttle that the block of (C3, shuttle, inspection) can  be
     * released
     */
    public synchronized void checkC3HasVial()
    {
        if(carousel.compartment[2] != null)
        {
            notifyAll();
        }
    }

}
