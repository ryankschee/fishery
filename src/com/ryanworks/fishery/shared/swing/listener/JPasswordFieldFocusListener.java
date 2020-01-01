package com.ryanworks.fishery.shared.swing.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JPasswordField;

public class JPasswordFieldFocusListener 
    implements FocusListener
{
    public void focusGained(FocusEvent focusEvent) 
    {
        if (((JPasswordField)focusEvent.getSource()).isEditable()==false)
            return;
        
        ((JPasswordField)focusEvent.getSource()).setBackground( new Color(255,255,153) );
    }
    
    public void focusLost(FocusEvent focusEvent)
    {
        ((JPasswordField)focusEvent.getSource()).setBackground( Color.WHITE );
    }
}
