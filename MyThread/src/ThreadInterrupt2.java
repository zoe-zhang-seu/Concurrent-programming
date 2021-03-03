public class ThreadInterrupt2 extends Thread
{
    public void run(){
        try{
            Thread.sleep(1000);
            System.out.println("TASK:");

        }catch(InterruptedException e){
            System.out.println("Exception handled \n"+ e);
        }
        System.out.println("thread is running...");
    }

    public static void main(String args[]){
        ThreadInterrupt2 t1 = new ThreadInterrupt2();
        t1.start();
        t1.interrupt();
    }
}
