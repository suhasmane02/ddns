/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.tree.TreePath;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.net.URL;

/**
 * Button Handler for opening file in read and/or write mode.
 *
 * @author anonymous_02
 */
public class OpenButtonHandler
        implements ActionListener {

    private DesktopPane dPane;
    private JButton button;
    private TreePath path;
    private int fileId;

    /**
     *
     * @param dPane
     * @param button
     * @param path
     * @param fileId
     */
    public OpenButtonHandler(DesktopPane dPane, JButton button, TreePath path, int fileId) {
        this.dPane = dPane;
        this.button = button;
        this.fileId = fileId;
        this.path = path;
    }

    /**
     * Invoked when action is performed.
     *
     * @param ev ActionEvent.
     */
    public void actionPerformed(ActionEvent ev) {
        JoinThread j = new JoinThread();
        Thread t = new Thread(j);
        t.start();
        t.isAlive();
    }

    /**
     * Sets id of file to be read and/or written
     *
     * @param fileId an integer value.
     */
    public void setFileIdAndMode(int fileId) {
        this.fileId = fileId;
    }

    /**
     *
     */
    class JoinThread implements Runnable {

        /**
         *
         */
        public JoinThread() {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "thread created");
        }

        /**
         * runs the thread.
         */
        public void run() {

            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "in run ... accessible fields are : path = " + dPane.getPath());
            path = dPane.getPath();
            int j = path.getPathCount();
            int readMode = 0;
            String selectedFile = path.getPathComponent(j - 1).toString();
            String bMode = button.getActionCommand();
            boolean mode = bMode.contains("Write");

System.out.println("clicked on : "+bMode+" AND mode = "+mode);

            String temp = selectedFile;
            try {
                byte[] filedata = Validate.compute.getLocations(fileId, mode);
                File file = new File(temp + ".loc");
                BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file.getName()));
                output.write(filedata, 0, filedata.length);
                output.flush();
                output.close();

                getFile(file, mode);

            } catch (java.rmi.RemoteException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(new java.awt.Frame(), "Error rmi: " + path.toString() + " - " + ex.getMessage());
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(new java.awt.Frame(), "Error io: " + ex.getMessage());
            }
        }

        private void getFile(File locationFile, boolean mode) {
            InternalFrame iframe;
            Joiner glue;
            BufferedOutputStream output = null;
            String outname = locationFile.getName();
            String[] temp = outname.split("\\(id=");
            outname = temp[0].trim();
            File f = new File(outname);
            try {
JOptionPane.showMessageDialog(new java.awt.Frame(), f.getName());
                output = new BufferedOutputStream(new FileOutputStream(f.getName()));
                glue = new Joiner(locationFile.getName(), "temp");

                byte[] filedata = glue.go();
JOptionPane.showMessageDialog(new java.awt.Frame(), f.getName()+" got successfullyyyyy...");
                System.out.println("Reading: " + glue.getNextSourceName());
                System.out.println(" data read" + filedata.length);
                output.write(filedata, 0, filedata.length);
                output.flush();
                System.out.println(" written to file");

                output.close();
                JOptionPane.showMessageDialog(new java.awt.Frame(), f.getName() + "file downloaded..");
                String filename = f.getPath();

                URL fileUrl = new URL("file:" + filename);
                JOptionPane.showMessageDialog(new java.awt.Frame(), filename + "opening...");
                try {
                    Process ps = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + f.getCanonicalPath());
                    iframe = new InternalFrame(dPane, f, mode, glue, ps, fileId);
                    iframe.set(fileUrl);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                JOptionPane.showMessageDialog(new java.awt.Frame(), "can u see frame?...");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}