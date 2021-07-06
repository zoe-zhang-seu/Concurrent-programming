/**
 * A scanner to detect potentially defective vials of vaccine.
 */
public class Scanner {
	
	// true if the alarm is currently sounding
	private boolean alarm;
	
	/**
	 * Create a new scanner
	 */
	public Scanner() {
		this.alarm = false;
	}

	/**
	 * Scan the vial currently in the scanner position.
	 * @param vial The vial to be scanned
	 * @throws InterruptedException
	 */
	public synchronized void scan(Vial vial)
			throws InterruptedException {
		// Turn on alarm if vial is defective
		if (vial.isDefective() && !vial.isInspected()) {
			alarm = true;
		}
	}
	
	/**
	 * Get the current status of the alarm.
	 * @return
	 */
	public boolean alarm() {
		return alarm;
	}
	
	/**
	 * Turn the alarm off.
	 */
	public void clearAlarm() {
		alarm = false;
	}
}
