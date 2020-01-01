package com.ryanworks.fishery.shared.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CountryUtil {

    public static List<CountryBean> getCountryList() {

        List<CountryBean> countries = new ArrayList<CountryBean>();

        Locale[] locales = Locale.getAvailableLocales();

        for (Locale locale : locales) {
            String iso = locale.getCountry();
            String code = locale.getCountry();
            String name = locale.getDisplayCountry();

            if (!"".equals(iso) && !"".equals(code) && !"".equals(name)) {
                countries.add(new CountryBean(iso, code, name));
            }
        }

        Collections.sort(countries, new CountryComparator());

        return countries;
    }
}