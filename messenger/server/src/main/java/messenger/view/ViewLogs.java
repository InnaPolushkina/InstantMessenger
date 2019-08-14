package messenger.view;

import java.time.LocalDateTime;

/**
 * The class has methods for printing logs to server console
 * @author Inna
 */
public class ViewLogs {
    /**
     * The method for printing to console info
     * @param text log text
     */
    public static void printInfo(String text){
        System.out.println(LocalDateTime.now() + " : " + text);
    }

    /**
     * The method for printing to console errors
     * @param text error log text
     */
    public static void printError(String text) {
        System.err.println("\n" + LocalDateTime.now() + " : " + text + "\n");
    }
}
