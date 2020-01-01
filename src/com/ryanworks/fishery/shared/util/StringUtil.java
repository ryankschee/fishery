/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ryanworks.fishery.shared.util;

import java.util.StringTokenizer;



/**
 *
 * @author ryan
 */
public class StringUtil {

    public static String encodeToChinese(String str)
    {
        if (str==null)
            return null;
        
        StringBuffer buffer = new StringBuffer();
        String token = null;

        StringTokenizer tokenizer = new StringTokenizer(str, "#&;");

        while (tokenizer.hasMoreTokens()) {
            try {
                token = tokenizer.nextToken();

                char ch = (char) Integer.parseInt( token );
                buffer.append(ch);
            }
            catch (Exception e) {
                buffer.append( token );
            }
        }

        return buffer.toString();
    }
    
}
