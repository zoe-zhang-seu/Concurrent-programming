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
        Carousel carousel = new Carousel(sensor, 5, "C1");
        Carousel carousel_two = new Carousel(null, 2, "C2");
        Producer producer = new Producer(carousel);
        Consumer consumer = new Consumer(carousel);
        Consumer consumer_two = new Consumer(carousel_two);
        CarouselDrive drive = new CarouselDrive(carousel);
        CarouselDrive drive_two = new CarouselDrive(carousel_two);
        Inspector inspector = new Inspector();
        Shuttle shuttle = new Shuttle(carousel, carousel_two, sensor, inspector);

        // start threads
        consumer.start();
        consumer_two.start();
        producer.start();
        drive.start();
        drive_two.start();
        shuttle.start();

        // check all threads still live
        while (consumer.isAlive() && 
        	   consumer_two.isAlive() &&
               producer.isAlive() && 
               drive.isAlive() &&
               drive_two.isAlive() &&
               shuttle.isAlive())
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                VaccineHandlingThread.terminate(e);
            }

        // interrupt other threads
        shuttle.interrupt();
        consumer.interrupt();
        consumer_two.interrupt();
        producer.interrupt();
        drive.interrupt();
        drive_two.interrupt();

        System.out.println("Sim terminating");
        System.out.println(VaccineHandlingThread.getTerminateException());
        System.exit(0);
    }
}
