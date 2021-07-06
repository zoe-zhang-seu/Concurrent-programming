/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
/**
 * A scanner take determined intervals to scan the vial
 * from the 3rd compartment of a carousel and give forward transmission a hint
 * that this vial is required to transfer to get inspection
 */

public class Scanner extends VaccineHandlingThread
{
    protected Carousel carousel;
    protected Shuttle shuttle;

    /**
     * Create a new scanner that scan a vial in carousel C3 and it links the forward trans
     */
    public Scanner(Carousel carousel, Shuttle shuttle)
    {
        super();
        this.carousel = carousel;
        this.shuttle = shuttle;
    }

    /**
     * Loop indefinitely to scan the C3 while
     */
    public void run()
    {
                    while (!isInterrupted())
                    {
                            try
                            {
                                    carousel.scanTheVial();
                                    shuttle.checkC3hasVial();

                            }catch (InterruptedException e)
                            {
                                this.interrupt();
                            }
                    }
                    System.out.println("Scanner terminated");
    }
}
