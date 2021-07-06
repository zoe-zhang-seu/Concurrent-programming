/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */

/**
 * The main class for the vaccine fill/finish system simulator.
 */

public class Sim1
{
    /**
     * The main method to run the simulator.
     */
    public static void main(String[] args)
    {
        
    	// Create system components
        Carousel carousel = new Carousel();
        Producer producer = new Producer(carousel);
        Consumer consumer = new Consumer(carousel);
        CarouselDrive driver = new CarouselDrive(carousel);
        Inspection inspection = new Inspection();
        Shuttle shuttle = new Shuttle(carousel,inspection);
        Scanner scanner = new Scanner(carousel,shuttle);


        // start threads
        consumer.start();
        producer.start();
        driver.start();
        scanner.start();
        inspection.start();
        shuttle.start();

        // check all threads still live
        while (consumer.isAlive() && 
               producer.isAlive() && 
               driver.isAlive() && scanner.isAlive() &&
                shuttle.isAlive() && inspection.isAlive()
        )
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                VaccineHandlingThread.terminate(e);
            }

        // interrupt other threads
        consumer.interrupt();
        producer.interrupt();
        driver.interrupt();
        scanner.interrupt();
        inspection.interrupt();
        shuttle.interrupt();
        //backwardTrans.interrupt();

        System.out.println("Sim terminating");
        System.out.println(VaccineHandlingThread.getTerminateException());
        System.exit(0);
    }
}
