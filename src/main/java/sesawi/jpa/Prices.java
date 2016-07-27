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
import java.math.MathContext;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Entity
@Table(name = "prices", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"price_code"})})
@NamedQueries({
    @NamedQuery(name = "Prices.selectAll",
            query = "SELECT p FROM Prices p"),
    @NamedQuery(name = "Prices.selectAllCount",
            query = "SELECT COUNT(p) FROM Prices p"),
    @NamedQuery(name = "Prices.selectByPriceCode",
            query = "SELECT p FROM Prices p "
            + "WHERE "
            + "p.priceCode = :priceCode")
})
public class Prices extends Base implements Serializable {

    private static final long serialVersionUID = -7882324918624906363L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "price_id", nullable = false)
    private Integer priceId;
    @Basic(optional = false)
    @Column(name = "price_code", length = 5)
    private String priceCode;
    @Basic(optional = false)
    @Column(name = "price_entry", precision = 5, scale = 0)
    private BigDecimal priceEntry;
    @Basic(optional = false)
    @Column(name = "price_entry_hour", precision = 1, scale = 0)
    private BigDecimal priceEntryHour;
    @Basic(optional = false)
    @Column(name = "price_per_hour", precision = 5, scale = 0)
    private BigDecimal pricePerHour;
    @Basic(optional = false)
    @Column(name = "price_lost", precision = 5, scale = 0)
    private BigDecimal priceLost;
    @Column(name = "price_last_change")
    @Temporal(TemporalType.TIMESTAMP)
    private Date priceLastChange;

    public Prices() {
        priceEntry = new BigDecimal(0, new MathContext(5)).setScale(0);
        priceEntryHour = new BigDecimal(0, new MathContext(1)).setScale(0);
        pricePerHour = new BigDecimal(0, new MathContext(5)).setScale(0);
        priceLost = new BigDecimal(0, new MathContext(5)).setScale(0);
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
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

    public Date getPriceLastChange() {
        return priceLastChange;
    }

    public void setPriceLastChange(Date priceLastChange) {
        this.priceLastChange = priceLastChange;
    }

}
