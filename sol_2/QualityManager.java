/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
import java.util.Random;

/**
 * the quality manager take vials from the position 1 of inspection carousel at random intervals
 */

public class QualityManager extends VaccineHandlingThread {

    // the carousel from which the consumer takes vials
    protected InspectionCarousel insCarousel;

    /**
     * Create a new quality manager that collect vial from a carousel
     */
    public QualityManager(InspectionCarousel insCarousel) {
        super();
        this.insCarousel = insCarousel;
    }

    /**
     * Loop indefinitely trying to get vials from the carousel
     */
    public void run() {
        while (!isInterrupted()) {
            try {
                insCarousel.getVial();

                // let some time pass ...
                Random random = new Random();
                int sleepTime = Params.CONSUMER_MIN_SLEEP + 
                		random.nextInt(Params.CONSUMER_MAX_SLEEP - 
                				Params.CONSUMER_MIN_SLEEP);
                sleep(sleepTime);
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
        System.out.println("Quality manager terminated");
    }
}
