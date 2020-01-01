/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ryanworks.fishery.shared.util;

/**
 *
 * @author Ryan
 */
public class CountryBean {

    private String iso;
    private String code;
    public String name;

    public CountryBean(String iso, String code, String name) {
        this.iso = iso;
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }
}
