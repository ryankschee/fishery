package com.ryanworks.fishery.client.ui.outbound.listener;

import com.ryanworks.fishery.shared.bean.CustomerBean;
import com.ryanworks.fishery.shared.bean.SalesBean;

public interface OutboundWizardDialogListener 
{    
    public void wizardDialogClosed( SalesBean salesBean, CustomerBean customer, long dateInMillis, int customerTripNo );
}
