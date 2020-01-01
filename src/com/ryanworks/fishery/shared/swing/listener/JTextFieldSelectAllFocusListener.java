package com.ryanworks.fishery.shared.swing.listener;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class JTextFieldSelectAllFocusListener 
    implements FocusListener
{        
    public JTextFieldSelectAllFocusListener() {
        super();
    }
        
    @Override
    public void focusGained(FocusEvent focusEvent) 
    {        
        ((JTextField)focusEvent.getSource()).selectAll();        
    }
    
    @Override
    public void focusLost(FocusEvent focusEvent)
    {
    }
}
