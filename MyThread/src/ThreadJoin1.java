import java.lang.*;
/*
This task is to explore the function of Thread.join()
 */
public class ThreadJoin1 extends Thread {
    public void run()
    {
        System.out.println( Thread.currentThread().getName()+":  Subthread is done");
    }

    public static void main(String[] args) throws InterruptedException
    {
        for(int i=0; i<5;i++)
        {
            ThreadJoin1 t1 = new ThreadJoin1();
            t1.start();

            try{
                t1.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+":  Main Thread is done.");
            System.out.println("---------------------");
        }
    }
}
