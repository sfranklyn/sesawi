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
package sesawi.ejb.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sesawi.jpa.Computers;
import sesawi.jpa.Shifts;
import sesawi.jpa.Users;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ShiftsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Shifts shifts) {
        em.persist(shifts);
        em.flush();
    }

    public void delete(Integer shift_id) {
        Shifts shifts = em.find(Shifts.class, shift_id);
        em.remove(shifts);
        em.flush();
    }

    public void update(Shifts shifts) {
        em.merge(shifts);
        em.flush();
    }

    public Shifts find(Integer shift_id) {
        return em.find(Shifts.class, shift_id);
    }

    public Shifts selectByUserAndComputer(Users users, Computers computers) {
        Query query = em.createNamedQuery("Shifts.selectByUserAndComputer");
        query.setParameter("users", users);
        query.setParameter("computers", computers);
        try {
            List<Shifts> shiftsList = query.getResultList();
            if (!shiftsList.isEmpty()) {
                return shiftsList.get(0);
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

}
