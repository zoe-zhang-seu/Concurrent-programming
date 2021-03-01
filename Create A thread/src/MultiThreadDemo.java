public class MultiThreadDemo {
    public static void main(String args[]) {
        new MultiThread();
        try {
            System.out.println("Main Thread");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("The Main thread is interrupted");
        }
        System.out.println("Exiting the Main thread");
    }
}
