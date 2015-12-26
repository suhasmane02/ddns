
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.ItemEvent;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import java.rmi.*;
import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * Handles Node Administration.
 *
 * @author anonymous_02
 */
public class NodeSpecificFrame extends JInternalFrame {

    private JDesktopPane dPane;
    private javax.swing.JRadioButton addnodeRadio;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nodeLabel;
    private javax.swing.JList nodeList;
    private javax.swing.JTextField nodeText;
    private javax.swing.JLabel nodelistLabel;
    private javax.swing.JButton proceedButton;
    private javax.swing.JRadioButton remnodeRadio;
    private javax.swing.ButtonGroup nodeButtonGroup;
    private javax.swing.DefaultListModel listModel;
    public int nextFrameX;
    public int nextFrameY;
    public int frameDistance;
    java.sql.Connection conn = null;
    java.sql.Statement stmt;
    java.sql.ResultSet rs;

    /**
     * initializes the Node Administrationr frame (InternalFrame on DesktopPane).
     *
     * @param dPane
     */
    public NodeSpecificFrame(JDesktopPane dPane) {
        this.dPane = dPane;
        initComponents();
    }

    private void initComponents() {

        nodeText = new javax.swing.JTextField();
        nodeLabel = new javax.swing.JLabel();
        nodelistLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        nodeList = new javax.swing.JList();
        addnodeRadio = new javax.swing.JRadioButton();
        remnodeRadio = new javax.swing.JRadioButton();
        proceedButton = new javax.swing.JButton();
        nodeButtonGroup = new javax.swing.ButtonGroup();
        listModel = new javax.swing.DefaultListModel();
        nodeList = new javax.swing.JList(listModel);

        nodeButtonGroup.add(addnodeRadio);
        nodeButtonGroup.add(remnodeRadio);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(false);
        setResizable(false);
        setTitle("Node Specific");

        nodeText.setText("192.168.24.253");
        nodeLabel.setText("Node IP");
        nodelistLabel.setText("Available Node List");

        jScrollPane1.setViewportView(nodeList);
        addnodeRadio.setText("Add Node");
        remnodeRadio.setText("Remove Node");
        proceedButton.setText("Proceed");

        initDatabase();

        addListeners();
        setLayout();

        setVisible(true);
        dPane.add(this, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    private void addListeners() {

        nodeList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                String temp = (String) nodeList.getSelectedValue();
                if (temp != null) {
                    String[] temp1 = temp.split(":");
                    nodeText.setText(temp1[0].trim());
                }
            }
        });

        addnodeRadio.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (addnodeRadio.isSelected()) {
                }
            }
        });

        remnodeRadio.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (remnodeRadio.isSelected()) {
                }
            }
        });

        proceedButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int num = 0;
                String nodeIP = nodeText.getText();
                if (addnodeRadio.isSelected()) {
                    String remoteName = "rmi://" + nodeIP + ":1099/nodeService";
                    try {
                        NodeAdmin nodeadmin = (NodeAdmin) Naming.lookup(remoteName);
                        System.out.println(" connected ............");
                        long freeSpace = nodeadmin.getFreeSpace();
                        System.out.println("freeSpace on " + nodeIP + " node is " + freeSpace);
                        if(Validate.debug)
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "FreeSpace on " + nodeIP + " : " + freeSpace);
                        ResultSet r = stmt.executeQuery("select max(nid) as start_num from node");
                        if (r.next()) {
                            num = r.getInt("start_num");
                            num++;
                        }
                        String queryString = "insert into node values (" + num + ",'" + nodeIP + "','" + nodeIP + "'," + freeSpace + ",'y')";
                        int val = stmt.executeUpdate(queryString);

                        if (val < 1) {
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "WARNING-ERROR : " + stmt.getWarnings());
                        } else {
                            listModel.addElement(nodeIP + " : " + freeSpace);
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Node : " + nodeIP + " added successfully");
                        }
                        nodeText.setText("");

                    } catch (NotBoundException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    }
                }

                if (remnodeRadio.isSelected()) {
                    try {
                        ResultSet r = stmt.executeQuery("select free_space from node where node_ip ='" + nodeIP + "'");
                        long freeSpace = 0;
                        while (r.next()) {
                            freeSpace = r.getLong("free_space");
                        }
                        int val = stmt.executeUpdate("delete from node where node_ip = '" + nodeIP + "'");
                        if (val < 1) {
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "WARNING-ERROR : " + stmt.getWarnings());
                        } else {
                            if(Validate.debug)
                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Node : " + nodeIP + " removed successfully");
                        }
                        listModel.removeElement(nodeIP + " : " + freeSpace);
                        nodeText.setText("");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + ex.getMessage());
                    }
                }
            }
        });
    }

    private void setLayout() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(74, 74, 74).addComponent(proceedButton)).addGroup(layout.createSequentialGroup().addGap(31, 31, 31).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(nodeLabel).addGap(33, 33, 33).addComponent(nodeText, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(addnodeRadio).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(remnodeRadio))))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 226, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(311, Short.MAX_VALUE).addComponent(nodelistLabel).addGap(70, 70, 70)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(nodelistLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(65, 65, 65).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(nodeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(nodeLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(addnodeRadio).addComponent(remnodeRadio)).addGap(52, 52, 52).addComponent(proceedButton).addGap(103, 103, 103)));

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
        nextFrameY += frameDistance;
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
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Statement Created.." + stmt);

            ResultSet r = stmt.executeQuery("select node_ip,free_space from node");

            while (r.next()) {
                listModel.addElement(r.getString("node_ip") + " : " + r.getLong("free_space"));
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + e.getMessage());
            e.printStackTrace();
        }
    }
}