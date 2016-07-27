/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
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
package sesawi.jsf.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import sesawi.ejb.dao.LocationsDaoBean;
import sesawi.ejb.dao.PricesDaoBean;
import sesawi.ejb.dao.TicketsDaoBean;
import sesawi.ejb.datamodel.ComputersDataModelBean;
import sesawi.ejb.datamodel.LocationsDataModelBean;
import sesawi.ejb.datamodel.PricesDataModelBean;
import sesawi.ejb.datamodel.UsersDataModelBean;
import sesawi.ejb.service.TicketsServiceBean;
import sesawi.jpa.Computers;
import sesawi.jpa.Locations;
import sesawi.jpa.Prices;
import sesawi.jpa.Tickets;
import sesawi.jpa.Users;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("tickets")
@SessionScoped
public class TicketsBean implements Serializable {

    private static final long serialVersionUID = -6514366669834651448L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private DataModel dataModel = new DatabaseDataModel();
    private final DateTimeFormatter dtfTicket = DateTimeFormat.
            forPattern("ddMMMyyHHmmss");
    private Tickets tickets = null;
    private List<SelectItem> locationNameList = null;
    private List<SelectItem> computerNameList = null;
    private List<SelectItem> userNameList = null;
    private List<SelectItem> priceCodeList = null;
    private String locationName;
    private String userName;
    private Date startTicketEntryTime;
    private Date endTicketEntryTime;
    private BigDecimal sumBd;
    private String sum;
    private BigInteger countBi;
    private String count;
    private final DecimalFormat df = new DecimalFormat("###,###,###");
    @Inject
    private VisitBean visit;
    @EJB
    private TicketsDaoBean ticketsDaoBean;
    @EJB
    private PricesDaoBean pricesDaoBean;
    @EJB
    private LocationsDaoBean locationsDaoBean;
    @EJB
    private TicketsServiceBean ticketsServiceBean;
    @EJB
    private LocationsDataModelBean locationsDataModelBean;
    @EJB
    private ComputersDataModelBean computersDataModelBean;
    @EJB
    private PricesDataModelBean pricesDataModelBean;

    public String select() {
        List<Tickets> listTickets = ticketsDaoBean.selectByCondition(
                visit.getUsers().getOwners(),
                locationName,
                userName,
                startTicketEntryTime, endTicketEntryTime);
        dataModel = new ListDataModel(listTickets);
        sumBd = ticketsDaoBean.sumByCondition(
                visit.getUsers().getOwners(),
                locationName,
                userName,
                startTicketEntryTime, endTicketEntryTime);
        sum = df.format(BigDecimal.ZERO);
        if (null != sumBd) {
            sum = df.format(sumBd);
        }
        countBi = ticketsDaoBean.countByCondition(
                visit.getUsers().getOwners(),
                locationName,
                userName,
                startTicketEntryTime, endTicketEntryTime);
        count = df.format(BigDecimal.ZERO);
        if (null != countBi) {
            count = df.format(countBi);
        }
        return "/secure/tickets?faces-redirect=true";
    }

    public void findTicket() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String ticketId = req.getParameter("ticketId");
        if (ticketId != null) {
            tickets = ticketsDaoBean.find(ticketId);
        }
    }

    public String create() {
        tickets = new Tickets();
        DateTime now = DateTime.now();
        tickets.setTicketNo(dtfTicket.print(now).toUpperCase());
        tickets.setTicketEntryTime(now.toDate());
        tickets.setTicketDuration(BigDecimal.ZERO);
        tickets.setTicketPrice(BigDecimal.ZERO);

        List pricesList = pricesDataModelBean.getAll(PricesDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        if (pricesList.size() > 0) {
            Prices priceFirst = (Prices) pricesList.get(0);
            tickets.setPriceEntry(priceFirst.getPriceEntry());
            tickets.setPriceEntryHour(priceFirst.getPriceEntryHour());
            tickets.setPricePerHour(priceFirst.getPricePerHour());
            tickets.setPriceLost(priceFirst.getPriceLost());
        }

        return "/secure/ticketsCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/ticketsCreate";

        tickets.setTicketId(UUID.randomUUID().toString().replaceAll("-", ""));
        List<String> errorList = ticketsServiceBean.saveCreate(tickets, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = select();
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/ticketsUpdate";

        List<String> errorList = ticketsServiceBean.saveUpdate(tickets, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = select();
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/ticketsDelete";

        List<String> errorList = ticketsServiceBean.saveDelete(tickets, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = select();
        }

        return result;
    }

    public List<SelectItem> getLocationNameList() {
        locationNameList = new ArrayList<>();
        List locationsList;

        if (visit.getUsers().getOwners() == null) {
            locationsList = locationsDataModelBean.getAll(LocationsDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        } else {
            locationsList = locationsDaoBean.selectByOwners(visit.getUsers().getOwners());
        }
        locationNameList.add(new SelectItem());
        for (Object locationsList1 : locationsList) {
            Locations locations = (Locations) locationsList1;
            SelectItem selectItem = new SelectItem(locations.getLocationName());
            locationNameList.add(selectItem);
        }

        return locationNameList;
    }

    public List<SelectItem> getComputerNameList() {
        computerNameList = new ArrayList<>();
        List computersList;

        computersList = computersDataModelBean.getAll(ComputersDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object computersList1 : computersList) {
            Computers computers = (Computers) computersList1;
            SelectItem selectItem = new SelectItem(computers.getComputerName());
            computerNameList.add(selectItem);
        }

        return computerNameList;
    }

    public List<SelectItem> getUserNameList() {
        userNameList = new ArrayList<>();
        List usersList;

        usersList = computersDataModelBean.getAll(UsersDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        userNameList.add(new SelectItem());
        for (Object usersList1 : usersList) {
            Users users = (Users) usersList1;
            SelectItem selectItem = new SelectItem(users.getUserName());
            userNameList.add(selectItem);
        }

        return userNameList;
    }

    public List<SelectItem> getPriceCodeList() {
        priceCodeList = new ArrayList<>();
        List pricesList;

        pricesList = pricesDataModelBean.getAll(PricesDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object pricesList1 : pricesList) {
            Prices prices = (Prices) pricesList1;
            SelectItem selectItem = new SelectItem(prices.getPriceCode());
            priceCodeList.add(selectItem);
        }

        return priceCodeList;
    }

    public void priceCodeSelect() {
        Prices prices = pricesDaoBean.selectByPriceCode(tickets.getPriceCode());
        tickets.setPriceEntry(prices.getPriceEntry());
        tickets.setPriceEntryHour(prices.getPriceEntryHour());
        tickets.setPricePerHour(prices.getPricePerHour());
        tickets.setPriceLost(prices.getPriceLost());
        ticketExitTimeSelect();
    }

    public void ticketExitTimeSelect() {
        tickets.setTicketPrice(new BigDecimal(ticketsServiceBean.getPrice(tickets)));
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public Tickets getTickets() {
        return tickets;
    }

    public void setTickets(Tickets tickets) {
        this.tickets = tickets;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

    public Date getStartTicketEntryTime() {
        return startTicketEntryTime;
    }

    public void setStartTicketEntryTime(Date startTicketEntryTime) {
        this.startTicketEntryTime = startTicketEntryTime;
    }

    public Date getEndTicketEntryTime() {
        return endTicketEntryTime;
    }

    public void setEndTicketEntryTime(Date endTicketEntryTime) {
        this.endTicketEntryTime = endTicketEntryTime;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
