/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanworks.fishery.shared.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class LogBook {
    // path to logbook
    private static String path = AppConstants.PROJECT_PATH + "log/logfile.txt";
    
    public static boolean Log(String message) {
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file, true);
            
            BufferedWriter bw = new BufferedWriter(fileWriter);
            fileWriter.append(message + "\n");
            
            fileWriter.close();
            bw.close();
            
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
