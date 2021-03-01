class MultiThread implements Runnable {
    Thread t;
    MultiThread() {
        t = new Thread(this, "Thread");
        System.out.println("Child thread: " + t);
        t.start();
    }
    public void run() {
        try {
            System.out.println("Child Thread");
            Thread.sleep(50);
        } catch (InterruptedException e) {
            System.out.println("The child thread is interrupted.");
        }
        System.out.println("Exiting the child thread");
    }
}
