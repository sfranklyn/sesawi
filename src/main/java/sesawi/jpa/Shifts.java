/*
 * Copyright 2016 Samuel Franklyn <sfranklyn@gmail.com>.
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
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Entity
@Table(name = "shifts", catalog = "sesawi", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"shift_id", "user_id", "computer_id"})})
@NamedQueries({
    @NamedQuery(name = "Shifts.selectAll",
            query = "SELECT s FROM Shifts s"),
    @NamedQuery(name = "Shifts.selectAllCount",
            query = "SELECT COUNT(c) FROM Shifts c"),
    @NamedQuery(name = "Shifts.selectByUserAndComputer",
            query = "SELECT s FROM Shifts s WHERE "
                    + "s.users = :users AND "
                    + "s.computers = :computers "
                    + "ORDER BY s.shiftId DESC")

})
public class Shifts extends Base implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "shift_id", nullable = false)
    private Integer shiftId;
    @Column(name = "shift_start")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shiftStart;
    @Column(name = "shift_end")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shiftEnd;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @ManyToOne(optional = false)
    private Users users;
    @JoinColumn(name = "computer_id", referencedColumnName = "computer_id", nullable = false)
    @ManyToOne(optional = false)
    private Computers computers;

    public Shifts() {
    }

    public Shifts(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Integer getShiftId() {
        return shiftId;
    }

    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    public Date getShiftStart() {
        return shiftStart;
    }

    public void setShiftStart(Date shiftStart) {
        this.shiftStart = shiftStart;
    }

    public Date getShiftEnd() {
        return shiftEnd;
    }

    public void setShiftEnd(Date shiftEnd) {
        this.shiftEnd = shiftEnd;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Computers getComputers() {
        return computers;
    }

    public void setComputers(Computers computers) {
        this.computers = computers;
    }

}
