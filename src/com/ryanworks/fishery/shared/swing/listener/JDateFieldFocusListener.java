package com.ryanworks.fishery.shared.swing.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class JDateFieldFocusListener 
    implements FocusListener
{
    @Override
    public void focusGained(FocusEvent focusEvent) 
    {
        if (((JTextField)focusEvent.getSource()).isEditable()==false)
            return;
        
        ((JTextField)focusEvent.getSource()).setBackground( new Color(255,255,153) );
        ((JTextField)focusEvent.getSource()).select(0, 2);
    }
    
    @Override
    public void focusLost(FocusEvent focusEvent)
    {
        ((JTextField)focusEvent.getSource()).setBackground( Color.WHITE );
    }
}
