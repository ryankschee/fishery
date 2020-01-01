package com.ryanworks.fishery.client.ui.inbound.listener;

import com.ryanworks.fishery.shared.bean.SupplierBean;

public interface InboundWizardDialogListener 
{    
    public void wizardDialogClosed( SupplierBean supplier, long dateInMillis, int supplierTripNo );
}
