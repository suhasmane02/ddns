
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.*;
import java.rmi.*;
import java.io.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import com.sun.org.apache.xml.internal.serialize.*;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;

/**
 * <p>Traverses the directories in selected Drive and distributes the files in
 * it on various nodes. Every single file is splitted in number of splits
 * considering every split of 1.38 MB equal to FLOPPY_SIZE(1.44MB).</p>
 * <p>To know number of splits per node of a sngle file to be stored are as follows :</p>
 * <p>consider an example of partnership : 'n' parteners with their own
 * investment of each started a business. They together have a profit of Rs.10000.
 * Now Rs.10000 is divided among 'n' partners according to their share in investment.</p>
 * <p>Similarly : we have 'n' nodes with their dedicated drive's available
 * free space as their investment,and Size of file as profit as stated in
 * above example, if we have 'N' number of predicted splits of a single file.
 * Then number of splits for that file will be stored on 'n' nodes,
 * according to their free space which we have considered as share.</p>
 *
 * Nodes are choosen from database maintained at ddnsServer machine.
 * As the directories are traversed same directories are created at ddnsNodes
 * and stores the files' splits as stated in above partnership example.
 * Generates the File System Hierarchy in xml format.
 *
 * @author anonymous_02
 */
public class GenerateDB extends javax.swing.JInternalFrame {

    private javax.swing.JDesktopPane dPane;
    private javax.swing.JLabel choosedriveLabel;
    private javax.swing.JComboBox driveCombo;
    private javax.swing.JButton genDBButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel mainLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JList statusList;
    private javax.swing.DefaultListModel listModel;
    public int nextFrameX;
    public int nextFrameY;
    public int frameDistance;
    String drive;
    private java.sql.Connection conn = null;
    private java.sql.Statement stmt;
    private java.sql.ResultSet rs;

    /**
     *
     * @param dPane
     */
    public GenerateDB(javax.swing.JDesktopPane dPane) {

        this.dPane = dPane;
        initComponents();
    }

    private void initComponents() {
        initDatabase();
        driveCombo = new javax.swing.JComboBox();
        choosedriveLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        statusList = new javax.swing.JList();
        genDBButton = new javax.swing.JButton();
        mainLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        listModel = new javax.swing.DefaultListModel();
        statusList = new javax.swing.JList(listModel);

        setClosable(true);
        setIconifiable(true);
        setTitle("Database Generation");

        jScrollPane1.setViewportView(statusList);
        genDBButton.setText("Store File System");

        choosedriveLabel.setText("Choose Drive");

        mainLabel.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        mainLabel.setText("File System Store Wizard");

        File[] roots = File.listRoots();
        driveCombo.addItem("--Select Drive--");
        try {
            for (int i = 0; i < roots.length; i++) {
                driveCombo.addItem(roots[i].getCanonicalPath());
            }
        } catch (java.io.IOException ex) {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error: " + ex.getMessage());
            ex.printStackTrace();
        }

        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        addListeners();
        setLayout();

        setVisible(true);
        dPane.add(this, javax.swing.JLayeredPane.DEFAULT_LAYER);
    }

