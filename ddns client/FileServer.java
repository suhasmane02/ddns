/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;

/**
 * An interface used by ddnsNode needed by java rmi.
 *
 * @author anonymous_02
 */
public interface FileServer extends Remote {

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
    boolean writeFile(String fileName, byte[] buffer, int bufferStart, int bufferLength) throws RemoteException;

    /**
     * Reads the content from specified file at remote machine (ddnsNode).
     *
     * @param fileName name of file to be read.
     * @return contents of file.
     * @throws java.rmi.RemoteException
     */
    byte[] readFile(String fileName) throws RemoteException;

    /**
     * Creates the directories / folders at remote machine (ddnsNode).
     *
     * @param file directory / directories absolute path specified by file.
     * @return boolean - true if successful, otherwise false.
     * @throws java.rmi.RemoteException
     */
    boolean mkdirs(java.io.File file) throws RemoteException;
}
