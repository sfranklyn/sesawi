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
package sesawi.jpa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Entity
@Table(name = "tickets")
@NamedQueries({
    @NamedQuery(name = "Tickets.selectAll",
            query = "SELECT t FROM Tickets t "
            + "ORDER BY t.ticketNo"),
    @NamedQuery(name = "Tickets.selectAllCount",
            query = "SELECT COUNT(t) FROM Tickets t"),
    @NamedQuery(name = "Tickets.selectByLocationName",
            query = "SELECT t FROM Tickets t "
            + "WHERE "
            + "t.locationName = :locationName AND "
            + "t.ticketExitTime IS NULL "
            + "ORDER BY t.ticketNo"),
    @NamedQuery(name = "Tickets.selectByTicketNo",
            query = "SELECT t FROM Tickets t "
            + "WHERE "
            + "t.locationName = :locationName AND "
            + "t.ticketNo LIKE :ticketNo AND "
            + "t.ticketExitTime IS NULL "
            + "ORDER BY t.ticketNo"),
    @NamedQuery(name = "Tickets.selectByTicketPoliceNo",
            query = "SELECT t FROM Tickets t "
            + "WHERE "
            + "t.locationName = :locationName AND "
            + "t.ticketPoliceNo LIKE :ticketPoliceNo AND "
            + "t.ticketExitTime IS NULL "
            + "ORDER BY t.ticketNo")
})
@NamedNativeQueries({
    @NamedNativeQuery(name = "Tickets.sumAll",
            query = "SELECT SUM(ticket_price) FROM tickets ")
})
public class Tickets extends Base implements Serializable {

    private static final long serialVersionUID = 2465520395791946372L;
    @Transient
    private final DecimalFormat df = new DecimalFormat("###,###,###");
    @Id
    @Column(name = "ticket_id", length = 32)
    private String ticketId;
    @Column(name = "ticket_no", length = 13)
    private String ticketNo;
    @Column(name = "owner_name", length = 30)
    private String ownerName;
    @Column(name = "location_name", length = 30)
    private String locationName;
    @Column(name = "computer_name", length = 50)
    private String computerName;
    @Column(name = "user_name", length = 50)
    private String userName;
    @Column(name = "ticket_entry_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ticketEntryTime;
    @Column(name = "ticket_exit_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ticketExitTime;
    @Column(name = "ticket_police_no", length = 15)
    private String ticketPoliceNo;
    @Column(name = "price_code", length = 5)
    private String priceCode;
    @Column(name = "price_entry", precision = 5, scale = 0)
    private BigDecimal priceEntry;
    @Column(name = "price_entry_hour", precision = 1, scale = 0)
    private BigDecimal priceEntryHour;
    @Column(name = "price_per_hour", precision = 5, scale = 0)
    private BigDecimal pricePerHour;
    @Column(name = "price_lost", precision = 5, scale = 0)
    private BigDecimal priceLost;
    @Column(name = "ticket_duration", precision = 3, scale = 0)
    private BigDecimal ticketDuration;
    @Column(name = "ticket_lost")
    private boolean ticketLost;
    @Column(name = "ticket_price", precision = 9, scale = 0)
    private BigDecimal ticketPrice;

    public Tickets() {
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getTicketEntryTime() {
        return ticketEntryTime;
    }

    public void setTicketEntryTime(Date ticketEntryTime) {
        this.ticketEntryTime = ticketEntryTime;
    }

    public Date getTicketExitTime() {
        return ticketExitTime;
    }

    public void setTicketExitTime(Date ticketExitTime) {
        this.ticketExitTime = ticketExitTime;
    }

    public String getTicketPoliceNo() {
        return ticketPoliceNo;
    }

    public void setTicketPoliceNo(String ticketPoliceNo) {
        this.ticketPoliceNo = ticketPoliceNo;
    }

    public String getPriceCode() {
        return priceCode;
    }

    public void setPriceCode(String priceCode) {
        this.priceCode = priceCode;
    }

    public BigDecimal getPriceEntry() {
        return priceEntry;
    }

    public void setPriceEntry(BigDecimal priceEntry) {
        this.priceEntry = priceEntry;
    }

    public BigDecimal getPriceEntryHour() {
        return priceEntryHour;
    }

    public void setPriceEntryHour(BigDecimal priceEntryHour) {
        this.priceEntryHour = priceEntryHour;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public BigDecimal getPriceLost() {
        return priceLost;
    }

    public void setPriceLost(BigDecimal priceLost) {
        this.priceLost = priceLost;
    }

    public BigDecimal getTicketDuration() {
        return ticketDuration;
    }

    public void setTicketDuration(BigDecimal ticketDuration) {
        this.ticketDuration = ticketDuration;
    }

    public boolean getTicketLost() {
        return ticketLost;
    }

    public void setTicketLost(boolean ticketLost) {
        this.ticketLost = ticketLost;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketPriceStr() {
        if (this.ticketPrice != null) {
            return df.format(this.ticketPrice);
        }
        return "";
    }
}
