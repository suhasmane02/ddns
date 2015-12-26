
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Button Handler.
 * 
 * @author anonymous_02
 */
public class OpenButtonHandler
        implements ActionListener {

    private DesktopPane dPane;
    private JButton button1,  button2;
    private boolean buttonUser,  buttonNode,  buttonDB;

    /**
     *
     * @param dPane
     * @param button1
     */
    public OpenButtonHandler(DesktopPane dPane, JButton button1) {
        this(dPane, button1, null);
    }

    /**
     *
     * @param dPane
     * @param button1
     * @param button2
     */
    public OpenButtonHandler(DesktopPane dPane, JButton button1, JButton button2) {
        this.dPane = dPane;
        this.button1 = button1;
        this.button2 = button2;
    }

    /**
     * Invoked when action is performed.
     *
     * @param ev ActionEvent.
     */
    public void actionPerformed(ActionEvent ev) {
        if (button1.getText().equalsIgnoreCase("Add or Remove User")) {
            buttonUser = true;
//            button1.setEnabled(false);
        } else if (button1.getText().equalsIgnoreCase("Add or Remove Node")) {
            buttonNode = true;
//            button1.setEnabled(false);
//            button2.setEnabled(false);
        } else if (button1.getText().equalsIgnoreCase("Generate Database of FileSystem")) {
            buttonDB = true;
//            button1.setEnabled(false);
//            button2.setEnabled(false);
        }
        JoinThread j = new JoinThread();
        Thread t = new Thread(j);
        t.start();
    }

    /**
     *
     */
    class JoinThread implements Runnable {

        /**
         *
         */
        public JoinThread() {
            if(Validate.debug)
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "thread created");
        }

        /**
         * runs the thread.
         */
        public void run() {
            if(Validate.debug)
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "opening window " + button1.getText());

            try {
                if (buttonUser) {
                    new UserSpecificFrame(dPane);
                } else if (buttonNode) {
                    new NodeSpecificFrame(dPane);
//                        button2.setEnabled(true);
                } else if (button1.getText().equalsIgnoreCase("Generate Database of FileSystem")) {
                    new GenerateDB(dPane);
//                        button2.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(new java.awt.Frame(), "Error: " + ex.getMessage());
            }
        }
    }
}