package pobieranko;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Hello world!");

        DownloadExample.sequentialDownload();
        DownloadExample.concurrentDownload1();
        DownloadExample.concurrentDownload2();
        DownloadExample.concurrentDownload25();
        DownloadExample.concurrentDownload3();
    }
}