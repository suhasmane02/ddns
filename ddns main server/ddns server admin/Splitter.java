//package ddnsAdmin;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * A utility class to split a file. Typically used to split a multi-megabyte
 * file into smaller, 1.44Mb floppy diskette-sized files.
 * It also generates a glue batch command to merge the split files back
 * together, it is useful if they are to be stored on local machine on
 * which this utility is running.
 *
 * For example:
 * <pre>
 *    Splitter axe = new Splitter(...);
 *    axe.setProgressPool(...);
 * 	  while (axe.moreToCopy()); {
 * 	     System.out.println("Next: "+axe.getNextTargetName());
 * 		 System.out.println("Insert diskette, then press Enter");
 * 		 ...    // prompt for Enter
 * 		 axe.go();
 * 	  }
 * </pre>
 *
 * <p>Copyright (c) 2003-2004 Paulos Siahu</p>
 * @author Paulos Siahu
 */
public class Splitter {

    // Terminology
    // - Source : the large file to be split into portions called Targets.
    // - Target : each split portion of the large file.
    // - Buffer : ideally we can read a chunk the size of a full Target
    //            from Source, then copy this chunk to Target. However,
    //            Target itself can be very large. Buffer is used as a
    //            transit of bytes from Source to Target.
    //            - buffer size : the total capacity of Buffer.
    //            - buffer length : the length of used portion of Buffer.
    //            - buffer start : the starting position of Buffer. Rather
    //              shifting bytes in buffer, this variable is used to
    //              indicate where current Buffer starts.
    final static public int FLOPPY_SIZE = 1457664;
    private File fromFile;
//	private File toDir;
    private int targetSize = FLOPPY_SIZE;
    private int bufferSize = FLOPPY_SIZE;
    private byte[] buffer;
    private BufferedInputStream bis;
    private BufferedWriter glue = null;
    private BufferedWriter locFile = null;
    private String targetName;
    String nodeIP;
    private int seq = 1;
    private boolean moreToCopy = true;
    private int bufferStart = 0;
    private int bufferLength = 0;
    private DecimalFormat seriesFormat;
    private FileServer fserver;
    private String fullName;
    private File loc;

    /**
     *
     */
    public Splitter() {
        System.out.println(" Enterd in Splitter constructor ");
    }

    /**
     * Sets the attributes and configures the ddnsServer.
     *
     * @param fromFile
     * @param fserver
     * @param nodeIP
     * @throws java.io.FileNotFoundException
     */
    public void SetAndPrepare(String fromFile, FileServer fserver, String nodeIP) throws FileNotFoundException {
        System.out.println("filename is : " + fromFile);
        this.fromFile = new File(fromFile);
        this.fserver = fserver;
        this.targetSize = FLOPPY_SIZE;
        this.bufferSize = FLOPPY_SIZE;
        this.nodeIP = nodeIP;
        seq = 1;
        fullName = "";
        prepare();
    }

    /**
     * This variant of constructor sets the buffer size to equal to the
     * target size.
     *
     * @param fromFile The name of the file to be split.
     * @param toDir The directory where smaller split files are to be
     *        written to.
     * @param targetSize The maximum size of each split file.
     *
     * @throws FileNotFoundException if fromFile is not found.
     */
    /*    public Splitter(String fromFile, FileServer fserver, int targetSize, String nodeIP)
    throws FileNotFoundException {
    this(fromFile, fserver, targetSize, targetSize, nodeIP);
    }
     */
    /**
     * This variant of constructor sets the target and buffer size
     * to default (FLOPPY_SIZE)
     *
     * @param fromFile The name of the file to be split.
     * @param toDir The directory where smaller split files are to be
     *        written to.
     *
     * @throws FileNotFoundException if fromFile is not found.
     */
    /*    public Splitter(String fromFile, FileServer fserver, String nodeIP) throws FileNotFoundException {
    this(fromFile, fserver, FLOPPY_SIZE, FLOPPY_SIZE, nodeIP);
    }
     */

    /**
     * Creates the directories / folders at remote machine (ddnsNode).
     *
     * @param fserver - FileServer reference holding remote object(rmi ddnsNode object) which creates directories / folders at remote machine.
     */
    public void mkDir(FileServer fserver) {
        File dir = fromFile.getParentFile();

        String temp = dir.getAbsolutePath();

        String token[] = temp.split(":");

        fullName = "";

        if (token.length == 0) {
            fullName = dir.getName();
        }
        for (int i = 0; i < token.length; i++) {
            System.out.println("token[" + i + "] : " + token[i]);
            fullName += token[i];
        }
        try {
            fserver.mkdirs(new File(fullName));
        } catch (Exception re) {
            re.printStackTrace();
        }
    }

