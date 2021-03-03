
import java.lang.*;

class toYield extends Thread{
    public void run(){
        for(int i=0;i<5;i++){
            System.out.println(Thread.currentThread().getName() +
                    " in control.");
        }
    }
}

public class ThreadYield{
    public static void main(String[] args){
        toYield t = new toYield();
        t.start();

        for (int i = 0;i< 5;i++){
            Thread.yield();
            System.out.println(Thread.currentThread().getName() +
                    " in control.");
        }
    }


}
