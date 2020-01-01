package com.ryanworks.fishery.shared.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static String convertListToString(List list) {

        StringBuilder buffer = new StringBuilder();
        for (Object obj: list)
            buffer.append(obj.toString()).append( ",");

        String stringList = buffer.toString().trim();

        if (stringList.length() > 0)
            return stringList.substring(0, stringList.length()-1);
        else
            return stringList;
    }
    
    public static List initializeNullList(List aList) {
        if (aList == null)
            return new ArrayList();
        return aList;
    }
}
