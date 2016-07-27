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
package sesawi.ejb.dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sesawi.jpa.Computers;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ComputersDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Computers computers) {
        em.persist(computers);
        em.flush();
    }

    public void delete(Integer computerId) {
        Computers computers = em.find(Computers.class, computerId);
        em.remove(computers);
        em.flush();
    }

    public void update(Computers computers) {
        em.merge(computers);
        em.flush();
    }

    public Computers find(Integer computerId) {
        return em.find(Computers.class, computerId);
    }

    public Computers selectByComputerName(String computerName) {
        Query query = em.createNamedQuery("Computers.selectByComputerName");
        query.setParameter("computerName", computerName);
        try {
            Computers computers = (Computers) query.getSingleResult();
            if (computers != null) {
                return computers;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }
}
