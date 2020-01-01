package com.ryanworks.fishery.client.ui.inbound.listener;

import com.ryanworks.fishery.shared.bean.InTransactionLineBean;
import com.ryanworks.fishery.shared.bean.SalesBean;

public interface InboundStockingLineDialogListener 
{    
    public void stockingLineDialogClosed( InTransactionLineBean inTransactionLineBean, SalesBean salesObj, boolean newLineBean );
}
