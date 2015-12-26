
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.*;

/**
 * ddnsServer which provides service to ddnsClient.
 *
 * @author anonymous_02
 */
public class DDNSserver {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if (args.length != 1) {
            System.out.println("Usage: java DDNSserver machineName");
            System.exit(0);
        }
        String objectURL = "rmi://" + args[0] + ":1099/ddnsService";
        try {
            System.out.println("Constructing server implementations...");

            System.out.println("Binding server implementations to registry...");

            ddnsImpl d1 = new ddnsImpl();

            Naming.rebind(objectURL, d1);

            System.out.println("Waiting for invocations from clients...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
