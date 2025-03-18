package co.istad.ems.util;

public class DisplayLoadingAnimation {
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";
    public void  displayLoadingAnimation() throws InterruptedException {
        char[] spinner = {'|', '/', '-', '\\'};
        String loadingText = "Loading ";
        System.out.print(ANSI_GREEN + loadingText);

        for (int i = 0; i < 20; i++) {
            System.out.print(spinner[i % spinner.length]);
            System.out.flush();
            Thread.sleep(80);
            System.out.print("\b");
        }

        // Clear "Loading " and spinner, then print "Done!"
        System.out.print("\r" + "Done!      " + ANSI_RESET + "\n");
    }
}
