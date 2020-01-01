/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ryanworks.fishery.shared.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 *
 * @author Ryan C
 */
public class NetworkUtil {
    
    //checks for connection to the internet through dummy request
    public static boolean isInternetReachable( String urlStr )
    {
        try {
            System.out.print("Network Test: " + urlStr + " ...");
            //make a URL to a known source
            URL url = new URL( urlStr );

            //open a connection to that source
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

            // Set timeout for connection
            urlConnect.setConnectTimeout(1000);
            
            //trying to retrieve data from the source. If there
            //is no connection, this line will fail
            Object objData = urlConnect.getContent();

        } 
        catch (Exception e) {
            System.out.println(" failed. (" + e.getMessage() + ")");
            return false;
        }
        
        return true;
    }    
}
