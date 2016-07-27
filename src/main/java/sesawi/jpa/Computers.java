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
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Entity
@Table(name = "computers", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"computer_name"})})
@NamedQueries({
    @NamedQuery(name = "Computers.selectAll", 
            query = "SELECT c FROM Computers c"),
    @NamedQuery(name = "Computers.selectAllCount", 
            query = "SELECT COUNT(c) FROM Computers c"),
    @NamedQuery(name = "Computers.selectByComputerName",
            query = "SELECT c FROM Computers c WHERE "
                    + "c.computerName = :computerName")
})
public class Computers extends Base implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "computers")
    private Collection<Shifts> shiftsCollection;

    private static final long serialVersionUID = 7943065042522091755L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "computer_id", nullable = false)
    private Integer computerId;
    @Basic(optional = false)
    @Column(name = "computer_name", length = 50)
    private String computerName;
    @Column(name = "computer_desc", length = 200)
    private String computerDesc;
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    @ManyToOne
    private Locations locations;

    public Computers() {
    }

    public Integer getComputerId() {
        return computerId;
    }

    public void setComputerId(Integer computerId) {
        this.computerId = computerId;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }

    public String getComputerDesc() {
        return computerDesc;
    }

    public void setComputerDesc(String computerDesc) {
        this.computerDesc = computerDesc;
    }
    
    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public Collection<Shifts> getShiftsCollection() {
        return shiftsCollection;
    }

    public void setShiftsCollection(Collection<Shifts> shiftsCollection) {
        this.shiftsCollection = shiftsCollection;
    }
}
