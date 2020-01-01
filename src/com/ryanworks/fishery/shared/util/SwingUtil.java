package com.ryanworks.fishery.shared.util;


import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author ryan
 */
public class SwingUtil {

    public static void centerDialogOnScreen(JDialog dialog) 
    {
        java.awt.Dimension screen =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setBounds( 
            (int)(screen.getWidth()-dialog.getWidth())/2,
            (int)(screen.getHeight()-dialog.getHeight())/2,
            dialog.getWidth(),
            dialog.getHeight());
        
        dialog.setVisible(true);
    }
    
    public static void centerInternalFrameOnScreen(JInternalFrame iframe) {
        java.awt.Dimension screen =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        iframe.setBounds( 
            (int)(screen.getWidth()-iframe.getWidth())/2,
            (int)(screen.getHeight()-iframe.getHeight())/2,
            iframe.getWidth(),
            iframe.getHeight());
        
        iframe.setVisible(true);
    }

    public static void maximizeFrameOnScreen(JFrame frame) {
        java.awt.Dimension screen =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        //setSize(screen.width,screen.height);

        frame.setBounds(0, 0, (int) screen.getWidth(), (int) screen.getHeight());                
        frame.setVisible(true);
    }
}
