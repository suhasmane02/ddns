
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.GroupLayout;

/**
 * This class initializes DesktopPane with InternalFrames.
 *
 * @author anonymous_02
 */
public class DesktopPane extends JDesktopPane {

    private App app;
    public static JInternalFrame fileSysInternalFrame;
    private JScrollPane scrollPane;
    private GroupLayout layout;
    private javax.swing.JButton addremNodeButton;
    private javax.swing.JButton addremUserButton;
    private javax.swing.JButton genDataButton;
    private javax.swing.JLabel welcomeLabel;

    /**
     * 
     * @param app
     */
    public DesktopPane(App app) {
        super();
        this.app = app;
        System.err.println("in DesktopPane constructor .....app is" + app);
        initComponents();
    }

    private void initComponents() {
        fileSysInternalFrame = new JInternalFrame();
        scrollPane = new JScrollPane();
        addremNodeButton = new JButton();
        addremUserButton = new JButton();
        genDataButton = new JButton();
        welcomeLabel = new javax.swing.JLabel();

        setPropertiesAndListeners();
    }

    private void setPropertiesAndListeners() {
        fileSysInternalFrame.setIconifiable(true);
        fileSysInternalFrame.setMaximizable(false);
        fileSysInternalFrame.setResizable(false);
        fileSysInternalFrame.setTitle("CM Administration");
        fileSysInternalFrame.setAutoscrolls(true);
        fileSysInternalFrame.setFocusCycleRoot(false);
        fileSysInternalFrame.setOpaque(false);

        try {
            fileSysInternalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        addremUserButton.setText("Add or Remove User");
        addremNodeButton.setText("Add or Remove Node");

        welcomeLabel.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        welcomeLabel.setText("Wel-Come to File System Administration");
        welcomeLabel.setAutoscrolls(true);

        genDataButton.setText("Generate Database of FileSystem");

        fileSysInternalFrame.setBounds(0, 0, 450, 300);

        addListeners();
        setLayoutAndAddtoDesktop();

        setVisible(true);
        fileSysInternalFrame.setVisible(true);
    }

    private void addListeners() {
        OpenButtonHandler obhUser = new OpenButtonHandler(this, addremUserButton);
        addremUserButton.addActionListener(obhUser);

        OpenButtonHandler obhNode = new OpenButtonHandler(this, addremNodeButton, genDataButton);
        addremNodeButton.addActionListener(obhNode);

        OpenButtonHandler obhGenDB = new OpenButtonHandler(this, genDataButton, addremNodeButton);
        genDataButton.addActionListener(obhGenDB);
    }

    private void setLayoutAndAddtoDesktop() {
        layout = new javax.swing.GroupLayout(fileSysInternalFrame.getContentPane());
        fileSysInternalFrame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(50, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(welcomeLabel).addComponent(addremNodeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(addremUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(genDataButton, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(54, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(welcomeLabel).addGap(18, 18, 18).addComponent(addremUserButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(addremNodeButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(genDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(160, 160, 160)));

        fileSysInternalFrame.setBounds(0, 0, 450, 300);

        add(fileSysInternalFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }
}