    private void prepare() throws FileNotFoundException {
        System.out.println(" prepare function executing ..");
        long len = fromFile.length();
        moreToCopy = (len > 0);
        try {
            System.out.println(" length of " + fromFile.getName() + " is : " + fromFile.getAbsoluteFile().length());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (moreToCopy) {

            mkDir(fserver);

            buffer = new byte[bufferSize];
            bis = new BufferedInputStream(new FileInputStream(fromFile));
            try {
                File glueFile = new File(fromFile.getName() + "_glue.bat");
                glue = new BufferedWriter(new FileWriter(glueFile));
            } catch (IOException ex) {
                glue = null;
                System.err.println(this);
                System.err.println("> Warning... Cannot create glue.bat");
            }
            try {
                loc = new File(fromFile.getName() + ".loc");
                locFile = new BufferedWriter(new FileWriter(loc));
            } catch (IOException ex) {
                locFile = null;
                System.err.println(this);
                System.err.println("> Warning... Cannot create .loc");
            }
            int disks = (int) Math.ceil(len / (double) targetSize);

            int dec = 0;
            int rem = disks;
            StringBuffer suffix = new StringBuffer();
            // Calculates number of digits for filename suffix.
            do {
                rem /= 10;
                suffix.append('0');
                dec++;
            } while (rem != 0);
            seriesFormat = new DecimalFormat(suffix.toString());

            targetName = setNextTargetName();
        }
    }

    /**
     * Writes the file content to the file at specified remote machine identified by nodeIP.
     *
     * @param fserver - FileServer reference holding remote object(rmi ddnsNode object) which writes contents of file.
     * @param nodeIP - remote machine IP (ddnsNode IP)
     * @return integer - number of bytes written.
     * @throws java.io.IOException - if an I/O error occurs.
     */
    public int go(FileServer fserver, String nodeIP) throws IOException {
        System.out.println(" go go go ... function executing ..");
        int bytesCopied = 0;
        boolean flag = false;

        if (!moreToCopy) {
            return 0;
        }

        File tofile = new File(targetName);
        while ((bytesCopied < targetSize) && (bis != null)) {
            if (bufferLength <= 0) {
                bufferLength = bis.read(buffer);
            }

            if (bufferLength > 0) {
                if (bytesCopied + bufferLength > targetSize) {
                    int residue = targetSize - bytesCopied;
                    do {
                        flag = fserver.writeFile(targetName, buffer, bufferStart, residue);
                    } while (flag == false);
                    bytesCopied += residue;
                    bufferStart += residue;
                    bufferLength -= residue;
                } else {
                    do {
                        flag = fserver.writeFile(targetName, buffer, bufferStart, bufferLength);
                    } while (flag == false);
                    bytesCopied += bufferLength;
                    bufferStart = 0;
                    bufferLength = 0;
                }
            } else {
                moreToCopy = false;
                bis.close();
                bis = null;
                if (glue != null) {
                    glue.write(" " + fromFile.getName());
                    glue.flush();
                    glue.close();
                    glue = null;
                }
            }
        }
        locFile.write(nodeIP);
        locFile.write("\t");
        locFile.write(targetName);
        locFile.write("\n");
        if (!moreToCopy) {
            locFile.flush();
            locFile.close();
            locFile = null;
        }
        targetName = setNextTargetName();
        return bytesCopied;
    }

    /**
     * @return next target filename. Useful for diagnostic purposes to
     *         inform the user before calling the next go().
     */
    public String getNextTargetName() {
        return targetName;
    }

    /**
     * @return true if there are more bytes to copy.
     */
    public boolean moreToCopy() {
        return moreToCopy;
    }

    private String setNextTargetName() {
        String target = null;

        try {
            if (glue != null) {
                if (seq == 1) {
                    glue.write("copy /b ");
                } else {
                    glue.write(" + ");
                }
            }

            fullName = fullName.replace("\\", "//");
            target = fullName + "//" + fromFile.getName() + '.' + seriesFormat.format(seq++);

            if (glue != null) {
                glue.write(target);
            }
        } catch (IOException ex) {
        }
        return target;
    }

    /**
     *
     * @return File - file containing locations of all splits of requested file.
     */
    public File getLocFile() {
        return loc;
    }
}