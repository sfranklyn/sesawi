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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import sesawi.jpa.Owners;
import sesawi.jpa.Tickets;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Stateless
public class TicketsDaoBean {

    @PersistenceContext
    private EntityManager em;

    public void insert(Tickets tickets) {
        em.persist(tickets);
        em.flush();
    }

    public void delete(String ticketId) {
        Tickets tickets = em.find(Tickets.class, ticketId);
        em.remove(tickets);
        em.flush();
    }

    public void update(Tickets tickets) {
        em.merge(tickets);
        em.flush();
    }

    public Tickets find(String ticketId) {
        return em.find(Tickets.class, ticketId);
    }

    public List<Tickets> selectAll() {
        Query query = em.createNamedQuery("Tickets.selectAll");
        try {
            List<Tickets> ticketsList = query.getResultList();
            if (ticketsList != null) {
                return ticketsList;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<Tickets> selectByLocationName(String locationName) {
        Query query = em.createNamedQuery("Tickets.selectByLocationName");
        query.setParameter("locationName", locationName);
        try {
            List<Tickets> ticketsList = query.getResultList();
            if (ticketsList != null) {
                return ticketsList;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<Tickets> selectByCondition(
            Owners owners,
            String locationName,
            String userName,
            Date startTicketEntryTime, Date endTicketEntryTime) {
        String select = "SELECT t FROM Tickets t ";
        String criteria = "";
        if (owners != null) {
            criteria = "t.ownerName = :ownerName ";
        }
        if (locationName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("t.locationName = :locationName ");
        }
        if (userName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("t.userName = :userName ");
        }
        if (startTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("t.ticketEntryTime >= :startTicketEntryTime ");
        }
        if (endTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("t.ticketEntryTime <= :endTicketEntryTime ");
        }
        if (!criteria.isEmpty()) {
            select = select.concat("WHERE ").concat(criteria);
        }
        select = select.concat("ORDER BY t.ticketNo");

        Query query = em.createQuery(select);

        if (owners != null) {
            query.setParameter("ownerName", owners.getOwnerName());
        }
        if (locationName != null) {
            query.setParameter("locationName", locationName);
        }
        if (userName != null) {
            query.setParameter("userName", userName);
        }
        if (startTicketEntryTime != null) {
            query.setParameter("startTicketEntryTime", startTicketEntryTime);
        }
        if (endTicketEntryTime != null) {
            query.setParameter("endTicketEntryTime", endTicketEntryTime);
        }

        try {
            List<Tickets> ticketsList = query.getResultList();
            if (ticketsList != null) {
                return ticketsList;
            }
        } catch (NoResultException ex) {
        }

        return null;
    }

    public BigDecimal sumByCondition(
            Owners owners,
            String locationName,
            String userName,
            Date startTicketEntryTime, Date endTicketEntryTime) {
        String sum = "select SUM(ticket_price) FROM tickets ";
        String criteria = "";

        if (owners != null) {
            criteria = "owner_name = :ownerName ";
        }
        if (locationName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("location_name = :locationName ");
        }
        if (userName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("user_name = :userName ");
        }
        if (startTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("ticket_entry_time >= :startTicketEntryTime ");
        }
        if (endTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("ticket_entry_time <= :endTicketEntryTime ");
        }
        if (!criteria.isEmpty()) {
            sum = sum.concat("WHERE ").concat(criteria);
        }

        Query query = em.createNativeQuery(sum);

        if (owners != null) {
            query.setParameter("ownerName", owners.getOwnerName());
        }
        if (locationName != null) {
            query.setParameter("locationName", locationName);
        }
        if (userName != null) {
            query.setParameter("userName", userName);
        }
        if (startTicketEntryTime != null) {
            query.setParameter("startTicketEntryTime", startTicketEntryTime);
        }
        if (endTicketEntryTime != null) {
            query.setParameter("endTicketEntryTime", endTicketEntryTime);
        }

        BigDecimal sumBD = (BigDecimal) query.getSingleResult();
        return sumBD;
    }

    public BigInteger countByCondition(
            Owners owners,
            String locationName,
            String userName,
            Date startTicketEntryTime, Date endTicketEntryTime) {
        String count = "select COUNT(*) FROM tickets ";
        String criteria = "";

        if (owners != null) {
            criteria = "owner_name = :ownerName ";
        }
        if (locationName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("location_name = :locationName ");
        }
        if (userName != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("user_name = :userName ");
        }
        if (startTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("ticket_entry_time >= :startTicketEntryTime ");
        }
        if (endTicketEntryTime != null) {
            if (!criteria.isEmpty()) {
                criteria = criteria.concat("AND ");
            }
            criteria = criteria.concat("ticket_entry_time <= :endTicketEntryTime ");
        }
        if (!criteria.isEmpty()) {
            count = count.concat("WHERE ").concat(criteria);
        }

        Query query = em.createNativeQuery(count);

        if (owners != null) {
            query.setParameter("ownerName", owners.getOwnerName());
        }
        if (locationName != null) {
            query.setParameter("locationName", locationName);
        }
        if (userName != null) {
            query.setParameter("userName", userName);
        }
        if (startTicketEntryTime != null) {
            query.setParameter("startTicketEntryTime", startTicketEntryTime);
        }
        if (endTicketEntryTime != null) {
            query.setParameter("endTicketEntryTime", endTicketEntryTime);
        }

        BigInteger countBI = (BigInteger) query.getSingleResult();
        return countBI;
    }

    public BigDecimal sumAll() {
        Query query = em.createNamedQuery("Tickets.sumAll");
        BigDecimal sum = (BigDecimal) query.getSingleResult();
        return sum;
    }

    public List<Tickets> selectByTicketNo(String locationName, String ticketNo) {
        Query query = em.createNamedQuery("Tickets.selectByTicketNo");
        query.setParameter("locationName", locationName);
        query.setParameter("ticketNo", ticketNo);
        try {
            List<Tickets> ticketsList = query.getResultList();
            if (ticketsList != null) {
                return ticketsList;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

    public List<Tickets> selectByTicketPoliceNo(String locationName, String ticketPoliceNo) {
        Query query = em.createNamedQuery("Tickets.selectByTicketPoliceNo");
        query.setParameter("locationName", locationName);
        query.setParameter("ticketPoliceNo", ticketPoliceNo);
        try {
            List<Tickets> ticketsList = query.getResultList();
            if (ticketsList != null) {
                return ticketsList;
            }
        } catch (NoResultException ex) {
        }
        return null;
    }

}
