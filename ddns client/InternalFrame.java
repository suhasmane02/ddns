/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;

import java.net.URL;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.beans.VetoableChangeListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;

/**
 * A class that generates InternalFrame which contains EditorPane that displays contents of opened file.
 * An InternalFrame is generated dynamically.
 *
 * @author anonymous_02
 */
public class InternalFrame extends JInternalFrame {

    private MyEditorPane editorPane;
    private JButton prevButton,  saveButton,  nextButton;
    private JDesktopPane dPane;
    private boolean mode;
    private JScrollPane scrollPane;
    private GroupLayout iFrameGroupLayout;
    private Joiner glue;
    private File file;
    private Process ps;
    private int fileId;
    public int nextFrameX;
    public int nextFrameY;
    public int frameDistance;

    /**
     * constructor.
     *
     * @param dPane DesktopPane on which InternalFrame is to be displayed.
     * @param file name of the file to be initialized by an EditorPane.
     * @param mode Read or Write mode. (read : false, write : true)
     * @param glue Joiner instance that is used for getting requested part of file.
     * @param ps Process indicating the application opened by System for requested file.
     */
    public InternalFrame(JDesktopPane dPane, File file, boolean mode, Joiner glue, Process ps, int fileId) {
        super(file.getName(),
                true, // resizable
                true, // closable
                true, // maximizable
                true);  // iconifiable
        this.dPane = dPane;
        this.file = file;
        this.mode = mode;
        this.glue = glue;
        this.ps = ps;
        this.fileId = fileId;
        initComponents();
    }

    private void initComponents() {
        editorPane = new MyEditorPane();
        scrollPane = new JScrollPane();
        if (mode) {
            editorPane.setEditable(mode);
        } else {
            editorPane.setEditable(mode);
        }
        prevButton = new JButton("Previous");
        saveButton = new JButton("Save current Document");
        nextButton = new JButton("Next");
        scrollPane.setViewportView(editorPane);
        if (mode) {
            saveButton.setEnabled(mode);
        } else {
            saveButton.setEnabled(mode);
        }
        addListeners();
        setLayout();
        setBounds(270, 30, 360, 370);
        editorPane.setVisible(true);
        setVisible(true);
        dPane.add(this, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    private void addListeners() {
        nextButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    if (!glue.isLastFile()) {
                        File f = new File(file.getName());
                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f.getName(), true));
                        byte[] filedata = glue.go();
                        System.out.println("Reading complete: " + glue.getNextSourceName());
                        System.out.println(" data read" + filedata.length);
                        output.write(filedata, 0, filedata.length);
                        output.flush();
                        System.out.println(" written to file");
                        output.close();
                        URL fileUrl = new URL("file:" + f.getPath());
                        set(fileUrl);
                        try {
                            ps = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + f.getCanonicalPath());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "reached the end....");
                    }
                } catch (Exception ex) {
                    editorPane.setText(ex.getMessage());
                }
            }
        });

        prevButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {

                    if (!glue.isFirstFile()) {
                        File f = new File(file.getName());
                        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(f.getName()));
                        byte[] filedata = glue.back();
                        System.out.println("Reading complete: " + glue.getNextSourceName());
                        System.out.println(" data read" + filedata.length);
                        output.write(filedata, 0, filedata.length);
                        output.flush();
                        System.out.println(" written to file");
                        output.close();
                        URL fileUrl = new URL("file:" + f.getPath());
                        set(fileUrl);
                        try {
                            ps = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + f.getCanonicalPath());
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "reached the beginning....");
                    }
                } catch (Exception ex) {
                    editorPane.setText(ex.getMessage());
                }
            }
        });

        saveButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
            }
        });

        addVetoableChangeListener(new VetoableChangeListener() {

            public void vetoableChange(PropertyChangeEvent event)
                    throws PropertyVetoException {
                String name = event.getPropertyName();
                Object value = event.getNewValue();

                // we only want to check attempts to close a frame
                if (name.equals("closed") && value.equals(Boolean.TRUE)) {
                    // ask user if it is ok to close
                    int result = JOptionPane.showInternalConfirmDialog(
                            dPane.getSelectedFrame(), "OK to close?");

                    // if the user doesn't agree, veto the close
                    if (result != JOptionPane.YES_OPTION) {
                        throw new PropertyVetoException(
                                "User canceled close", event);
                    } else if (result == JOptionPane.YES_OPTION) {
                        try {
                            boolean flag = Validate.compute.closeFile(fileId);
                            if (Validate.debug) {
                                if (flag) {
                                    javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "File closed successfully");
                                } else {
                                    javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error in closing the file");
                                }
                            }
                        } catch (java.rmi.RemoteException rex) {
                            rex.printStackTrace();
                        }
//                        ps.destroy();
                        System.runFinalization();
                        if(Validate.debug)
                        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "destroyed...");
                    }
                }
            }
        });
    }

    private void setLayout() {
        iFrameGroupLayout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(iFrameGroupLayout);
        iFrameGroupLayout.setHorizontalGroup(
                iFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(iFrameGroupLayout.createSequentialGroup().addGroup(iFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(iFrameGroupLayout.createSequentialGroup().addGap(36, 36, 36).addComponent(prevButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(nextButton)).addGroup(iFrameGroupLayout.createSequentialGroup().addContainerGap().addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))).addContainerGap()));
        iFrameGroupLayout.setVerticalGroup(
                iFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(iFrameGroupLayout.createSequentialGroup().addContainerGap().addGroup(iFrameGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(prevButton).addComponent(saveButton).addComponent(nextButton)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE).addContainerGap()));

        int width = dPane.getWidth() / 2;
        int height = dPane.getHeight() / 2;
        this.reshape(nextFrameX, nextFrameY, width, height);

        this.show();

        // select the frame--might be vetoed
        try {
            this.setSelected(true);
        } catch (PropertyVetoException e) {
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

    /**
     * Gets the frame distance.
     *
     * @return integer that indicates framedistance.
     */
    public int getFrameDistance() {
        return frameDistance;
    }

    /**
     * Sets the EditorPane with the contents specified by URL of file.
     *
     * @param u URL of file to be displayed in EditorPane.
     */
    public void set(URL u) {
        try {
            editorPane.setPage(u);
        } catch (java.io.IOException e) {
            editorPane.setText("Exception: " + e);
        }
    }
}
