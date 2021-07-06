/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
/**
 * Aim: An inspection bay to test the quality of vial
 * the defective vial will be given tags
 * ("i") represents inspected and ("t") represents tagged
 */

public class InspectionBay extends VaccineHandlingThread
{
    //import object form inspection carousel
    protected InspectionCarousel insCarousel;

    /**Constructor
     * Create a new Inspection bay which has empty vial
     */
    public InspectionBay(InspectionCarousel insCarousel)
    {
        super();
        this.insCarousel = insCarousel;
    }

    /**
     * Loop indefinitely trying to inspected the provided vial
     * then, sleep for a time period to mimic the process time
     */
    public void run()
    {
        while(!isInterrupted())
        {
            try
            {
                insCarousel.inspection();

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





}
