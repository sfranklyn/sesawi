/*
 * Copyright 2013 Samuel Franklyn <sfranklyn@gmail.com>.
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
import javax.faces.application.FacesMessage;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import sesawi.ejb.dao.OwnersDaoBean;
import sesawi.ejb.dao.RolesDaoBean;
import sesawi.ejb.dao.UsersDaoBean;
import sesawi.ejb.datamodel.OwnersDataModelBean;
import sesawi.ejb.datamodel.RolesDataModelBean;
import sesawi.ejb.datamodel.UsersDataModelBean;
import sesawi.ejb.service.UsersRolesServiceBean;
import sesawi.ejb.service.UsersServiceBean;
import sesawi.jpa.Owners;
import sesawi.jpa.Roles;
import sesawi.jpa.Users;
import sesawi.jpa.UsersRoles;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("users")
@SessionScoped
public class UsersBean implements Serializable {

    private static final long serialVersionUID = -4969194868089495954L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Users users = null;
    private String userPassword1 = null;
    private String userPassword2 = null;
    private String roleName = null;
    private List<SelectItem> roleNameList = null;
    private String ownerName = null;
    private List<SelectItem> ownerNameList = null;
    @Inject
    private VisitBean visit;
    @EJB
    private UsersDataModelBean usersDataModelBean;
    @EJB
    private RolesDataModelBean rolesDataModelBean;
    @EJB
    private OwnersDataModelBean ownersDataModelBean;
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private UsersServiceBean usersServiceBean;
    @EJB
    private RolesDaoBean rolesDaoBean;
    @EJB
    private OwnersDaoBean ownersDaoBean;
    @EJB
    private UsersRolesServiceBean usersRolesServiceBean;

    @PostConstruct
    public void init() {
        if (visit.getIsAdmin()) {
            dataModel.setSelect(UsersDataModelBean.SELECT_ALL);
            dataModel.setSelectCount(UsersDataModelBean.SELECT_ALL_COUNT);
            dataModel.setSelectParam(null);
            dataModel.setWrappedData(usersDataModelBean);
        }
        users = new Users();
    }

    public void findUser() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String userId = req.getParameter("userId");
        if (userId != null) {
            Integer userIdInt = Integer.valueOf(userId);
            users = usersDaoBean.find(userIdInt);
        }
    }

    public String changePassword() {
        users = visit.getUsers();
        userPassword1 = "";
        userPassword2 = "";
        return "/secure/change_password?faces-redirect=true";
    }

    public String saveChangePassword() {
        String result = "/secure/change_password";

        List<String> errorList = usersServiceBean.saveChangePassword(visit.getUsers(),
                userPassword1, userPassword2, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            visit.setUsers(users);
            result = "/secure/index?faces-redirect=true";
        }

        return result;
    }

    public String create() {
        users = new Users();
        userPassword1 = "";
        userPassword2 = "";
        return "/secure/usersCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/usersCreate";

        if (ownerName != null) {
            Owners owners = ownersDaoBean.selectByOwnerName(ownerName);
            users.setOwners(owners);
        }
        List<String> errorList = usersServiceBean.saveCreate(users,
                userPassword1, userPassword2, roleName, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            UsersRoles usersRoles = new UsersRoles();
            usersRoles.setUsers(users);
            Roles roles = rolesDaoBean.selectByRoleName(roleName);
            usersRoles.setRoles(roles);
            errorList = usersRolesServiceBean.saveCreate(usersRoles, visit.getLocale());
            if (errorList.size() > 0) {
                for (String error : errorList) {
                    FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    error, ""));
                }
            } else {
                create();
                result = "/secure/users?faces-redirect=true";
            }
        }
        return result;
    }

    public String saveUpdate() {
        String result = "/secure/usersUpdate";
        List<String> errorList = usersServiceBean.saveUpdate(users, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/users?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/usersDelete";

        List<String> errorList = usersServiceBean.saveDelete(users, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/users?faces-redirect=true";
        }

        return result;
    }

    public List<SelectItem> getRoleNameList() {
        roleNameList = new ArrayList<>();
        List roleList;

        roleList = rolesDataModelBean.getAll(RolesDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        for (Object roleItem : roleList) {
            Roles roles = (Roles) roleItem;
            SelectItem selectItem = new SelectItem(roles.getRoleName());
            roleNameList.add(selectItem);
        }

        return roleNameList;
    }

    public List<SelectItem> getOwnerNameList() {
        ownerNameList = new ArrayList<>();
        List ownerList;

        ownerList = ownersDataModelBean.getAll(OwnersDataModelBean.SELECT_ALL, null, 0, Short.MAX_VALUE);
        ownerNameList.add(new SelectItem());
        for (Object ownerItem : ownerList) {
            Owners owners = (Owners) ownerItem;
            SelectItem selectItem = new SelectItem(owners.getOwnerName());
            ownerNameList.add(selectItem);
        }

        return ownerNameList;
    }

    public DatabaseDataModel getDataModel() {
        return dataModel;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public Integer getFastStep() {
        return fastStep;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getUserPassword1() {
        return userPassword1;
    }

    public void setUserPassword1(String userPassword1) {
        this.userPassword1 = userPassword1;
    }

    public String getUserPassword2() {
        return userPassword2;
    }

    public void setUserPassword2(String userPassword2) {
        this.userPassword2 = userPassword2;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

}
