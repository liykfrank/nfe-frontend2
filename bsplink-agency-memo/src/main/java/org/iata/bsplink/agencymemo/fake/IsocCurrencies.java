package org.iata.bsplink.agencymemo.fake;

import static org.iata.bsplink.agencymemo.fake.Country.ISO_COUNTRY_CODE_AA;
import static org.iata.bsplink.agencymemo.fake.Country.ISO_COUNTRY_CODE_BB;
import static org.iata.bsplink.agencymemo.fake.Country.ISO_COUNTRY_CODE_CC;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class IsocCurrencies {

    private String isoc;
    private List<Currency> currencies;

    /**
     * Get all currencies by isoc.
     * @param isoc Country code
     * @return IsocCurrencies list of currencies.
     */
    public List<IsocCurrencies> getCurrenciesByIsoc1(String isoc) {

        List<IsocCurrencies> list = new ArrayList<>();

        if (isoc.equals(ISO_COUNTRY_CODE_AA)) {

            IsocCurrencies currency = new IsocCurrencies();

            currency.setIsoc(ISO_COUNTRY_CODE_AA);

            Currency abc = new Currency();
            abc.setName("ABC");
            abc.setNumDecimals(2);
            abc.setExpirationDate(LocalDate.of(2058, 12, 24));

            List<Currency> listCurrencies = new ArrayList<>();
            listCurrencies.add(abc);

            Currency def = new Currency();
            def.setName("DEF");
            def.setNumDecimals(3);
            def.setExpirationDate(LocalDate.of(2058, 12, 25));
            listCurrencies.add(def);


            Currency ghi = new Currency();
            ghi.setName("GHI");
            ghi.setNumDecimals(0);
            ghi.setExpirationDate(LocalDate.of(2058, 12, 25));
            listCurrencies.add(ghi);

            currency.setCurrencies(listCurrencies);

            list.add(currency);

            return list;

        } else if (isoc.equals(ISO_COUNTRY_CODE_BB)) {

            IsocCurrencies currency = new IsocCurrencies();
            currency.setIsoc(ISO_COUNTRY_CODE_BB);

            Currency zzz = new Currency();
            zzz.setName("ZZZ");
            zzz.setNumDecimals(0);
            zzz.setExpirationDate(LocalDate.of(2017, 1, 1));

            List<Currency> listCurrencies = new ArrayList<>();
            listCurrencies.add(zzz);
            currency.setCurrencies(listCurrencies);
            list.add(currency);

            return list;

        } else if (isoc.equals(ISO_COUNTRY_CODE_CC)) {

            IsocCurrencies currency = new IsocCurrencies();
            currency.setIsoc(ISO_COUNTRY_CODE_CC);

            Currency ccc = new Currency();
            ccc.setName("ERR");
            ccc.setNumDecimals(2);
            ccc.setExpirationDate(LocalDate.of(2031, 1, 1));

            List<Currency> listCurrencies = new ArrayList<>();
            listCurrencies.add(ccc);
            currency.setCurrencies(listCurrencies);
            list.add(currency);

            return list;
        }

        return list;
    }




    /**
     * Returns all currencies.
     *
     * @return listIsocCurrencies A list with all currencies.
     */
    public static List<IsocCurrencies> getAllCurrencies() {

        List<IsocCurrencies> listIsocCurrencies = new ArrayList<>();

        IsocCurrencies aaCurrency = new IsocCurrencies();
        listIsocCurrencies.addAll(aaCurrency.getCurrenciesByIsoc1(ISO_COUNTRY_CODE_AA));

        IsocCurrencies bbCurrency = new IsocCurrencies();
        listIsocCurrencies.addAll(bbCurrency.getCurrenciesByIsoc1(ISO_COUNTRY_CODE_BB));

        IsocCurrencies ccCurrency = new IsocCurrencies();
        listIsocCurrencies.addAll(ccCurrency.getCurrenciesByIsoc1(ISO_COUNTRY_CODE_CC));

        return listIsocCurrencies;
    }
}
