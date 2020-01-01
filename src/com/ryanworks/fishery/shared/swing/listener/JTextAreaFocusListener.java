package com.ryanworks.fishery.shared.swing.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextArea;

public class JTextAreaFocusListener 
    implements FocusListener
{
    public void focusGained(FocusEvent focusEvent) 
    {
        if (((JTextArea)focusEvent.getSource()).isEditable()==false)
            return;
        
        ((JTextArea)focusEvent.getSource()).setBackground( new Color(255,255,153) );
    }
    
    public void focusLost(FocusEvent focusEvent)
    {
        ((JTextArea)focusEvent.getSource()).setBackground( Color.WHITE );
    }
}
