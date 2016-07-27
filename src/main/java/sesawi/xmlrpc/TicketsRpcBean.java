/*
 * Copyright 2014 Samuel Franklyn <sfranklyn at gmail.com>.
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
package sesawi.xmlrpc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.api.TicketsRpc;
import sesawi.ejb.dao.LocationsDaoBean;
import sesawi.ejb.dao.TicketsDaoBean;
import sesawi.ejb.service.TicketsServiceBean;
import sesawi.jpa.Locations;
import sesawi.jpa.Tickets;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Stateless
public class TicketsRpcBean implements TicketsRpc {

    private static final Logger log = Logger.getLogger(TicketsRpcBean.class.getName());
    @EJB
    private TicketsDaoBean ticketsDaoBean;
    @EJB
    private TicketsServiceBean ticketsServiceBean;
    @EJB
    private LocationsDaoBean locationsDaoBean;

    @Override
    public List<String> sendEntry(Map ticketsMap) {
        List<String> errorList = new ArrayList<>();
        try {
            Tickets tickets = new Tickets();
            convertMapToTicketsEntry(tickets, ticketsMap);
            errorList = ticketsServiceBean.saveCreate(tickets, Locale.ENGLISH);
        } catch (RuntimeException ex) {
            log.severe(ex.getMessage());
            errorList.add(ex.getMessage());
        }
        return errorList;
    }

    private void convertMapToTicketsEntry(Tickets tickets, Map ticketsMap) {
        String locationName = (String) ticketsMap.get("locationName");
        Locations locations = locationsDaoBean.selectByLocationName(locationName);

        tickets.setTicketId((String) ticketsMap.get("ticketId"));
        tickets.setTicketNo((String) ticketsMap.get("ticketNo"));
        tickets.setOwnerName(locations.getOwners().getOwnerName());
        tickets.setLocationName(locationName);
        tickets.setComputerName((String) ticketsMap.get("computerName"));
        tickets.setUserName((String) ticketsMap.get("userName"));
        tickets.setTicketEntryTime((Date) ticketsMap.get("ticketEntryTime"));
        tickets.setPriceCode((String) ticketsMap.get("priceCode"));
        tickets.setPriceEntry(new BigDecimal((String) ticketsMap.get("priceEntry")));
        tickets.setPriceEntryHour(new BigDecimal((String) ticketsMap.get("priceEntryHour")));
        tickets.setPricePerHour(new BigDecimal((String) ticketsMap.get("pricePerHour")));
        tickets.setPriceLost(new BigDecimal((String) ticketsMap.get("priceLost")));
        tickets.setTicketPoliceNo((String) ticketsMap.get("ticketPoliceNo"));
    }

    private void convertMapToTicketsExit(Tickets tickets, Map ticketsMap) {
        tickets.setTicketExitTime((Date) ticketsMap.get("ticketExitTime"));
        tickets.setTicketDuration(new BigDecimal((String) ticketsMap.get("ticketDuration")));
        tickets.setTicketLost((Boolean) ticketsMap.get("ticketLost"));
    }

    @Override
    public List<String> sendExit(Map ticketsMap) {
        List<String> errorList = new ArrayList<>();
        try {
            Tickets tickets = ticketsDaoBean.find((String) ticketsMap.get("ticketId"));
            if (tickets != null) {
                convertMapToTicketsExit(tickets, ticketsMap);
                errorList = ticketsServiceBean.saveUpdate(tickets, Locale.ENGLISH);
            }
        } catch (RuntimeException ex) {
            log.severe(ex.getMessage());
            errorList.add(ex.getMessage());
        }
        return errorList;
    }

    @Override
    public List<Map> receiveEntry(String locationName) {
        List<Map> ticketsListMap = new ArrayList<>();
        List<Tickets> ticketsList = ticketsDaoBean.selectByLocationName(locationName);
        for (Tickets tickets : ticketsList) {
            Map ticketsMap = new HashMap();
            ticketsMap.put("ticketId", tickets.getTicketId());
            ticketsMap.put("ticketNo", tickets.getTicketNo());
            ticketsMap.put("locationName", tickets.getLocationName());
            ticketsMap.put("computerName", tickets.getComputerName());
            ticketsMap.put("userName", tickets.getUserName());
            ticketsMap.put("ticketEntryTime", tickets.getTicketEntryTime());
            ticketsMap.put("ticketPoliceNo", tickets.getTicketPoliceNo());
            ticketsMap.put("priceCode", tickets.getPriceCode());
            ticketsListMap.add(ticketsMap);
        }
        return ticketsListMap;
    }

    @Override
    public Integer receivePrice(Map ticketsMap) {
        Integer price = 0;
        Tickets tickets = ticketsDaoBean.find((String) ticketsMap.get("ticketId"));
        if (tickets != null) {
            convertMapToTicketsExit(tickets, ticketsMap);
            price = ticketsServiceBean.getPrice(tickets);
        }
        return price;
    }

    @Override
    public List<Map> receiveEntryByTicketNo(String locationName, String ticketNo) {
        List<Map> ticketsListMap = new ArrayList<>();
        List<Tickets> ticketsList = ticketsDaoBean.selectByTicketNo(locationName, ticketNo);
        for (Tickets tickets : ticketsList) {
            Map ticketsMap = new HashMap();
            ticketsMap.put("ticketId", tickets.getTicketId());
            ticketsMap.put("ticketNo", tickets.getTicketNo());
            ticketsMap.put("locationName", tickets.getLocationName());
            ticketsMap.put("computerName", tickets.getComputerName());
            ticketsMap.put("userName", tickets.getUserName());
            ticketsMap.put("ticketEntryTime", tickets.getTicketEntryTime());
            ticketsMap.put("ticketPoliceNo", tickets.getTicketPoliceNo());
            ticketsMap.put("priceCode", tickets.getPriceCode());
            ticketsListMap.add(ticketsMap);
        }
        return ticketsListMap;
    }

    @Override
    public List<Map> receiveEntryByTicketPoliceNo(String locationName, String ticketPoliceNo) {
        List<Map> ticketsListMap = new ArrayList<>();
        List<Tickets> ticketsList = ticketsDaoBean.selectByTicketPoliceNo(locationName, ticketPoliceNo);
        for (Tickets tickets : ticketsList) {
            Map ticketsMap = new HashMap();
            ticketsMap.put("ticketId", tickets.getTicketId());
            ticketsMap.put("ticketNo", tickets.getTicketNo());
            ticketsMap.put("locationName", tickets.getLocationName());
            ticketsMap.put("computerName", tickets.getComputerName());
            ticketsMap.put("userName", tickets.getUserName());
            ticketsMap.put("ticketEntryTime", tickets.getTicketEntryTime());
            ticketsMap.put("ticketPoliceNo", tickets.getTicketPoliceNo());
            ticketsMap.put("priceCode", tickets.getPriceCode());
            ticketsListMap.add(ticketsMap);
        }
        return ticketsListMap;
    }

}
