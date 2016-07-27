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
@Table(name = "owners", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"owner_name"})})
@NamedQueries({
    @NamedQuery(name = "Owners.selectAll", 
            query = "SELECT o FROM Owners o"),
    @NamedQuery(name = "Owners.selectAllCount", 
            query = "SELECT COUNT(o) FROM Owners o"),
    @NamedQuery(name = "Owners.selectByOwnerName",
            query = "SELECT o FROM Owners o WHERE o.ownerName = :ownerName"),
})
public class Owners extends Base implements Serializable {
    private static final long serialVersionUID = -8738411786641279595L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;
    @Basic(optional = false)
    @Column(name = "owner_name", length = 30, nullable = false)
    private String ownerName;
    @Column(name = "owner_desc", length = 200)
    private String ownerDesc;

    public Owners() {
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerDesc() {
        return ownerDesc;
    }

    public void setOwnerDesc(String ownerDesc) {
        this.ownerDesc = ownerDesc;
    }

}
