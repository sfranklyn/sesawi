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
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sesawi.ejb.dao.OwnersDaoBean;
import sesawi.ejb.datamodel.OwnersDataModelBean;
import sesawi.ejb.service.OwnersServiceBean;
import sesawi.jpa.Owners;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("owners")
@SessionScoped
public class OwnersBean implements Serializable {

    private static final long serialVersionUID = -4665788848693495195L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Owners owners = null;
    @Inject
    private VisitBean visit;
    @EJB
    private OwnersDaoBean ownersDaoBean;
    @EJB
    private OwnersServiceBean ownersServiceBean;
    @EJB
    private OwnersDataModelBean ownersDataModelBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(OwnersDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(OwnersDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(ownersDataModelBean);
        owners = new Owners();
    }

    public void findOwner() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String ownerId = req.getParameter("ownerId");
        if (ownerId != null) {
            Integer ownerIdInt = Integer.valueOf(ownerId);
            owners = ownersDaoBean.find(ownerIdInt);
        }
    }

    public String create() {
        owners = new Owners();
        return "/secure/ownersCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/ownersCreate";

        List<String> errorList = ownersServiceBean.saveCreate(owners, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/owners?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/ownersUpdate?ownerId=" + owners.getOwnerId();

        List<String> errorList = ownersServiceBean.saveUpdate(owners, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/owners?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/ownersDelete";

        List<String> errorList = ownersServiceBean.saveDelete(owners, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/owners?faces-redirect=true";
        }

        return result;
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

    public Owners getOwners() {
        return owners;
    }

    public void setOwners(Owners owners) {
        this.owners = owners;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
