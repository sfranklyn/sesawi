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
import sesawi.ejb.dao.ComputersDaoBean;
import sesawi.ejb.dao.LocationsDaoBean;
import sesawi.jpa.Computers;
import sesawi.jpa.Locations;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ComputersServiceBean {

    private static final Logger log = Logger.getLogger(ComputersServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private ComputersDaoBean computersDaoBean;
    @EJB
    private LocationsDaoBean locationsDaoBean;

    public List<String> saveCreate(Computers computers, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(computers.getComputerName())) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if (errorList.isEmpty()) {
            try {
                Locations locations = locationsDaoBean.selectByLocationName(
                        computers.getLocations().getLocationName());
                computers.setLocations(locations);
                computersDaoBean.insert(computers);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    log.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("computer_name_already_registered"));
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

    public List<String> saveUpdate(Computers computers, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (computers.getComputerId() == null || computers.getComputerId() == 0) {
            errorList.add(messageSource.getString("computer_id_required"));
        }
        if ("".equals(computers.getComputerName())) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if (errorList.isEmpty()) {
            try {
                Locations locations = locationsDaoBean.selectByLocationName(
                        computers.getLocations().getLocationName());
                computers.setLocations(locations);
                computersDaoBean.update(computers);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Computers computers, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (computers.getComputerId() == null || computers.getComputerId() == 0) {
            errorList.add(messageSource.getString("computer_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                computersDaoBean.delete(computers.getComputerId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                log.severe(ex.toString());
            }
        }
        return errorList;
    }
}
