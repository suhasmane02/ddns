/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;

import java.io.IOException;

/**
 * This class initializes DesktopPane with InternalFrame with file system hierarchy.
 *
 * @author anonymous_02
 */
public class DesktopPane extends JDesktopPane {

    private App app;
    public static JInternalFrame fileSysInternalFrame;
    private JTree fileSysHierarchyTree;
    private JScrollPane scrollPane;
    private JButton readopenButton;
    private JButton writeopenButton;
    private String selectedFile;
    private TreePath path;
    private GroupLayout fileSysInternalFrameGroupLayout;
    private int fileId = 0;
    boolean mode;

    /**
     * 
     * @param app
     */
    public DesktopPane(App app) {
        super();
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        fileSysInternalFrame = new JInternalFrame();
        scrollPane = new JScrollPane();
        readopenButton = new JButton();
        writeopenButton = new JButton();

        try {
            fileSysHierarchyTree = new XMLTreee(app.getHome().getXMLFileName());
            if (Validate.debug) {
                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), app.getHome().getXMLFileName());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        setPropertiesAndListeners();
    }

    private void setPropertiesAndListeners() {
        fileSysInternalFrame.setIconifiable(true);
        fileSysInternalFrame.setMaximizable(true);
        fileSysInternalFrame.setResizable(true);
        fileSysInternalFrame.setTitle("File System");
        fileSysInternalFrame.setAutoscrolls(true);
        fileSysInternalFrame.setFocusCycleRoot(false);
        fileSysInternalFrame.setOpaque(false);

        try {
            fileSysInternalFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        fileSysHierarchyTree.setAutoscrolls(true);
        fileSysHierarchyTree.setDoubleBuffered(true);
        fileSysHierarchyTree.setDragEnabled(true);
        fileSysHierarchyTree.setFocusCycleRoot(true);
        fileSysHierarchyTree.setFocusTraversalPolicyProvider(true);
        fileSysHierarchyTree.setInheritsPopupMenu(true);
        fileSysHierarchyTree.setLargeModel(true);
        fileSysHierarchyTree.setShowsRootHandles(true);

        readopenButton.setText("Open in Read mode");
        writeopenButton.setText("Open in Write mode");

        fileSysInternalFrame.setBounds(0, 0, 290, 330);

        scrollPane.setViewportView(fileSysHierarchyTree);
        addListeners();
        setLayoutAndAddtoDesktop();

        setVisible(true);
        fileSysInternalFrame.setVisible(true);
        fileSysHierarchyTree.setVisible(true);
    }

    private void addListeners() {
        mode = false;
        final OpenButtonHandler obhRead = new OpenButtonHandler(this, readopenButton, path, fileId);
        readopenButton.addActionListener(obhRead);

        mode = true;
        final OpenButtonHandler obhWrite = new OpenButtonHandler(this, writeopenButton, path, fileId);
        writeopenButton.addActionListener(obhWrite);

        fileSysHierarchyTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent event) {
                selectedFile = "";
                String selectedFilePath = "";
                path = fileSysHierarchyTree.getSelectionPath();
                if (path == null) {
                    return;
                }

                int j = path.getPathCount();

                selectedFile = path.getPathComponent(j - 1).toString();

                for (int i = 0; i < j; i++) {
                    selectedFilePath += path.getPathComponent(i).toString();

                    if ((j - i) != 1) {
                        selectedFilePath += "//";
                    }
                }
                System.out.println("path is : " + selectedFilePath + " level is : " + j);
                String str = path.getLastPathComponent().toString();
                String[] temp = str.split(",");
                String[] id = temp[0].split("id=");
                id[1] = id[1].trim();
                fileId = Integer.parseInt(id[1]);
                System.out.println("path is : " + fileId + " level is : " + j);
                obhRead.setFileIdAndMode(fileId);
                obhWrite.setFileIdAndMode(fileId);
            }
        });
    }

    private void setLayoutAndAddtoDesktop() {
        fileSysInternalFrameGroupLayout = new javax.swing.GroupLayout(fileSysInternalFrame.getContentPane());
        fileSysInternalFrame.getContentPane().setLayout(fileSysInternalFrameGroupLayout);
        fileSysInternalFrameGroupLayout.setHorizontalGroup(
                fileSysInternalFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE).addGroup(fileSysInternalFrameGroupLayout.createSequentialGroup().addContainerGap().addComponent(readopenButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(writeopenButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        fileSysInternalFrameGroupLayout.setVerticalGroup(
                fileSysInternalFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fileSysInternalFrameGroupLayout.createSequentialGroup().addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE).addGap(11, 11, 11).addGroup(fileSysInternalFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(readopenButton).addComponent(writeopenButton)).addContainerGap()));

        fileSysInternalFrame.setBounds(0, 0, 290, 330);

        add(fileSysInternalFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    /**
     * Gets the path of selected file
     *
     * @return TreePath.
     */
    public TreePath getPath() {
        return path;
    }
}
