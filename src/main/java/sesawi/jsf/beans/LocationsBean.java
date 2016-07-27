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
import sesawi.ejb.dao.LocationsDaoBean;
import sesawi.ejb.datamodel.LocationsDataModelBean;
import sesawi.ejb.datamodel.OwnersDataModelBean;
import sesawi.ejb.service.LocationsServiceBean;
import sesawi.jpa.Locations;
import sesawi.jpa.Owners;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("locations")
@SessionScoped
public class LocationsBean implements Serializable {

    private static final long serialVersionUID = -2592240693442856375L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Locations locations = null;
    private List<SelectItem> ownerNameList = null;
    @Inject
    private VisitBean visit;
    @EJB
    private LocationsDaoBean locationsDaoBean;
    @EJB
    private LocationsServiceBean locationsServiceBean;
    @EJB
    private LocationsDataModelBean locationsDataModelBean;
    @EJB
    private OwnersDataModelBean ownersDataModelBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(LocationsDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(LocationsDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(locationsDataModelBean);
        locations = new Locations();
        locations.setOwners(new Owners());
    }

    public void findLocation() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String locationId = req.getParameter("locationId");
        if (locationId != null) {
            Integer locationIdInt = Integer.valueOf(locationId);
            locations = locationsDaoBean.find(locationIdInt);
            if (locations.getOwners() == null) {
                locations.setOwners(new Owners());
            }
        }
    }

    public String create() {
        locations = new Locations();
        locations.setOwners(new Owners());
        return "/secure/locationsCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/locationsCreate";

        List<String> errorList = locationsServiceBean.saveCreate(locations, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/locations?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/locationsUpdate";

        List<String> errorList = locationsServiceBean.saveUpdate(locations, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/locations?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/locationsDelete";

        List<String> errorList = locationsServiceBean.saveDelete(locations, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/locations?faces-redirect=true";
        }

        return result;
    }

    public List<SelectItem> getOwnerNameList() {
        ownerNameList = new ArrayList<>();
        List ownerList;

        ownerList = ownersDataModelBean.getAll(OwnersDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object ownerList1 : ownerList) {
            Owners owners = (Owners) ownerList1;
            SelectItem selectItem = new SelectItem(owners.getOwnerName());
            ownerNameList.add(selectItem);
        }

        return ownerNameList;
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

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
