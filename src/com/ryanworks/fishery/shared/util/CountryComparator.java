/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ryanworks.fishery.shared.util;

import java.text.Collator;
import java.util.Comparator;

/**
 *
 * @author Ryan
 */
public class CountryComparator
    implements Comparator<CountryBean> {

    private Comparator comparator;

    public CountryComparator() {
        comparator = Collator.getInstance();
    }

    public int compare(CountryBean o1, CountryBean o2) {
        return comparator.compare(o1.name, o2.name);
    }
}
