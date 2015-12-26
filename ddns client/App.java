/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.UIManager;

/** An entry point of ddnsClient.
 * Generally used to set and get ClientGUI attribute which is usable in DesktopPane.
 *
 * @author anonymous_02
 */
public class App {

    private ClientGUI home;

    /**
     * Sets the ClientGUI attribute.
     *
     * @param home ClientGUI instance
     */
    public void setHome(ClientGUI home) {
        this.home = home;
    }

    /**
     * Gets the ClientGUI attribute.
     *
     * @return ClientGUI instance.
     */
    public ClientGUI getHome() {
        return home;
    }

    /**
     * An entry point of ddnsClient - main method.
     *
     * @param args - command line arguments.
     */
    public static void main(String[] args) {
        App app = new App();
        if (args.length != 1) {
            System.out.println("Usage: java app ServermachineAddress");
            System.exit(0);
        }

//        String arg = "192.168.24.253";
        Validate dialog = new Validate(new javax.swing.JFrame(), true, args[0], app);

        UIManager.getLookAndFeelDefaults().put("ClassLoader", dialog.getClass().getClassLoader());
        dialog.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
}
