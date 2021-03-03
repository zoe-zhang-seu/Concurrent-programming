public class UseMyThread {
    public static void main(String[] args)
    {
        Thread myThread = new MyThread();
        myThread.run();
        myThread.run();

        /*
        //it shows that start is start a new thread,
        but cannot invoke duplicatedly

        Thread myThread = new MyThread();
        myThread.start();
        myThread.start();
        */

        /*
        Thread t = new MyThread();
        t.run();
        */
    }
}

