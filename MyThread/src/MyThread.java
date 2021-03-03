public class MyThread extends Thread{
    public void run()
    {
        System.out.println("This are the Threads:");
        for(int i = 0; i < 10;i++)
        {
            System.out.println("\t thread " + i);
        }
    }
}
