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

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sesawi.jpa.Locations;
import sesawi.jpa.Owners;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class LocationsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Locations locations) {
        em.persist(locations);
        em.flush();
    }

    public void delete(Integer locationId) {
        Locations locations = em.find(Locations.class, locationId);
        em.remove(locations);
        em.flush();
    }

    public void update(Locations locations) {
        em.merge(locations);
        em.flush();
    }

    public Locations find(Integer locationId) {
        return em.find(Locations.class, locationId);
    }

    public Locations selectByLocationName(String locationName) {
        Query query = em.createNamedQuery("Locations.selectByLocationName");
        query.setParameter("locationName", locationName);
        try {
            Locations locations = (Locations) query.getSingleResult();
            if (locations != null) {
                return locations;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<Locations> selectByOwners(Owners owners) {
        Query query = em.createNamedQuery("Locations.selectByOwners");
        query.setParameter("owners", owners);
        try {
            List<Locations> locationsList = (List) query.getResultList();
            if (locationsList != null) {
                return locationsList;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }
}
