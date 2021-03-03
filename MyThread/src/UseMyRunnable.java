public class UseMyRunnable
{
    public static void main(String[] args)
    {
        Thread myRunnable = new Thread(new MyRunnable());
        myRunnable.start();
    }
}
