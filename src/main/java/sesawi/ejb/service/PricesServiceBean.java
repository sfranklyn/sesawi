/*
 * Copyright 2014 Samuel Franklyn <sfranklyn@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sesawi.ejb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.ejb.dao.PricesDaoBean;
import sesawi.jpa.Prices;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class PricesServiceBean {

    private static final Logger log = Logger.getLogger(PricesServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private PricesDaoBean pricesDaoBean;

    public List<String> saveCreate(Prices prices, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(prices.getPriceCode())) {
            errorList.add(messageSource.getString("price_code_required"));
        }
        if (prices.getPriceEntry() == null) {
            errorList.add(messageSource.getString("price_entry_required"));
        } else {
            if (prices.getPriceEntry().longValue() <= 0) {
                errorList.add(messageSource.getString("price_entry_greater_than_zero"));
            }
        }
        if (prices.getPriceEntryHour() == null) {
            errorList.add(messageSource.getString("price_entry_hour_required"));
        } else {
            if (prices.getPriceEntryHour().longValue() <= 0) {
                errorList.add(messageSource.getString("price_entry_hour_greater_than_zero"));
            }
        }
        if (prices.getPricePerHour() == null) {
            errorList.add(messageSource.getString("price_per_hour_required"));
        } else {
            if (prices.getPricePerHour().longValue() <= 0) {
                errorList.add(messageSource.getString("price_per_hour_greater_than_zero"));
            }
        }
        if (prices.getPriceLost() == null) {
            errorList.add(messageSource.getString("price_lost_required"));
        } else {
            if (prices.getPriceLost().longValue() <= 0) {
                errorList.add(messageSource.getString("price_lost_greater_than_zero"));
            }
        }
        if (errorList.isEmpty()) {
            prices.setPriceLastChange(new Date());
            try {
                pricesDaoBean.insert(prices);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("price_code_already_registered"));
                        duplicate = true;
                        break;
                    }
                    cause = cause.getCause();
                }
                if (!duplicate) {
                    errorList.add(ex.toString());
                    log.severe(ex.toString());
                }
            }
        }
        return errorList;
    }

    public List<String> saveUpdate(Prices prices, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (prices.getPriceId() == null || prices.getPriceId() == 0) {
            errorList.add(messageSource.getString("price_id_required"));
        }
        if (prices.getPriceEntry() == null) {
            errorList.add(messageSource.getString("price_entry_required"));
        } else {
            if (prices.getPriceEntry().longValue() <= 0) {
                errorList.add(messageSource.getString("price_entry_greater_than_zero"));
            }
        }
        if (prices.getPriceEntryHour() == null) {
            errorList.add(messageSource.getString("price_entry_hour_required"));
        } else {
            if (prices.getPriceEntryHour().longValue() <= 0) {
                errorList.add(messageSource.getString("price_entry_hour_greater_than_zero"));
            }
        }
        if (prices.getPricePerHour() == null) {
            errorList.add(messageSource.getString("price_per_hour_required"));
        } else {
            if (prices.getPricePerHour().longValue() <= 0) {
                errorList.add(messageSource.getString("price_per_hour_greater_than_zero"));
            }
        }
        if (prices.getPriceLost() == null) {
            errorList.add(messageSource.getString("price_lost_required"));
        } else {
            if (prices.getPriceLost().longValue() <= 0) {
                errorList.add(messageSource.getString("price_lost_greater_than_zero"));
            }
        }
        if (errorList.isEmpty()) {
            prices.setPriceLastChange(new Date());
            try {
                pricesDaoBean.update(prices);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Prices prices, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (prices.getPriceId() == null || prices.getPriceId() == 0) {
            errorList.add(messageSource.getString("price_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                pricesDaoBean.delete(prices.getPriceId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
