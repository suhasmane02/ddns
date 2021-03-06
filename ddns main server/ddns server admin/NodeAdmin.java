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
public interface NodeAdmin extends Remote {

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
     */
    long getFreeSpace() throws RemoteException;
}
