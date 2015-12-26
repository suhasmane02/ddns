
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.UIManager;

/**
 * This class generates the ddnsServerAdmin GUI, sets the menubar, DesktopPane etc..
 *
 * @author anonymous_02
 */
public class AdminGUI extends JFrame {

    private App app;
    public static DesktopPane desktop;
    private JMenuBar menuBar;
    private GroupLayout layout;

    /**
     * 
     * @param app
     */
    public AdminGUI(App app) {
        super("DDNS Admin");
        this.app = app;
        app.setHome(this);

        UIManager.getLookAndFeelDefaults().put("ClassLoader", this.getClass().getClassLoader());
        this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        initComponents();
    }

    private void initComponents() {
        desktop = new DesktopPane(app);
        menuBar = new MenuBar(app);
        add(desktop);
        setLayout();
        addToAdminGUI();
        setVisible(true);
        pack();
    }

    private void setLayout() {
        layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(desktop, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE));
    }

    private void addToAdminGUI() {
        setJMenuBar(menuBar);
    }

    /**
     * Loads the {@code LookAndFeel} specified by the given class
     * name, using the current thread's context class loader, and
     * passes it to {@code setLookAndFeel(LookAndFeel)}.
     *
     * @param name  a string specifying the name of the class that implements
     *        the look and feel
     */
    public void setLookAndFeel(String name) {
        if (name == null) {
            return;
        }
        try {
            UIManager.setLookAndFeel(name);
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
