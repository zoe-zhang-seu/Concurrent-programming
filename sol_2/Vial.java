/**Author:
 *          name: yanni zhang
 *          ID:1132371
 */
import java.util.Random;

/**
 * A class representing a vial moving through quality control
 */
public class Vial
{
    protected static Random r = new Random();
    
    // specifies whether the vial is defective
    protected boolean defective = false;
    
    // specifies whether the vial has been inspected
    protected boolean inspected = false;

    // specifies whether the vial has been tagged for destruction 
    protected boolean tagged = false;

    // specifies this is required to be shuttle
    protected volatile boolean scanned = false;

    protected volatile boolean requiredInspect = false;
    protected volatile boolean atInsSide = false;//vial at Inspection side, which is initialized false
    
    // the ID of this vial
    protected int id;

    // the next ID that can be allocated
    protected static int nextId = 1;

    // create a new vial with a given ID
    private Vial(int id)
    {
        this.id = id;
        if (r.nextFloat() < Params.DEFECT_PROB)
        {
         	defective = true;
        }
    }

    /**
     * @return a new vial instance with its unique ID.
     */
    public static Vial getInstance()
    {
        return new Vial(nextId++);
    }

    /**
     * @return the ID of this vial.
     */
    public int getId() {
        return id;
    }

    /**
     * Set defective status of vial to true.
     */
    public void setDefective() {
        defective = true;
    } 
    
    /**
     * @return true if and only if this vial is defective.
     */
    public boolean isDefective() {
        return defective;
    }
    
    /**
     * Flag that the vial has been inspected.
     */
    public void setInspected() {
    	inspected = true;
    }
    
    /**
     * @return true if and only if this vial has been inspected.
     */
    public boolean isInspected() {
        return inspected;
    }  
    
    /**
     * Tag this vial for destruction 
     */
    public void setTagged() {
        tagged = true;
    }
    
    /**
     * @return true if and only if this vial is tagged for destruction
     */
    public boolean isTagged() {
        return tagged;
    }
    //////////
    /**
     * The scanner will set this tag invisible  is scanned
     */
    public synchronized void setIsScanned(){ scanned = true;}
    ///////////
    /**
     * @return true if and only if this vial is required to is scanned
     */
    public synchronized boolean isScanned(){ return scanned;}

    /**
     * The scanner will set this tag invisible  is required to inspect
     */
    public synchronized void setIsRequiredInspect(){ requiredInspect = true;}

    ///////////
    /**
     * @return true if and only if this vial is required to inspect
     */
    public synchronized boolean isRequiredInspect(){ return requiredInspect;}

    /**
     * The vial is transferred from C3
     */
    public synchronized void setAtInsSide() {atInsSide = true;}

    ///////////
    /**
     * @return true if and only if this vial is at Inspection position
     */
    public synchronized boolean isAtInsSide(){ return atInsSide;}

    public String toString()
    {
    	String dFlag = defective ? "d" : "-";
    	String iFlag = inspected ? "i" : "-";
    	String tFlag = tagged ? "t" : "-";
        return "V:" + String.format("%03d", id) + "(" + dFlag + iFlag + tFlag + ")";
    }
}
