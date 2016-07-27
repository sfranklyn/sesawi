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
package sesawi.jsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sesawi.ejb.dao.ComputersDaoBean;
import sesawi.ejb.datamodel.ComputersDataModelBean;
import sesawi.ejb.datamodel.LocationsDataModelBean;
import sesawi.ejb.service.ComputersServiceBean;
import sesawi.jpa.Computers;
import sesawi.jpa.Locations;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("computers")
@SessionScoped
public class ComputersBean implements Serializable {

    private static final long serialVersionUID = 6737367934989669484L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Computers computers = null;
    private List<SelectItem> locationNameList = null;
    @Inject
    private VisitBean visit;
    @EJB
    private ComputersDaoBean computersDaoBean;
    @EJB
    private ComputersServiceBean computersServiceBean;
    @EJB
    private ComputersDataModelBean computersDataModelBean;
    @EJB
    private LocationsDataModelBean locationsDataModelBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(ComputersDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(ComputersDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(computersDataModelBean);
        computers = new Computers();
        computers.setLocations(new Locations());
    }

    public void findComputer() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String computerId = req.getParameter("computerId");
        if (computerId != null) {
            Integer computerIdInt = Integer.valueOf(computerId);
            computers = computersDaoBean.find(computerIdInt);
            if (computers.getLocations() == null) {
                computers.setLocations(new Locations());
            }
        }
    }

    public String create() {
        computers = new Computers();
        computers.setLocations(new Locations());
        return "/secure/computersCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/computersCreate";

        List<String> errorList = computersServiceBean.saveCreate(computers, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/computers?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/computersUpdate?computerId=" + computers.getComputerId();

        List<String> errorList = computersServiceBean.saveUpdate(computers, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/computers?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/computersDelete";

        List<String> errorList = computersServiceBean.saveDelete(computers, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/computers?faces-redirect=true";
        }

        return result;
    }

    public List<SelectItem> getLocationNameList() {
        locationNameList = new ArrayList<>();
        List locationList;

        locationList = locationsDataModelBean.getAll(
                LocationsDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object locationList1 : locationList) {
            Locations locations = (Locations) locationList1;
            SelectItem selectItem = new SelectItem(locations.getLocationName());
            locationNameList.add(selectItem);
        }

        return locationNameList;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public DatabaseDataModel getDataModel() {
        return dataModel;
    }

    public Computers getComputers() {
        return computers;
    }

    public void setComputers(Computers computers) {
        this.computers = computers;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
