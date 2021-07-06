/**
 * The main class for the vaccine fill/finish system simulator.
 */

public class Sim {
    /**
     * The main method to run the simulator.
     */
    public static void main(String[] args) {
        
    	// Create system components
    	Scanner sensor = new Scanner();
        Carousel carousel = new Carousel(sensor);
        Producer producer = new Producer(carousel);
        Consumer consumer = new Consumer(carousel);
        CarouselDrive drive = new CarouselDrive(carousel);
        Inspector inspector = new Inspector();
        Shuttle shuttle = new Shuttle(carousel, sensor, inspector);

        // start threads
        consumer.start();
        producer.start();
        drive.start();
        shuttle.start();

        // check all threads still live
        while (consumer.isAlive() && 
               producer.isAlive() && 
               drive.isAlive() &&
               shuttle.isAlive())
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                VaccineHandlingThread.terminate(e);
            }

        // interrupt other threads
        shuttle.interrupt();
        consumer.interrupt();
        producer.interrupt();
        drive.interrupt();

        System.out.println("Sim terminating");
        System.out.println(VaccineHandlingThread.getTerminateException());
        System.exit(0);
    }
}
