public class MyClass implements Runnable {
    @Override
    public void run() {

        System.out.println("MyClass running");
    }
    static void threadMessage(String message) {
        String threadName =Thread.currentThread().getName();
        System.out.format("%s: %s",threadName,message);
    }
    public static void main (String args[])throws InterruptedException{
        String message = "is created ";
        Thread t1 = new Thread(new MyClass ());
        threadMessage(message);
        t1.start();




    }
}
