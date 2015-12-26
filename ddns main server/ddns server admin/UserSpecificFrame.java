
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.ItemEvent;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import java.sql.*;

/**
 *
 * @author anonymous_02
 */
public class UserSpecificFrame extends JInternalFrame {

    private JDesktopPane dPane;
    private javax.swing.JRadioButton adduserRadio;
    private javax.swing.JLabel userlistLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton passwdRadio;
    private javax.swing.JPasswordField passwdText;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JButton proceedButton;
    private javax.swing.JLabel reenterPasswordLabel;
    private javax.swing.JRadioButton remuserRadio;
    private javax.swing.JPasswordField repasswdText;
    private javax.swing.JList userList;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameText;
    private javax.swing.ButtonGroup userButtonGroup;
    private javax.swing.DefaultListModel listModel;
    public int nextFrameX;
    public int nextFrameY;
    public int frameDistance;
    java.sql.Connection conn = null;
    java.sql.Statement stmt;
    java.sql.ResultSet rs;
    CryptoLibrary cl;

    /**
     * Initializes the User Administration IntrnalFrame.
     *
     * @param dPane
     */
    public UserSpecificFrame(JDesktopPane dPane) {
        this.dPane = dPane;
        initComponents();
    }

    private void initComponents() {

        cl = new CryptoLibrary();
        jScrollPane1 = new javax.swing.JScrollPane();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        reenterPasswordLabel = new javax.swing.JLabel();
        adduserRadio = new javax.swing.JRadioButton();
        remuserRadio = new javax.swing.JRadioButton();
        passwdRadio = new javax.swing.JRadioButton();
        usernameText = new javax.swing.JTextField();
        passwdText = new javax.swing.JPasswordField();
        repasswdText = new javax.swing.JPasswordField();
        proceedButton = new javax.swing.JButton();
        userlistLabel = new javax.swing.JLabel();
        userButtonGroup = new javax.swing.ButtonGroup();
        listModel = new javax.swing.DefaultListModel();
        userList = new javax.swing.JList(listModel);

        userButtonGroup.add(passwdRadio);
        userButtonGroup.add(adduserRadio);
        userButtonGroup.add(remuserRadio);

        jScrollPane1.setViewportView(userList);
        usernameLabel.setText("User Name");
        passwordLabel.setText("Password");
        reenterPasswordLabel.setText("Re - Enter Password");
        adduserRadio.setText("Add User");
        remuserRadio.setText("Remove User");
        passwdRadio.setText("Set New Password");
        proceedButton.setText("Proceed");
        userlistLabel.setText("Available User List");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setTitle("User Specific");
        setAutoscrolls(true);

        initDatabase();

        addListeners();
        setLayout();

        setVisible(true);
        dPane.add(this, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    private void addListeners() {

        userList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                usernameText.setText((String) userList.getSelectedValue());
            }
        });

