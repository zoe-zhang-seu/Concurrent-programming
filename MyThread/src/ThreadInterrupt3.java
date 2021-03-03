public class ThreadInterrupt3 extends Thread
{
    Thread t;
    public void run(){
        t = Thread.currentThread();
        for(int i=1;i<=2;i++){
            if(Thread.interrupted()){
                System.out.println(t.getName() + "   code for interrupted thread");
            }
            else{
                System.out.println(t.getName() + "  code for normal thread");
            }

        }//end of for loop
    }

    public static void main(String args[]){

        ThreadInterrupt3 t1=new ThreadInterrupt3();
        ThreadInterrupt3 t2=new ThreadInterrupt3();

        t1.start();
        t1.interrupt();

        t2.start();
        t2.interrupt();

    }
}
