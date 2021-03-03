import java.lang.*;

public class ThreadJoin2 extends Thread
{
    public void run(){
        Thread t = Thread.currentThread();
        System.out.println("Current thread: "+ t.getName() );
        System.out.println("Is it Alive?" + t.isAlive());
    }

    public static void main(String args[])throws Exception
    {
        Thread t1 = new Thread(new ThreadJoin2());
        t1.start();

        Thread t2= new Thread(new ThreadJoin2());
        t2.start();
        //wait for 1000ms unitl this thread to die
        t2.join(50);

        System.out.println(t2.getName() + " Joining after 50"+
                " mili seconds: \n");
        System.out.println("Current thread: " + t2.getName());
        // Checks if this thread is alive
        System.out.println("Is t1 alive? " + t1.isAlive());
        System.out.println("Is t2 alive? " + t2.isAlive());
    }


}
