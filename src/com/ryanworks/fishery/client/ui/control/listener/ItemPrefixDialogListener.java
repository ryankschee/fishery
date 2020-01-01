package com.ryanworks.fishery.client.ui.control.listener;

import com.ryanworks.fishery.shared.bean.ItemBean;

public interface ItemPrefixDialogListener 
{    
    public void itemPrefixDialogClosed( ItemBean item, int indexInList, int callerTabIndex );
}
