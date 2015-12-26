/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;

/**
 * ddnsNode provides service to ddnsServer and ddnsClient.
 *
 * @author anonymous_02
 */
public class FileServerClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv) {
        // TODO code application logic here
        if (argv.length != 1) {
            System.out.println("Usage: java FileServerClass machineName");
            System.exit(0);
        }
        String objectURL = "rmi://" + argv[0] + ":1099/nodeService";
        try {
            System.out.println("Constructing server implementations...");

            System.out.println("Binding server implementations to registry...");

            FileServerImpl fs = new FileServerImpl();

            Naming.rebind(objectURL, fs);

            System.out.println("Waiting for invocations from clients...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
