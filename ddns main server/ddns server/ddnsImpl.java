
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
public class ddnsImpl extends UnicastRemoteObject implements Compute {

    private java.sql.Connection conn = null;
    private java.sql.Statement stmt;
    private java.sql.ResultSet rs;
    private boolean readMode = false;
    private boolean writeMode = true;

    /**
     *
     * @throws java.rmi.RemoteException
     */
    public ddnsImpl() throws RemoteException {
    }

    /**
     * Checks the username and password at remote machine (ddnsServer).
     *
     * @param userid username of user.
     * @param password password of user.
     * @return true for valid userid and password.
     * @throws java.rmi.RemoteException
     */
    public boolean isValidUser(String userId, String password) throws RemoteException {

        initDatabase();
        String queryString = "select username, passwd from users where username='" + userId + "' and passwd='" + password + "'";
        String username = "null", passwd = "null";
        try {
            java.sql.ResultSet r = stmt.executeQuery(queryString);
            while (r.next()) {
                username = r.getString("username");
                passwd = r.getString("passwd");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("closing the database connections...");
            shutDatabase();
        }

        if (userId.equals(username) && password.equals(password)) {
            /*           try
            {
            CreatXMLFile.main(null);
            }
            catch(Exception ex)
            {
            ex.printStackTrace();
            }
             */ return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the Location of file from remote machine (ddnsServer).
     *
     * @param fileId an unique id of file.
     * @param path absolute path of file.
     * @param mode read or write mode (read : false, write : true)
     * @return location of file.
     * @throws java.rmi.RemoteException
     */
    public byte[] getLocations(int fileId/*, javax.swing.tree.TreePath path*/, boolean mode) throws RemoteException {

        initDatabase();
        String busyMode = "write", grant = "no grant";
        String fileName = "";
        int onlineUsers = 0;
        System.out.println("fileId = " + fileId);
        try {
            java.sql.ResultSet r = stmt.executeQuery("select * from file_sys where f_id='" + fileId + "'");
            System.out.println(" r : " + r);
            while (r.next()) {
                busyMode = r.getString("busyMode");
                fileName = r.getString("file_location");
                onlineUsers = r.getInt("online_users");
                if ((busyMode.equalsIgnoreCase("free") || busyMode.equalsIgnoreCase("read")) && mode == readMode) {
                    grant = "read";
                } else if (busyMode.equalsIgnoreCase("free") && mode == writeMode) {
                    grant = "write";
                }
            }

            if (grant.equalsIgnoreCase("read") || grant.equalsIgnoreCase("write")) {
                onlineUsers++;
                int rows = stmt.executeUpdate("update file_sys set busyMode='" + grant + "',online_users=" + onlineUsers + " where f_id='" + fileId + "'");
                System.out.println("number of rows affected : " + rows);
            } else {
                String value = "file is busy " + onlineUsers + " are " + busyMode + "ing this file. Please TRY LATER";
                throw new Exception(value);
//                return value.getBytes();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage().getBytes();
        } finally {
            System.out.println("closing the database connections...");
            shutDatabase();
        }
//        String fileName = path.getPathComponent(path.getPathCount() - 1).toString();
        //      fileName += ".loc";

        try {
            File file = new File(fileName);
            byte buffer[] = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (Exception e) {
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return (null);
        } finally {
        }
    }

    /**
     * Gets XML file : File System Hierarchy from remote machine (ddnsServer).
     *
     * @return xml file.
     * @throws java.rmi.RemoteException
     */
    public byte[] getXMLFile() throws RemoteException {
        String fileName = "test.xml";
        try {
            File file = new File(fileName);
            byte buffer[] = new byte[(int) file.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
            input.read(buffer, 0, buffer.length);
            input.close();
            return (buffer);
        } catch (Exception e) {
            System.out.println("FileImpl: " + e.getMessage());
            e.printStackTrace();
            return (null);
        } finally {
        }
    }

    /**
     * Closes the file and sets file busy mode as free.
     *
     * @param fileId - id of the file to be closed.
     * @return boolean valus - true if closed successfully, false otherwise.
     * @throws java.rmi.RemoteException
     */
    public boolean closeFile(int fileId) throws RemoteException {
        int rows = 0, users = 0;
        String busyMode = "";
        initDatabase();
        try {
            java.sql.ResultSet rs = stmt.executeQuery("select online_users, busyMode from file_sys where f_id='" + fileId + "'");
            while (rs.next()) {
                busyMode = rs.getString("busyMode");
                users = rs.getInt("online_users");
            }

            if (users <= 1) {
                --users;
                rows = stmt.executeUpdate("update file_sys set busyMode='free',  online_users=" + users + "  where f_id='" + fileId + "'");
            }//, online_users="+ users +" 
            else {
                --users;
                rows = stmt.executeUpdate("update file_sys set online_users=" + users + " where f_id='" + fileId + "'");
            }

            if (rows != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            shutDatabase();
        }
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
//        javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Connection Established ..");
        try {
            stmt = conn.createStatement();
            System.out.println("Statement Created");
//            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Statement Created ..");
        } catch (Exception e) {
            System.out.println(e);
            javax.swing.JOptionPane.showMessageDialog(new java.awt.Frame(), "Error : " + e.getMessage());
            System.exit(0);
        }
    }

    private void shutDatabase() {
        try {
            stmt.close();
            conn.clearWarnings();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
