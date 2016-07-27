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
import sesawi.jpa.Owners;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class OwnersDaoBean {

    @PersistenceContext
    private EntityManager em;

    public Owners selectByOwnerName(String ownerName) {
        Query query = em.createNamedQuery("Owners.selectByOwnerName");
        query.setParameter("ownerName", ownerName);
        try {
            Owners owners = (Owners) query.getSingleResult();
            if (owners != null) {
                return owners;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }
    
    public void insert(Owners owners) {
        em.persist(owners);
        em.flush();
    }

    public void delete(Integer ownerId) {
        Owners owners = em.find(Owners.class, ownerId);
        em.remove(owners);
        em.flush();
    }

    public void update(Owners owners) {
        em.merge(owners);
        em.flush();
    }

    public Owners find(Integer ownerId) {
        return em.find(Owners.class, ownerId);
    }}
