public class ThreadInterference1 extends Thread
{

        static volatile int n = 0;
        public void run()
        {
            int temp;
            for (int i = 0; i < 10; i++)
            {
                temp = n;
                n = temp + 1;
            }
        }
        public static void main(String[] args)
        {
            ThreadInterference1 p = new ThreadInterference1();
            ThreadInterference1 q = new ThreadInterference1();
            p.start();
            q.start();
            try {
                p.join();
                q.join();
            }
            catch (InterruptedException e) { }
            System.out.println("The final value of n is " + n);
        }
}
