import java.lang.*;
/*
This class is to explore the thread interleaving features using sleep
 */
public class ThreadSleep implements Runnable{

    Thread t;

    public void run(){
        for(int i= 0 ; i < 10; i++){
            System.out.println(Thread.currentThread().getName() +
                    " " + i);
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(new ThreadSleep());
        t1.start();

        Thread t2 = new Thread(new ThreadSleep());
        t2.start();
    }
}