        adduserRadio.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (adduserRadio.isSelected()) {
                    passwdText.setEnabled(true);
                    repasswdText.setEnabled(true);
                }
            }
        });

        remuserRadio.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (remuserRadio.isSelected()) {
                    passwdText.setEnabled(false);
                    repasswdText.setEnabled(false);
                }
            }
        });

        passwdRadio.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (passwdRadio.isSelected()) {
                    passwdText.setEnabled(true);
                    repasswdText.setEnabled(true);
                }
            }
        });

        proceedButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                String user = usernameText.getText();

                try {
                    if (adduserRadio.isSelected()) {
                        String passwd = passwdText.getText();
                        String passwd2 = repasswdText.getText();

                        if (passwd.equals(passwd2)) {
                            int num = 0;
                            ResultSet r = stmt.executeQuery("select max(u_id) as user_id from users");
                            while (r.next()) {
                                num = r.getInt("user_id");
                                num++;
                            }
                            int val = stmt.executeUpdate("insert into users (u_id,username,passwd) values(" + num + ",'" + cl.encrypt(user) + "','" + cl.encrypt(passwd) + "')");
                            if (val < 1) {
                                if(Validate.debug)
                                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "WARNING-ERROR : " + stmt.getWarnings());
                            } else {
                                listModel.addElement(user);
                                if(Validate.debug)
                                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "user : " + user + " added successfully");
                            }
                            usernameText.setText("");
                            passwdText.setText("");
                            repasswdText.setText("");
                        } else {
                            passwdText.setText("");
                            repasswdText.setText("");
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "passwords are different, they must be same");
                        }

                    }

                    if (remuserRadio.isSelected()) {
                        int val = 0;
                        val = stmt.executeUpdate("delete from users where username = '" + cl.encrypt(user) + "'");
                        if (val < 1) {
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), " invalid username or " + stmt.getWarnings());
                        } else {
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "user : " + user + " removed successfully");
                        }
                        listModel.removeElement(user);
                        usernameText.setText("");
                        passwdText.setText("");

                    }

                    if (passwdRadio.isSelected()) {
                        String passwd = passwdText.getText();
                        String passwd2 = repasswdText.getText();

                        if (passwd.equals(passwd2)) {
                            int num = 0;
                            int val = stmt.executeUpdate("update users set passwd = '" + cl.encrypt(passwd) + "' where username = '" + cl.encrypt(user) + "'");
                            if (val < 1) {
                                if(Validate.debug)
                                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "invalid username or " + stmt.getWarnings());
                            } else {
                                if(Validate.debug)
                                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), user + " : password changed successfully");
                            }
                            usernameText.setText("");
                            passwdText.setText("");
                            repasswdText.setText("");
                        } else {
                            passwdText.setText("");
                            repasswdText.setText("");
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "passwords are different, they must be same");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                }
            }
        });

    }

    private void setLayout() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(passwordLabel).addComponent(usernameLabel).addComponent(reenterPasswordLabel).addComponent(adduserRadio)).addGap(9, 9, 9).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(39, 39, 39).addComponent(remuserRadio)).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(repasswdText, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE).addComponent(passwdText, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE).addComponent(usernameText, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE))))).addGroup(layout.createSequentialGroup().addGap(94, 94, 94).addComponent(proceedButton))).addGap(37, 37, 37)).addGroup(layout.createSequentialGroup().addGap(82, 82, 82).addComponent(passwdRadio).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(layout.createSequentialGroup().addGap(34, 34, 34).addComponent(userlistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(userlistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(27, 27, 27).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(usernameText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(usernameLabel)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(passwdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(passwordLabel)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(repasswdText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(reenterPasswordLabel)).addGap(26, 26, 26).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(remuserRadio).addComponent(adduserRadio)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(passwdRadio).addGap(36, 36, 36).addComponent(proceedButton))).addContainerGap()));

        int width = dPane.getWidth() / 2;
        int height = dPane.getHeight() / 2;
        this.reshape(nextFrameX, nextFrameY, width, height);

        this.show();

        // select the frame--might be vetoed
        try {
            this.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }

        /* if this is the first time, compute distance between
        cascaded frames
         */

        if (frameDistance == 0) {
            frameDistance = this.getHeight() - this.getContentPane().getHeight();
        }

// compute placement for next frame
        nextFrameX += frameDistance;
        nextFrameY +=
                frameDistance;
        if (nextFrameX + width > dPane.getWidth()) {
            nextFrameX = 0;
        }

        if (nextFrameY + height > dPane.getHeight()) {
            nextFrameY = 0;
        }

    }

    public int getFrameDistance() {
        return frameDistance;
    }

    private void initDatabase() {
        System.out.println("SQL Test");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }

        try {
            conn = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:CM", "scott", "tiger");
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }

        System.out.println("Connection established");
        if(Validate.debug)
        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Connection Established ..");
        try {
            stmt = conn.createStatement();
            System.out.println("Statement Created");
            if(Validate.debug)
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Statement Created..");

            ResultSet r = stmt.executeQuery("select username from users");
            while (r.next()) {
                listModel.addElement(cl.decrypt(r.getString("username")));
            }

        } catch (Exception e) {
            System.out.println(e);
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + e.getMessage());
        }
    }
}