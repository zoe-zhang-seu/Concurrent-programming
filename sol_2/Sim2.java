/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */

/**
 * The main class for the vaccine fill/finish system simulator.
 */

public class Sim2
{
    /**
     * The main method to run the simulator.
     */
    public static void main(String[] args)
    {

        // Create system components
        Carousel carousel = new Carousel();
        CarouselDrive driver = new CarouselDrive(carousel);
        // inpection carousel and its drive
        InspectionCarousel inspectionCarousel = new InspectionCarousel();
        InspectionBay inspectionBay = new InspectionBay(inspectionCarousel);
        InspectionCarouselDrive inspectionDriver = new InspectionCarouselDrive(inspectionCarousel);

        Producer producer = new Producer(carousel);
        Consumer consumer = new Consumer(carousel);
        QualityManager qualityManager = new QualityManager(inspectionCarousel);

        Shuttle shuttle = new Shuttle(carousel,inspectionCarousel);
        Scanner scanner = new Scanner(carousel,shuttle);


        // start threads
        consumer.start();
        producer.start();
        qualityManager.start();
        driver.start();
        inspectionDriver.start();
        scanner.start();
        inspectionBay.start();
        shuttle.start();


        // check all threads still live
        while (consumer.isAlive() &&
                producer.isAlive() &&
                driver.isAlive() && scanner.isAlive() &&
                shuttle.isAlive() && inspectionBay.isAlive() && inspectionDriver.isAlive() && qualityManager.isAlive()

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
        inspectionDriver.interrupt();
        scanner.interrupt();
        inspectionBay.interrupt();
        shuttle.interrupt();
        qualityManager.interrupt();

        //backwardTrans.interrupt();

        System.out.println("Sim terminating");
        System.out.println(VaccineHandlingThread.getTerminateException());
        System.exit(0);
    }
}
