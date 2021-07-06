/**
 * The Shuttle moves vials that have been identified by for more
 * detailed inspection from the carousel to the inspection bay
 * and then back to the carousel.
 */
public class Shuttle extends VaccineHandlingThread {
	
	// the carousel, scanner, and inspector
	protected Carousel carousel;
	protected Scanner scanner;
	protected Inspector inspector;
	
	// the vial currently in the shuttle
	protected Vial vial;
	
    final private static String indentation = "                  ";
    
	/**
	 * Create a new shuttle.
	 */
	public Shuttle(Carousel carousel, Scanner scanner, Inspector inspector) {
		super();
		this.carousel = carousel;
		this.scanner = scanner;
		this.inspector = inspector;
		vial = null;
	}
	
	/**
	 * Iterate forever, moving vials between the carousel and the inspector.
	 */
	public void run() {
		while (!isInterrupted()) {
			try {
				if (scanner.alarm() && !inspector.hasVial()) {
					vial = carousel.getVial();
					scanner.clearAlarm();
					sleep(Params.SHUTTLE_TIME);
					inspector.putVial(vial);
					System.out.println(indentation +
							vial + " [  S -> I  ]");
					inspector.inspect();
					vial = inspector.getVial();
					System.out.println(indentation +
							vial + " [  I -> S  ]");
					sleep(Params.SHUTTLE_TIME);
					carousel.handBack(vial);	
				}
			} catch (OverloadException e) {
				terminate(e);
			} catch (InterruptedException e) {
                this.interrupt();
            }
		}
	}
}
