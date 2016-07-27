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
@Table(name = "locations", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"location_name"})})
@NamedQueries({
    @NamedQuery(name = "Locations.selectAll", 
            query = "SELECT l FROM Locations l"),
    @NamedQuery(name = "Locations.selectAllCount", 
            query = "SELECT COUNT(l) FROM Locations l"),
    @NamedQuery(name = "Locations.selectByLocationName",
            query = "SELECT l FROM Locations l WHERE "
                    + "l.locationName = :locationName"),
    @NamedQuery(name = "Locations.selectByOwners",
            query = "SELECT l FROM Locations l WHERE "
                    + "l.owners = :owners")
})
public class Locations extends Base implements Serializable {

    private static final long serialVersionUID = -7626409794574854002L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "location_id", nullable = false)
    private Integer locationId;
    @Basic(optional = false)
    @Column(name = "location_name", length = 30)
    private String locationName;
    @Column(name = "location_desc", length = 200)
    private String locationDesc;
    @JoinColumn(name = "owner_id", referencedColumnName = "owner_id")
    @ManyToOne
    private Owners owners;

    public Locations() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationDesc() {
        return locationDesc;
    }

    public void setLocationDesc(String locationDesc) {
        this.locationDesc = locationDesc;
    }

    public Owners getOwners() {
        return owners;
    }

    public void setOwners(Owners owners) {
        this.owners = owners;
    }

}
