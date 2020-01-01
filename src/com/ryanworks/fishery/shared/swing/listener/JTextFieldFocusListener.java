package com.ryanworks.fishery.shared.swing.listener;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;

public class JTextFieldFocusListener 
    implements FocusListener
{    
    private Color lostFocusBackground = Color.WHITE;
    private Color focusBackground = new Color(255,255,153);
    private Color focusForeground = Color.BLACK;
    
    public JTextFieldFocusListener() {
        super();
    }
    
    public JTextFieldFocusListener(Color background, Color foreground) {
        super();
        
        this.lostFocusBackground = background;
        this.focusBackground = background;
        this.focusForeground = foreground;
    }
    
    @Override
    public void focusGained(FocusEvent focusEvent) 
    {
        if (((JTextField)focusEvent.getSource()).isEditable()==false)
            return;
        
        ((JTextField)focusEvent.getSource()).setBackground( focusBackground );
        ((JTextField)focusEvent.getSource()).setForeground( focusForeground );
        ((JTextField)focusEvent.getSource()).selectAll();        
    }
    
    @Override
    public void focusLost(FocusEvent focusEvent)
    {
        ((JTextField)focusEvent.getSource()).setBackground( lostFocusBackground );
    }
}
