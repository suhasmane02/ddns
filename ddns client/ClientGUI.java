

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.GroupLayout;
import javax.swing.UIManager;

/**
 * This class generates the ddnsClient GUI, sets the menubar, DesktopPane etc..
 *
 * @author anonymous_02
 */
public class ClientGUI extends JFrame {

    private App app;
    public static DesktopPane desktop;
    private JMenuBar menuBar;
    private String fileName = "test.xml";
    private GroupLayout layout;

    /**
     * constructor sets client GUI attributes and renders the GUI.
     *
     * @param app 
     * @param fileName xml file name.
     */
    public ClientGUI(App app, String fileName) {
        super("DDNS Client");
        this.app = app;
        this.fileName = fileName;
        app.setHome(this);

        UIManager.getLookAndFeelDefaults().put("ClassLoader", this.getClass().getClassLoader());
        this.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        String jaxpPropertyName =
                "javax.xml.parsers.DocumentBuilderFactory";
        // Pass the parser factory in on the command line with
        // -D to override the use of the Apache parser.
        if (System.getProperty(jaxpPropertyName) == null) {
            String apacheXercesPropertyValue = //"org.xml.sax";
                    "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";
            System.setProperty(jaxpPropertyName,
                    apacheXercesPropertyValue);
        } else {
            System.out.println("jaxpPropertyName is not null");
            javax.swing.JOptionPane.showMessageDialog(this, "jaxpPropertyName is not null");
        }

        initComponents();
    }

    private void initComponents() {
        desktop = new DesktopPane(app);
        menuBar = new MenuBar(app);
        add(desktop);
        setLayout();
        addToClientGUI();
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

    private void addToClientGUI() {
        setJMenuBar(menuBar);
    }

    /**
     * Gets XML file name.
     *
     * @return String name of xml file.
     */
    public String getXMLFileName() {
        return fileName;
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
