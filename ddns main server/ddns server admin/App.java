
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.UIManager;

/** An entry point of ddnsServerAdmin.
 * Generally used to set and get ClientGUI attribute which is usable in DesktopPane.
 *
 * @author anonymous_02
 */
public class App {

    private AdminGUI home;

    /**
     * Sets the AdminGUI attribute.
     *
     * @param home AdminGUI instance
     */
    public void setHome(AdminGUI home) {
        this.home = home;
    }

    /**
     * Gets the AdminGUI attribute.
     *
     * @return AdminGUI instance.
     */
    public AdminGUI getHome() {
        return home;
    }

    /**
     * An entry point of ddnsServerAdmin - main method.
     *
     * @param args - command line arguments.
     */
    public static void main(String[] args) {
        App app = new App();

        Validate dialog = new Validate(new javax.swing.JFrame(), true, app);

        UIManager.getLookAndFeelDefaults().put("ClassLoader", dialog.getClass().getClassLoader());
        dialog.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
}
