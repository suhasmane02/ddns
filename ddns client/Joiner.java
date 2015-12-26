
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.rmi.Naming;

/**
 * This class gets the next part of file on demand.
 *
 * @author anonymous_02
 */
public class Joiner {

    private File file;
    private File todir;
    private File fromdir;
    private String[] IP;
    private String[] fileName;
    private BufferedOutputStream bos;
    private int seq;
    private FileServer fserver;

    /**
     * constructor.
     *
     * @param fromFile name of file containing locations of aplitted parts of file.
     * @param toDir 
     * @throws java.io.IOException
     */
    public Joiner(String fromFile, String toDir) throws IOException {
        this.file = new File(fromFile);
        this.todir = new File(toDir);
        fromdir = file.getParentFile();
        prepare();
    }

    /**
     * Checks availability of more splits.
     *
     * @return boolean - true if more number of splits are available, otherwise false.
     */
    public boolean moreToJoin() {
        if (seq >= fileName.length) {
            return false;
        }
        return true;
    }

    /**
     * Gets the name of next split.
     *
     * @return String - name of file.
     */
    public String getNextSourceName() {
        return fileName[seq];
    }

    /**
     * Gets file sequence number.
     *
     * @return integer that is current sequence number.
     */
    public int getSeq() {
        return seq;
    }

    /**
     * Checks the end of splits.
     *
     * @return boolean - true if it is the last split, otherwise false.
     */
    public boolean isLastFile() {
        if (seq == IP.length - 1) {
            return true;
        }
        return false;
    }

    /**
     * Checks the beginning of split.
     *
     * @return boolean - true if it is the first split, otherwise false.
     */
    public boolean isFirstFile() {
        if (seq == 0) {
            return true;
        }
        return false;
    }

    /**
     * Goes to the ddnsNode and gets the contents of next split.
     *
     * @return byte array - contents of the file requested.
     * @throws java.rmi.RemoteException - if this function fails to get requested split, for any reason at remote machine (ddnsNode).
     */
    public byte[] go() throws java.rmi.RemoteException {
        String temp = IP[++seq];

        if (seq > 0 && !temp.equals(IP[seq - 1])) {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), Connect(IP[seq]));
        }

        byte[] buf = fserver.readFile(fileName[seq]);
        System.out.println("file read  " + buf.length + "  bytes");
        System.out.println("in go function n returnings : " + fileName[seq]);
        return buf;
    }

    /**
     * Goes to the ddnsNode and gets the contents of previous split.
     *
     * @return byte array - contents of file requested.
     * @throws java.rmi.RemoteException
     */
    public byte[] back() throws java.rmi.RemoteException {
        String temp = IP[--seq];

        if (!temp.equals(IP[seq + 1])) {
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), Connect(IP[seq]));
        }

        byte[] buf = fserver.readFile(fileName[seq]);
        System.out.println("file read  " + buf.length + "  bytes");
        System.out.println("in go function n returnings : " + fileName[seq]);
        return buf;
    }

    private void prepare() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        ArrayList ipAList = new ArrayList();
        ArrayList fileNameAList = new ArrayList();
        String line = null;
        String[] token = null;
        while ((line = reader.readLine()) != null) {
            if (line.trim().length() == 0) {
                continue;
            }
            token = line.split("\\t");

            System.out.println(token[0]);
            System.out.println(token[1]);

            ipAList.add(token[0]);
            fileNameAList.add(token[1]);
        }
        IP = new String[ipAList.size()];
        fileName = new String[fileNameAList.size()];
        for (int i = 0; i < IP.length; i++) {
            IP[i] = (String) ipAList.get(i);
            fileName[i] = (String) fileNameAList.get(i);
        }
        /*        String outname = file.getName();
        outname = outname.substring(0, outname.length() - 4);
        bos = new BufferedOutputStream(new FileOutputStream(
        new File(todir, outname)));*/

        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), Connect(IP[0]) + "  inside joiner::prepare");
        seq = -1;
    }

    private String Connect(String nodeIP) {
        String remoteName = "rmi://" + nodeIP + ":1099/nodeService";
        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), remoteName);
        try {
            fserver = (FileServer) Naming.lookup("rmi://" + nodeIP + ":1099/nodeService");
            System.out.println(" connected ............");
        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "connected successfully");
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
}
