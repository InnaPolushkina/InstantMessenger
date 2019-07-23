package messenger.view;

import java.awt.*;

/**
 * The class Notificator contains method for notification about any server actions
 */
public class Notificator {
    /**
     * The public simple constructor
     */
    public Notificator() {
        super();
    }

    /**
     * The method notify user by TrayIcon
     * @param msg have string with message
     * @param caption have string with caption of notification
     * @param messageType have type of message
     * @see java.awt.TrayIcon.MessageType
     */
    public void notifyUser(String msg, String caption, TrayIcon.MessageType messageType) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        trayIcon.displayMessage(caption,msg,messageType);
    }
}
