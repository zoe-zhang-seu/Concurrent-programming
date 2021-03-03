public class MyRunnable implements Runnable
{
    public void run()
    {
        System.out.println("This is Runnable");
        for(int i= 0;i<10;i++){
            System.out.println("\t runnable " + i);
        }
    }
}
