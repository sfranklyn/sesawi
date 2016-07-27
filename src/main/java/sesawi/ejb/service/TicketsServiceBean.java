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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import sesawi.ejb.dao.LocationsDaoBean;
import sesawi.ejb.dao.TicketsDaoBean;
import sesawi.jpa.Locations;
import sesawi.jpa.Tickets;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class TicketsServiceBean {

    private static final Logger log = Logger.getLogger(TicketsServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private TicketsDaoBean ticketsDaoBean;
    @EJB
    private LocationsDaoBean locationsDaoBean;

    public List<String> saveCreate(Tickets tickets, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = validate(locale, tickets, errorList);
        if (errorList.isEmpty()) {
            try {
                Locations locations = locationsDaoBean.selectByLocationName(tickets.getLocationName());
                tickets.setOwnerName(locations.getOwners().getOwnerName());
                ticketsDaoBean.insert(tickets);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("ticket_id_already_registered"));
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

    private ResourceBundle validate(Locale locale, Tickets tickets, List<String> errorList) {
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (tickets.getTicketId() == null || tickets.getTicketId().isEmpty()) {
            errorList.add(messageSource.getString("ticket_id_required"));
        }
        if (tickets.getTicketNo() == null || tickets.getTicketNo().isEmpty()) {
            errorList.add(messageSource.getString("ticket_no_required"));
        }
        if (tickets.getLocationName() == null || tickets.getLocationName().isEmpty()) {
            errorList.add(messageSource.getString("location_name_required"));
        }
        if (tickets.getComputerName() == null || tickets.getComputerName().isEmpty()) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if (tickets.getUserName() == null || tickets.getUserName().isEmpty()) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (tickets.getTicketEntryTime() == null) {
            errorList.add(messageSource.getString("ticket_entry_time_required"));
        }
        if (tickets.getPriceCode() == null || tickets.getPriceCode().isEmpty()) {
            errorList.add(messageSource.getString("price_code_required"));
        }
        if (tickets.getTicketEntryTime() != null && tickets.getTicketPoliceNo().isEmpty()) {
            errorList.add(messageSource.getString("ticket_police_no_required"));
        }
        if (tickets.getTicketDuration() != null) {
            if (tickets.getTicketDuration().longValue() < 0) {
                errorList.add(messageSource.getString("ticket_duration_less_than_zero"));
            }
        }
        return messageSource;
    }

    public List<String> saveUpdate(Tickets tickets, Locale locale) {
        List<String> errorList = new ArrayList<>();
        validate(locale, tickets, errorList);
        if (errorList.isEmpty()) {
            try {
                ticketsDaoBean.update(tickets);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Tickets tickets, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (tickets.getTicketId() == null || tickets.getTicketId().isEmpty()) {
            errorList.add(messageSource.getString("ticket_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                ticketsDaoBean.delete(tickets.getTicketId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public Integer getPrice(Tickets tickets) {
        DateTime dtEntry = new DateTime(tickets.getTicketEntryTime());
        DateTime dtExit = new DateTime(tickets.getTicketExitTime());
        Duration duration = new Duration(dtEntry, dtExit);
        long ticketDuration = duration.getStandardHours();
        tickets.setTicketDuration(new BigDecimal(ticketDuration));
        long ticketDurationCalc = ticketDuration - tickets.getPriceEntryHour().longValue();
        if (ticketDurationCalc < 0) {
            ticketDurationCalc = 0l;
        }
        BigDecimal ticketPriceBD = tickets.getPricePerHour().
                multiply(new BigDecimal(ticketDurationCalc)).
                add(tickets.getPriceEntry());
        if (tickets.getTicketLost()) {
            ticketPriceBD = ticketPriceBD.add(tickets.getPriceLost());
        }
        tickets.setTicketPrice(ticketPriceBD);

        return ticketPriceBD.intValue();
    }

}
