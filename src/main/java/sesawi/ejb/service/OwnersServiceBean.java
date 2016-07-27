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

package sesawi.ejb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.ejb.dao.OwnersDaoBean;
import sesawi.jpa.Owners;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class OwnersServiceBean {

    private static final Logger log = Logger.getLogger(OwnersServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private OwnersDaoBean ownersDaoBean;

    public List<String> saveCreate(Owners owners, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(owners.getOwnerName())) {
            errorList.add(messageSource.getString("owner_name_required"));
        }
        if (errorList.isEmpty()) {
            try {
                ownersDaoBean.insert(owners);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("owner_name_already_registered"));
                        duplicate = true;
                        break;
                    }
                    cause = cause.getCause();
                }
                if (!duplicate) {
                    errorList.add(ex.toString());
                    log.severe(ex.toString());
                }
            }
        }
        return errorList;
    }

    public List<String> saveUpdate(Owners owners, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (owners.getOwnerId() == null || owners.getOwnerId() == 0) {
            errorList.add(messageSource.getString("owner_id_required"));
        }
        if ("".equals(owners.getOwnerName())) {
            errorList.add(messageSource.getString("owner_name_required"));
        }
        if (errorList.isEmpty()) {
            try {
                ownersDaoBean.update(owners);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Owners owners, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (owners.getOwnerId() == null || owners.getOwnerId() == 0) {
            errorList.add(messageSource.getString("owner_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                ownersDaoBean.delete(owners.getOwnerId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
