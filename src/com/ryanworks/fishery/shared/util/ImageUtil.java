package com.ryanworks.fishery.shared.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtil {
        
    public static ImageIcon createImageIcon(File file, String description) {
        return createImageIcon(file.getAbsolutePath(), description);        
    }
    
    /** 
     * Returns an ImageIcon, or null if the path was invalid. 
     */
    public static ImageIcon createImageIcon(String path, String description) {
        return new ImageIcon(path, description);
    }
    
    public static ImageIcon createImageIconFromByteArray(byte[] byteArray) {
        try {            
            BufferedImage bufferedImage = ImageIO.read( new ByteArrayInputStream(byteArray) );            
            return new ImageIcon( bufferedImage );
        }
        catch (Exception e) {
            LogBook.Log("exception: " + e.getMessage());
            return null;
        }
    }
        
    public static byte[] createByteArrayFromImageFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            //InputStream in = resource.openStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
        
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum); 
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset 
                off to this byte array output stream.*/
                System.out.println("read " + readNum + " bytes,");
            }
            
            byte[] bytes = bos.toByteArray();
            return bytes;
        } 
        catch (Exception e) {
            LogBook.Log("exception: " + e.getMessage());
            return null;
        }        
    }
}
