
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.beans.PropertyVetoException;

/**
 * MenuBar of ServerAdmin GUI
 *
 * @author anonymous_02
 */
public class MenuBar extends JMenuBar {

    private App app;
    private JMenu fileMenu,  windowMenu;
    private JMenuItem quitMenuItem,  nextMenuItem,  cascadeMenuItem,/* tileMenuItem,*/  dragOutlineCheckBoxMenuItem;

    public MenuBar(App app) {
        super();
        this.app = app;
        initComponents();
    }

    private void initComponents() {
        fileMenu = new JMenu();
        windowMenu = new JMenu();

        quitMenuItem = new JMenuItem();
        nextMenuItem = new JMenuItem();
        cascadeMenuItem = new JMenuItem();
//        tileMenuItem = new JMenuItem();
        dragOutlineCheckBoxMenuItem = new JCheckBoxMenuItem();

        setText();
        setMnemonics();
        setAccelerator();
        addListeners();
        addToMenuBar();
    }

    private void setText() {
        fileMenu.setText("File");
        windowMenu.setText("Window");

        quitMenuItem.setText("Quit");
        nextMenuItem.setText("Next");
        cascadeMenuItem.setText("Cascade");
//        tileMenuItem.setText("Tile");
        dragOutlineCheckBoxMenuItem.setText("Drag Outline");
        dragOutlineCheckBoxMenuItem.setSelected(false);
    }

    private void setMnemonics() {
        fileMenu.setMnemonic('F');
        windowMenu.setMnemonic('W');

        quitMenuItem.setMnemonic('Q');
        nextMenuItem.setMnemonic('N');
        cascadeMenuItem.setMnemonic('C');
//        tileMenuItem.setMnemonic('T');
        dragOutlineCheckBoxMenuItem.setMnemonic('D');
    }

    private void setAccelerator() {
        quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        nextMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        cascadeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
//        tileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        dragOutlineCheckBoxMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    }

    private void addListeners() {
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });

        nextMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextMenuItemActionPerformed(evt);
            }
        });

        cascadeMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cascadeMenuItemActionPerformed(evt);
            }
        });

        /*        tileMenuItem.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
        tileMenuItemActionPerformed(evt);
        }
        });
         */
        dragOutlineCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dragOutlineCheckBoxMenuItemActionPerformed(evt);
            }
        });
    }

    private void addToMenuBar() {
        fileMenu.add(quitMenuItem);
        windowMenu.add(nextMenuItem);
        windowMenu.add(cascadeMenuItem);
//        windowMenu.add(tileMenuItem);
        windowMenu.add(dragOutlineCheckBoxMenuItem);

        add(fileMenu);
        add(windowMenu);
    }

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void nextMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        selectNextWindow();
    }

    private void cascadeMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        cascadeWindows();
    }

    private void tileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        tileWindows();
    }

    private void dragOutlineCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        app.getHome().desktop.setDragMode(dragOutlineCheckBoxMenuItem.isSelected()
                ? JDesktopPane.OUTLINE_DRAG_MODE
                : JDesktopPane.LIVE_DRAG_MODE);
    }

    /**
     * Selects / moves the control to the next window (InternalFrame on KesktopPane).
     */
    public void selectNextWindow() {
        JInternalFrame[] frames = app.getHome().desktop.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].isSelected()) {
                // find next frame that isn't an icon and can be
                // selected
                try {
                    int next = (i + 1) % frames.length;
                    while (next != i && frames[next].isIcon()) {
                        next = (next + 1) % frames.length;
                    }
                    if (next == i) {
                        return;
                    }
                    // all other frames are icons or veto selection
                    frames[next].setSelected(true);
                    frames[next].toFront();
                    return;
                } catch (PropertyVetoException e) {
                }
            }
        }
    }

    /**
     * cascades the all windows / InternalFrames currently present on DesktopPane.
     */
    public void cascadeWindows() {
        JInternalFrame[] frames = app.getHome().desktop.getAllFrames();
        int x = 0;
        int y = 0;
        int width = app.getHome().desktop.getWidth() / 2;
        int height = app.getHome().desktop.getHeight() / 2;

        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                try {
                    // try to make maximized frames resizable
                    // this might be vetoed
                    frames[i].setMaximum(false);
                    frames[i].reshape(x, y, width, height);

                    x += frames[i].getHeight() - frames[i].getContentPane().getHeight();
                    y += frames[i].getHeight() - frames[i].getContentPane().getHeight();
                    // wrap around at the desktop edge
                    if (x + width > app.getHome().desktop.getWidth()) {
                        x = 0;
                    }
                    if (y + height > app.getHome().desktop.getHeight()) {
                        y = 0;
                    }
                } catch (PropertyVetoException e) {
                }
            }
        }
    }

    /**
     * Tiles the all windows / InternalFrames currently present on DesktopPane.
     */
    public void tileWindows() {
        JInternalFrame[] frames = app.getHome().desktop.getAllFrames();

        // count frames that aren't iconized
        int frameCount = 0;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                frameCount++;
            }
        }

        int rows = (int) Math.sqrt(frameCount);
        int cols = frameCount / rows;
        int extra = frameCount % rows;
        // number of columns with an extra row

        int width = app.getHome().desktop.getWidth() / cols;
        int height = app.getHome().desktop.getHeight() / rows;
        int r = 0;
        int c = 0;
        for (int i = 0; i < frames.length; i++) {
            if (!frames[i].isIcon()) {
                try {
                    frames[i].setMaximum(false);
                    frames[i].reshape(c * width,
                            r * height, width, height);
                    r++;
                    if (r == rows) {
                        r = 0;
                        c++;
                        if (c == cols - extra) {
                            // start adding an extra row
                            rows++;
                            height = app.getHome().desktop.getHeight() / rows;
                        }
                    }
                } catch (PropertyVetoException e) {
                }
            }
        }
    }
}
