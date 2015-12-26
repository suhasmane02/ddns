/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;

/**
 * An interface used by ddnsClient needed by java rmi.
 *
 * @author anonymous_02
 */
public interface Compute extends Remote {

    /**
     * Checks the username and password at remote machine (ddnsServer).
     *
     * @param userid username of user.
     * @param password password of user.
     * @return true for valid userid and password.
     * @throws java.rmi.RemoteException
     */
    boolean isValidUser(String userid, String password) throws RemoteException;

    /**
     * Gets the Location of file from remote machine (ddnsServer).
     *
     * @param fileId an unique id of file.
     * @param path absolute path of file.
     * @param mode read or write mode (read : false, write : true)
     * @return location of file.
     * @throws java.rmi.RemoteException
     */
    byte[] getLocations(int fileId/*, javax.swing.tree.TreePath path*/, boolean mode) throws RemoteException;

    /**
     * Gets XML file : File System Hierarchy from remote machine (ddnsServer).
     *
     * @return xml file.
     * @throws java.rmi.RemoteException
     */
    byte[] getXMLFile() throws RemoteException;

    /**
     * Closes the file and sets file busy mode as free.
     *
     * @param fileId - id of the file to be closed.
     * @return boolean valus - true if closed successfully, false otherwise.
     * @throws java.rmi.RemoteException
     */
    boolean closeFile(int fileId) throws RemoteException;
}