    private void addListeners() {
        driveCombo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                drive = (String) driveCombo.getSelectedItem();
                statusLabel.setText(drive + " drive selected");
            }
        });

        genDBButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                int num = 0;
                try {
                    ResultSet rs = stmt.executeQuery("select max(f_id) as start_num from file_sys");
                    if (rs.next()) {
                        num = rs.getInt("start_num");
                        num++;
                    }

                    String temp = drive;
                    temp += "\\";
                    File mainFile = new File(temp);
                    statusLabel.setText("storing files of " + temp + " drive");
                    listModel.clear();
                    new Generate(mainFile, num, listModel, statusLabel);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error caught at genDB.addActionListener() method : " + ex.getMessage());
                }
            }
        });
    }

    private void setLayout() {
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addGap(66, 66, 66).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(driveCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(115, 115, 115).addComponent(genDBButton)).addComponent(choosedriveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(164, Short.MAX_VALUE).addComponent(mainLabel).addGap(152, 152, 152)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(25, 25, 25).addComponent(mainLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE).addComponent(choosedriveLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(driveCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(genDBButton)).addGap(18, 18, 18).addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(11, 11, 11).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));

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
        if (Validate.debug) {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Connection Established ..");
        }
        try {
            stmt = conn.createStatement();
            System.out.println("Statement Created");
            if (Validate.debug) {
                javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Statement Created ..");
            }
        } catch (Exception e) {
            System.out.println(e);
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + e.getMessage());
            System.exit(0);
        }
    }

    class Generate extends Thread {

        private java.io.File file;
        private java.io.FileWriter fis,  fis2;
        private File Filedir;
        private String name;
        private javax.swing.DefaultListModel listmodel;
        private javax.swing.JLabel lblpath;
        private int tempCount;
        private String data;
        private int counter;
        private DocumentBuilder db;
//        private Element elt,  elt1;
        private DocumentBuilderFactory dbf;
        private Document dom;
        private ArrayList<String> nodeList;
        private ArrayList<Long> freeSpace;
        private ArrayList<Long> filesPerNode;
        private Splitter axe;
        private FileServer fserver;
        private long sizeInB;
        final static public int FLOPPY_SIZE = 1457664;

        /**
         *
         * @param d
         * @param counter
         * @param dm
         * @param l
         * @throws java.io.IOException
         */
        public Generate(File d, int counter, javax.swing.DefaultListModel dm, javax.swing.JLabel l) throws java.io.IOException {
            file = new File("myfile.txt");
            fis = new java.io.FileWriter(file);
            fis2 = new java.io.FileWriter(new File("data.txt"));

            Filedir = d;
//            Filedir = new File("U:\\ddnsproject");
            this.counter = counter;
            this.tempCount = counter;
            listmodel = dm;
            lblpath = l;
            nodeList = new ArrayList<String>();
            freeSpace = new ArrayList<Long>();
            filesPerNode = new ArrayList<Long>();
            axe = new Splitter();

            this.start();
        }

        /**
         * runs the thread.
         */
        public void run() {
            long size = 0;
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                dom = db.newDocument();
                System.out.println("dom document created successfully");
                /*
                rootEle = dom.createElement("Drive");
                rootEle.setAttribute("name", Filedir.getCanonicalPath());
                dom.appendChild(rootEle);
                 */
                fis.write(Filedir.getAbsolutePath());
                fis.write("\n");
                fis.flush();
                listmodel.addElement(Filedir.getAbsolutePath());
                fis2.write("BEFORE very first call to search..\n\n");
                fis2.flush();

                ResultSet r = stmt.executeQuery("select * from node");

                while (r.next()) {
                    nodeList.add(r.getString("node_ip"));
                    size = r.getLong("free_space");
                    System.out.println("size for node : " + size);
                    if (freeSpace == null) {
                        System.out.println("freeSpace is null");
                    } else {
                        System.out.println("freeSpace is OK");
                    }
                    freeSpace.add(size);
                    sizeInB += size;
                }

                /*                String remoteName = "rmi://" + nodeList.get(0) + ":1099/nodeService";
                fserver = (FileServer) Naming.lookup(remoteName);
                System.out.println(" connected ............");
                 */
                Connect(nodeList.get(0));
                search(counter, null, Filedir, listmodel, lblpath);

                fis2.write("\nSEARCH IS FINISHED");
                fis2.flush();
                //print
                OutputFormat format = new OutputFormat(dom);
                format.setIndenting(true);
                //to generate output to console use this serializer
                //XMLSerializer serializer = new XMLSerializer(System.out, format);
                //to generate a file output use fileoutputstream instead of system.out
                XMLSerializer serializer = new XMLSerializer(
                        new FileOutputStream(new File("test.xml")), format);
                serializer.serialize(dom);

//		int i = stmt.executeUpdate("delete from file_sys");
//		System.out.println("\nNUMber of rows deleted : "+i);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private void ComputeSpace(long len) {
            int nodeNum = freeSpace.size();
            int i = 0;
            long spaceOnNode = 0, files = 0;
            long numberOfSplits = len / FLOPPY_SIZE;

            if (len % FLOPPY_SIZE != 0) {
                numberOfSplits = numberOfSplits + 1;
            }

            while (i < nodeNum) {
                spaceOnNode = freeSpace.get(i);
                files = numberOfSplits * spaceOnNode / sizeInB;
                filesPerNode.add(files);

                System.out.println("Files per node :");
                System.out.println("Node name :  " + nodeList.get(i) + "  :  " + files);

                i++;
            }
        }

        private String Connect(String nodeIP) {
            String remoteName = "rmi://" + nodeIP + ":1099/nodeService";
            try {
                fserver = (FileServer) Naming.lookup(remoteName);
                System.out.println(" connected ............");
            } catch (java.rmi.NotBoundException ex) {
                ex.printStackTrace();
                return ex.getMessage();
            } catch (java.net.MalformedURLException ex) {
                ex.printStackTrace();
                return ex.getMessage();
            } catch (java.rmi.RemoteException ex) {
                ex.printStackTrace();
                return ex.getMessage();
            }
            return "connected successfully";
        }

        private void search(int counter, Element elm, File dir, javax.swing.DefaultListModel model, javax.swing.JLabel lb) {
            int tempInt = 0;
            try {

                if (elm != null) {
                    fis2.write("In search(" + counter + "," + elm.getAttribute("name") + "," + dir.getName() + ")\n");
                } else {
                    fis2.write("In search(" + counter + "," + "null" + "," + dir.getName() + ")\n");
                }

                String st1 = dir.getName().toLowerCase();
                String tempStr = "";
                lb.setText(dir.getAbsolutePath());

                if (!dir.isDirectory()) {
                    tempStr = "unknown";
                    st1 = st1.replace(".", ",");
                    String sttext[] = st1.split(",");
                    if (sttext.length > 1) {
                        st1 = sttext[1];
                    } else {
                        st1 = "null";
                    }

                    if (st1.equals("doc") || st1.equals("txt") || st1.equals("ppt") || st1.equals("exe") || st1.equals("as") || st1.equals("mdb") || st1.equals("xls") || st1.equals("zip")) {
                        tempStr = "document";
                    }
                    if (st1.equals("gif") || st1.equals("png") || st1.equals("bmp") || st1.equals("jpeg") || st1.equals("jpg") || st1.equals("wbmp")) {
                        tempStr = "image";
                    }
                    if (st1.equals("wav") || st1.equals("dat") || st1.equals("avi") || st1.equals("midi") || st1.equals("mp3") || st1.equals("rmvb") || st1.equals("vob") || st1.equals("3gp") || st1.equals("wmv")) {
                        tempStr = "sound";
                    } else if (st1.equals("null")) {
                        tempStr = "unknown";
                    }

                    java.util.Date d = new java.util.Date(dir.lastModified());
                    java.util.Formatter fmt = new java.util.Formatter();
                    fmt.format("%td-%tb-%tY", d, d, d);

                    tempInt = 0;
                    int temp = 0;
                    ComputeSpace(dir.length());
                    System.out.println("Inside else : connecting to : " + nodeList.get(temp));
                    Connect(nodeList.get(temp));
                    System.out.println("connected  to node = " + nodeList.get(temp));
                    axe.SetAndPrepare(dir.getCanonicalPath(), fserver, nodeList.get(tempInt));
//                    axe.mkDir(fserver);


                    while (axe.moreToCopy()) {
                        System.out.println("Writing: " + axe.getNextTargetName());

                        if (filesPerNode.get(temp) >= ++tempInt) {
                            System.out.println(temp + " : " + filesPerNode.get(temp) + " : " + tempInt + "\n");
                        } else {
                            tempInt = 0;
                            temp++;
//                            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(),Connect(nodeList.get(temp)));
                            System.out.println("Inside else : connecting to : " + nodeList.get(temp));
                            Connect(nodeList.get(temp));
                            System.out.println("connected  to node = " + nodeList.get(temp));
                            axe.mkDir(fserver);
                        }
                        axe.go(fserver, nodeList.get(temp));
                    }

                    File locFile = axe.getLocFile();

                    filesPerNode.clear();

                    stmt.executeUpdate("insert into file_sys values (" + counter + ",'" + dir.getName() + "','" + tempStr + "'," + (int) dir.length() + ",'" + fmt + "','" + locFile.getCanonicalPath() + "','free',child_set(),0)");
                    Element elt = dom.createElement("File");
                    elt.setAttribute("name", dir.getName());
                    elt.setAttribute("id", counter + "");
                    elm.appendChild(elt);

//                    data = counter + "--" + dir.getName() + "--" + tempStr + "--NO_CHILD--";
                    fis2.write("\nFILE.....\n");
                    data = elm.getAttribute("name") + "--" + elt.getAttribute("name");
                    fis2.flush();
                    fis2.write(data);
                    fis2.write("\n\n");
                    fis2.flush();

                    model.addElement(data);
                } else if (dir.isDirectory()) {
                    String[] children = dir.list();
                    int[] set = new int[children.length];
                    String childSet = "child_set (";
                    int j = 0;
                    for (j = 0; j < children.length - 1; j++) {
                        System.out.println(new File(dir, children[j]).getName() + " -> " + tempCount);
                        childSet += ++tempCount + ",";
                        set[j] = tempCount;

                        fis.write(new File(dir, children[j]).getAbsolutePath());
                        fis.write("\n");
                        fis.flush();
                    }

                    fis.write(new File(dir, children[j]).getAbsolutePath());
                    fis.write("\n");
                    fis.flush();

                    System.out.println(new File(dir, children[j]).getName() + " -> " + tempCount);
                    childSet += ++tempCount + ")";
                    set[j] = tempCount;

                    java.util.Date d = new java.util.Date(dir.lastModified());
                    java.util.Formatter fmt = new java.util.Formatter();
                    fmt.format("%td-%tb-%tY", d, d, d);

                    String queryString;
                    if (dir.getName() != null) {
                        queryString = "insert into file_sys values (" + counter + ",'" + dir.getName() + "','folder'," + (int) dir.length() + ",'" + fmt + "','" + dir.getCanonicalPath() + "','free'," + childSet + ",0)";
                        System.out.println("NOT_NULLl - >" + queryString);
                    } else {
                        queryString = "insert into file_sys values (" + counter + ",'" + dir + "','folder'," + (int) dir.length() + ",'" + fmt + "','" + dir.getCanonicalPath() + "','free'," + childSet + ",0)";
                        System.out.println("NULL - >" + queryString);
                    }

                    stmt.executeUpdate(queryString);

                    Element elt1;

                    if (counter == 1) {
                        elt1 = dom.createElement("Drive");
                        elt1.setAttribute("name", dir.getName());
                        dom.appendChild(elt1);
                        System.out.println("\n..Drive created..");
                    } else {
                        elt1 = dom.createElement("Folder");
                        elt1.setAttribute("name", dir.getName());
                    }

                    elt1.setAttribute("id", counter + "");

                    System.out.println("child");
                    System.out.println(".." + elt1.getAttribute("name") + "..\n");
                    System.out.println("parent");

                    data = counter + "--" + elt1.getAttribute("name") + "--FOLDER--" + childSet;
                    fis2.flush();
                    fis2.write(data);
                    fis2.write("\n\n");
                    fis2.flush();

                    model.addElement(data);

                    for (int i = 0; i < children.length; i++) {
                        System.out.println("b4 calll " + i + " elt1 is : " + elt1.getAttribute("name"));
                        search(set[i], elt1, new File(dir, children[i]), model, lb);
                        System.out.println("after calll " + i + " elt1 is : " + elt1.getAttribute("name"));
                        fis2.write("\n.....\n");
                        if (new File(dir, children[i]).isDirectory() && elm != null) {
                            elm.appendChild(elt1);
                            fis2.write("\nFOLDER.....\n");
                            data = elm.getAttribute("name") + "--" + elt1.getAttribute("name");
                            fis2.flush();
                            fis2.write(data);
                            fis2.write("\n\n");
                            fis2.flush();
                        }
                    }

                    fis2.write("\nout of for loop ->  " + elt1.getAttribute("name"));
                    if (elm != null) {
                        fis2.write("\n                ->  " + elm.getAttribute("name"));
                        elm.appendChild(elt1);
                    } else {
                        fis2.write("\n                ->  null");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}