/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
/**
 * A scanner take determined intervals to scan the vial
 * from the 3rd compartment of a carousel
 */

public class Scanner extends VaccineHandlingThread
{
    protected Carousel carousel;
    protected Shuttle shuttle;

    /**
     * Create a new scanner that scan a vial in carousel C3 and it links the shuttle
     */
    public Scanner(Carousel carousel, Shuttle shuttle)
    {
        super();
        this.carousel = carousel;
        this.shuttle = shuttle;
    }

    /**
     * Loop indefinitely to scan the C3
     */
    public void run()
    {
                    while (!isInterrupted())
                    {
                            try
                            {
                                    carousel.scanTheVial();// scan the c3 vial
                                    shuttle.checkC3HasVial();// notify shuttle when C3 has vial

                            }catch (InterruptedException e)
                            {
                                this.interrupt();
                            }
                    }
                    System.out.println("Scanner terminated");
    }


}
