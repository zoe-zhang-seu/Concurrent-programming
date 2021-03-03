public class ThreadInterrupt1 extends Thread {
    public void run()
    {
        try{
            Thread.sleep(1000);
            System.out.println("task");
        }catch(InterruptedException e){
            throw new RuntimeException("\n  Thread interrupted... \n  " +e);
        }
    }
    public static void main(String args[]){
        ThreadInterrupt1 t1 = new ThreadInterrupt1();
        t1.start();
        try{
            t1.interrupt();
        }catch(Exception e){
            System.out.println("\n  Exception handled \n" + e);
        }
    }
}
