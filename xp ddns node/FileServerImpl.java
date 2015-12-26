/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;

/**
 * 
 *
 * @author anonymous_02
 */
public class FileServerImpl extends UnicastRemoteObject implements FileServer, NodeAdmin {

    private String fileName;
    private int disks;

    /**
     * 
     * @throws java.rmi.RemoteException
     */
    public FileServerImpl() throws RemoteException {
    }

    /**
     * Writes the content to specified file at remote machine (ddnsNode).
     *
     * @param fileName name of file to be written.
     * @param buffer content to be written to file.
     * @param bufferStart
     * @param bufferLength
     * @return true if file is written successfully at remote machine (ddns????Node).
     * @throws java.rmi.RemoteException
     */
    public boolean writeFile(String fileName, byte[] buffer, int bufferStart, int bufferLength) throws RemoteException {
        File tofile;
        BufferedOutputStream bos = null;
        try {
            tofile = new File(fileName);
            bos = new BufferedOutputStream(new FileOutputStream(tofile));

            bos.write(buffer, bufferStart, bufferLength);
            System.out.println(" Writting of " + fileName + " has started");
            bos.close();
            bos = null;
            System.out.println(" Writting of " + fileName + " finished");

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Reads the content from specified file at remote machine (ddnsNode).
     *
     * @param fileName name of file to be read.
     * @return contents of file.
     * @throws java.rmi.RemoteException
     */
    public byte[] readFile(String fileName) throws RemoteException {
        try {
            File file = new File(fileName);
            byte buffer[] = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return (null);
        }
    }

    /**
     * Returns the number of bytes available to this virtual machine on the
     * partition <a href="#partName">named</a> by this abstract pathname.  When
     * possible, this method checks for write permissions and other operating
     * system restrictions and will therefore usually provide a more accurate
     * estimate of how much new data can actually be written than {@link
     * #getFreeSpace}.
     *
     * <p> The returned number of available bytes is a hint, but not a
     * guarantee, that it is possible to use most or any of these bytes.  The
     * number of unallocated bytes is most likely to be accurate immediately
     * after this call.  It is likely to be made inaccurate by any external
     * I/O operations including those made on the system outside of this
     * virtual machine.  This method makes no guarantee that write operations
     * to this file system will succeed.
     *
     * @return  The number of available bytes on the partition or <tt>0L</tt>
     *          if the abstract pathname does not name a partition.  On
     *          systems where this information is not available, this method
     *          will be equivalent to a call to {@link #getFreeSpace}.
     *
     * @throws  SecurityException
     *          If a security manager has been installed and it denies
     *          {@link RuntimePermission}<tt>("getFileSystemAttributes")</tt>
     *          or its {@link SecurityManager#checkRead(String)} method denies
     *          read access to the file named by this abstract pathname
     *
     */
    public long getFreeSpace() throws RemoteException {
        try {
            File dir = new File("C:\\");
            long space = dir.getUsableSpace();
            System.out.println("free space is " + space);
            return space;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Creates the directories / folders at remote machine (ddnsNode).
     *
     * @param file directory / directories absolute path specified by file.
     * @return boolean - true if successful, otherwise false.
     * @throws java.rmi.RemoteException
     */
    public boolean mkdirs(File file) throws RemoteException {
        boolean flag = false;
        flag = file.mkdirs();
        return flag;
    }
}
