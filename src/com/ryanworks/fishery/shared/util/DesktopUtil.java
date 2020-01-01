package com.ryanworks.fishery.shared.util;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class DesktopUtil 
{
    public static void openFileOnTheFly(String filePath) 
        throws IOException
    {    
        if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(filePath);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                    // no application registered for CSVs
                    LogBook.Log("exception: " + ex.getMessage());
                    throw new IOException("Exception: No application registered for file type ["+ filePath.substring(filePath.length()-4) + "].");
                }
            }
    }
}
