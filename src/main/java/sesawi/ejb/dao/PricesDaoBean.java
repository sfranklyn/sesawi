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
package sesawi.ejb.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sesawi.jpa.Prices;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Stateless
public class PricesDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Prices prices) {
        em.persist(prices);
        em.flush();
    }

    public void delete(Integer priceId) {
        Prices prices = em.find(Prices.class, priceId);
        em.remove(prices);
        em.flush();
    }

    public void update(Prices prices) {
        em.merge(prices);
        em.flush();
    }

    public Prices find(Integer priceId) {
        return em.find(Prices.class, priceId);
    }

    public List selectAll() {
        Query query = em.createNamedQuery("Prices.selectAll");
        List pricesList = null;
        try {
            pricesList = query.getResultList();
        } catch (NoResultException ex) {
        }
        return pricesList;
    }

    public Prices selectByPriceCode(String priceCode) {
        Query query = em.createNamedQuery("Prices.selectByPriceCode");
        query.setParameter("priceCode", priceCode);
        Prices prices = (Prices) query.getSingleResult();
        return prices;
    }
}
