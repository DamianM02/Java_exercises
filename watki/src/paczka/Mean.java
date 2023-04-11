package paczka;

import java.util.Locale;
import java.util.concurrent.*;

public class Mean {
    static double[] array;
    static Semaphore sem = new Semaphore(0);
    static BlockingQueue<Double> results = new ArrayBlockingQueue<Double>(100);
    static void initArray(int size){
        array = new double[size];
        for(int i=0;i<size;i++){
            array[i]= Math.random()*size/(i+1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        initArray(12800000);
        for(int cnt:new int[]{1,2,4,8,16,32,64,128}){
            parallelMean(cnt);
        }
    }




    static class MeanCalc extends Thread{
        private final int start;
        private final int end;
        double mean = 0;

        MeanCalc(int start, int end){
            this.start = start;
            this.end=end;
        }
        public void run(){
            // ??? liczymy średnią
            //double s=0;
            for (int i=start; i<end; i++){
                mean+=array[i];
            }
            mean/=(end-start);
            try {
                results.put(mean);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.printf(Locale.US,"%d-%d mean=%f\n",start,end,mean);
            sem.release();
        }
    }


    /**
     * Oblicza średnią wartości elementów tablicy array uruchamiając równolegle działające wątki.
     * Wypisuje czasy operacji
     * @param cnt - liczba wątków
     */
    static void parallelMean(int cnt) throws InterruptedException {
        // utwórz tablicę wątków
        MeanCalc threads[]=new MeanCalc[cnt];
        // utwórz wątki, podziel tablice na równe bloki i przekaż indeksy do wątków
        // załóż, że array.length dzieli się przez cnt)

        for (int i=0; i<cnt; i++){
            threads[i]=new MeanCalc(i*array.length/cnt, (i+1)*array.length/cnt);
        }
        double t1 = System.nanoTime()/1e6;
            for(MeanCalc mc:threads){
                mc.start();
            }
        double t2 = System.nanoTime()/1e6;
        // czekaj na ich zakończenie używając metody ''join''
        for(MeanCalc mc:threads) {
            mc.join();
        }
        // oblicz średnią ze średnich
        double mean=0;
        for(MeanCalc mc:threads){
            mean+=mc.mean;
        }
        mean/=cnt;
        double t3 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1,
                mean);
    }


    static void parallelMean2(int cnt) throws InterruptedException {
        // utwórz tablicę wątków
        MeanCalc threads[]=new MeanCalc[cnt];

        // utwórz wątki, podziel tablice na równe bloki i przekaż indeksy do wątków
        // załóż, że array.length dzieli się przez cnt)

        for (int i=0; i<cnt; i++){
            threads[i]=new MeanCalc(i*array.length/cnt, (i+1)*array.length/cnt);
        }
        double t1 = System.nanoTime()/1e6;
        for(MeanCalc mc:threads){
            mc.start();
        }
        double t2 = System.nanoTime()/1e6;

        // oblicz średnią ze średnich
        sem.acquire(cnt);
        double mean =0;
        for (double i : results){
            mean+=i;
        }
        mean /= results.size();
        double t3 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1,
                mean);
    }


    static void parallelMean3(int cnt) throws InterruptedException {
        // utwórz tablicę wątków
        //MeanCalc threads[]=new MeanCalc[cnt];
        // utwórz wątki, podziel tablice na równe bloki i przekaż indeksy do wątków
        // załóż, że array.length dzieli się przez cnt)

        ExecutorService executor = Executors.newFixedThreadPool(16);




        double t1 = System.nanoTime()/1e6;
        for (int i=0; i<cnt; i++){
            executor.execute(new MeanCalc(i*array.length/cnt, (i+1)*array.length/cnt)) ;
        }
        executor.shutdown();


        double t2 = System.nanoTime()/1e6;
        // czekaj na ich zakończenie używając metody ''join''

        // oblicz średnią ze średnich
        sem.acquire(cnt);
        double mean =0;
        for (double i : results){
            mean+=i;
        }
        mean /= results.size();
        double t3 = System.nanoTime()/1e6;
        System.out.printf(Locale.US,"size = %d cnt=%d >  t2-t1=%f t3-t1=%f mean=%f\n",
                array.length,
                cnt,
                t2-t1,
                t3-t1,
                mean);
    }


//    ExecutorService executor = Executors.newFixedThreadPool(16);
//        for(int i=0;i<100;i++){
//        executor.execute(new RunnableOrThread(i));
//    }
//        executor.shutdown();
}
