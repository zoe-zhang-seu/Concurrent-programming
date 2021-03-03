
/*
This is to explore the yield function
 */
public class ThreadYield3 extends Thread {

    public ThreadYield3(String name) {
        super(name);
    }

    @Override
    public void run()
    {
        for (int i = 1; i <= 10; i++)
        {
            System.out.println(Thread.currentThread().getName() + ":  " + i);
            //when i== 30, the A thread will turn the state from running to
            //runnable, which give a chance to compete the CPU. So CPU can be got by A OR B
            if (i == 5) {
                System.out.println("***before the yield() works***");
                this.yield();
                System.out.println("***after the yield() works***");
            }
        }
    }

    public static void main(String[] args) {
        ThreadYield3 t1 = new ThreadYield3("A");
        ThreadYield3 t2 = new ThreadYield3("B");
        t1.start();
        t2.start();
    }
}

