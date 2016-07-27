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
package sesawi.ejb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.ejb.dao.ComputersDaoBean;
import sesawi.ejb.dao.UsersDaoBean;
import sesawi.ejb.dao.UsersRolesDaoBean;
import sesawi.jpa.Users;
import sesawi.jpa.UsersRoles;
import sesawi.ejb.dao.ShiftsDaoBean;
import sesawi.jpa.Computers;
import sesawi.jpa.Shifts;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersServiceBean {

    private static final Logger LOG = Logger.getLogger(UsersServiceBean.class.getName());
    private static final String MESSAGES = "ejbmessages";
    @EJB
    private UsersDaoBean usersDaoBean;
    @EJB
    private AppServiceBean appServiceBean;
    @EJB
    private UsersRolesDaoBean usersRolesDaoBean;
    @EJB
    private ComputersDaoBean computersDaoBean;
    @EJB
    private ShiftsDaoBean shiftsDaoBean;

    public List<String> logIn(String userName, String userPassword,
            String computerName, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if (computerName == null) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if ("".equals(computerName)) {
            errorList.add(messageSource.getString("computer_name_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }
        Users users = usersDaoBean.selectByUserName(userName);
        if (users == null) {
            errorList.add(messageSource.getString("user_name_not_registered"));
            return errorList;
        }
        if (!appServiceBean.hashPassword(userPassword).equals(users.getUserPassword())) {
            errorList.add(messageSource.getString("user_password_not_match"));
        }

        Computers computers = computersDaoBean.selectByComputerName(computerName);
        if (computers == null) {
            errorList.add(messageSource.getString("computer_name_not_registered"));
            return errorList;
        }

        if (!isShiftOpen(users, computers)) {
            errorList.add(messageSource.getString("shift_not_open"));
            return errorList;
        }

        return errorList;
    }

    public List<String> openShift(String userName, String userPassword,
            String computerName, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if (computerName == null) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if ("".equals(computerName)) {
            errorList.add(messageSource.getString("computer_name_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }

        Users users = usersDaoBean.selectByUserName(userName);
        if (users == null) {
            errorList.add(messageSource.getString("user_name_not_registered"));
            return errorList;
        }
        if (!appServiceBean.hashPassword(userPassword).equals(users.getUserPassword())) {
            errorList.add(messageSource.getString("user_password_not_match"));
        }

        Computers computers = computersDaoBean.selectByComputerName(computerName);
        if (computers == null) {
            errorList.add(messageSource.getString("computer_name_not_registered"));
            return errorList;
        }

        if (isShiftOpen(users, computers)) {
            errorList.add(messageSource.getString("shift_already_open"));
            return errorList;
        }

        Shifts shiftsNew = new Shifts();
        shiftsNew.setUsers(users);
        shiftsNew.setComputers(computers);
        shiftsNew.setShiftStart(new Date());
        shiftsDaoBean.insert(shiftsNew);

        return errorList;
    }

    public List<String> closeShift(String userName, String userPassword,
            String computerName, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (userName == null) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if ("".equals(userName)) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (userPassword == null) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword)) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if (computerName == null) {
            errorList.add(messageSource.getString("computer_name_required"));
        }
        if ("".equals(computerName)) {
            errorList.add(messageSource.getString("computer_name_required"));
        }

        if (errorList.size() > 0) {
            return errorList;
        }

        Users users = usersDaoBean.selectByUserName(userName);
        if (users == null) {
            errorList.add(messageSource.getString("user_name_not_registered"));
            return errorList;
        }
        if (!appServiceBean.hashPassword(userPassword).equals(users.getUserPassword())) {
            errorList.add(messageSource.getString("user_password_not_match"));
        }

        Computers computers = computersDaoBean.selectByComputerName(computerName);
        if (computers == null) {
            errorList.add(messageSource.getString("computer_name_not_registered"));
            return errorList;
        }

        if (!isShiftOpen(users, computers)) {
            errorList.add(messageSource.getString("shift_not_open"));
            return errorList;
        }

        Shifts shifts = shiftsDaoBean.selectByUserAndComputer(users, computers);
        shifts.setShiftEnd(new Date());
        shiftsDaoBean.update(shifts);

        Shifts shiftsNew = new Shifts();
        shiftsNew.setUsers(users);
        shiftsNew.setComputers(computers);
        shiftsDaoBean.insert(shiftsNew);

        return errorList;
    }

    private boolean isShiftOpen(Users users, Computers computers) {
        Shifts shifts = shiftsDaoBean.selectByUserAndComputer(users, computers);
        if (shifts == null) {
            return false;
        }

        return shifts.getShiftStart() != null;
    }

    public List<String> saveChangePassword(Users users,
            String userPassword1, String userPassword2, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        errorList = this.checkPassword(userPassword1, userPassword2, errorList, messageSource);

        if (errorList.isEmpty()) {
            try {
                users.setUserPassword(appServiceBean.hashPassword(userPassword1));
                users.setUserLastPwdChange(new Date());
                usersDaoBean.update(users);
            } catch (Exception ex) {
                LOG.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveCreate(Users users,
            String userPassword1, String userPassword2, String roleName,
            Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if ("".equals(users.getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }
        if (roleName.equals("OWN") && (users.getOwners() == null)) {
            errorList.add(messageSource.getString("owner_name_required"));
        }

        errorList = this.checkPassword(userPassword1, userPassword2, errorList, messageSource);

        if (errorList.isEmpty()) {
            try {
                users.setUserPassword(appServiceBean.hashPassword(userPassword1));
                users.setUserLastPwdChange(new Date());
                usersDaoBean.insert(users);
            } catch (Exception ex) {
                Throwable cause = ex.getCause();
                boolean duplicate = false;
                while (cause != null) {
                    LOG.log(Level.SEVERE, cause.toString(), cause);
                    if (cause.toString().contains("Duplicate entry")) {
                        errorList.add(messageSource.getString("user_name_already_registered"));
                        duplicate = true;
                        break;
                    }
                    cause = cause.getCause();
                }
                if (!duplicate) {
                    errorList.add(ex.toString());
                    LOG.severe(ex.toString());
                }
            }
        }
        return errorList;
    }

    private List<String> checkPassword(String userPassword1, String userPassword2, List<String> errorList, ResourceBundle messageSource) {

        if ("".equals(userPassword1)) {
            errorList.add(messageSource.getString("user_password_required"));
        }
        if ("".equals(userPassword2)) {
            errorList.add(messageSource.getString("reconfirm_user_password_required"));
        }

        if (!userPassword1.equals(userPassword2)) {
            errorList.add(messageSource.getString("user_password_reconfirm_not_match"));
        } else if (userPassword1.length() < 5) {
            errorList.add(messageSource.getString("user_password_not_long_enough"));
        }

        return errorList;
    }

    public List<String> saveUpdate(Users users, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (users.getUserId() == null || users.getUserId() == 0) {
            errorList.add(messageSource.getString("user_id_required"));
        }
        if ("".equals(users.getUserName())) {
            errorList.add(messageSource.getString("user_name_required"));
        }

        if (errorList.isEmpty()) {
            try {
                usersDaoBean.update(users);
            } catch (Exception ex) {
                errorList.add(ex.toString());
                LOG.severe(ex.toString());
            }
        }
        return errorList;
    }

    public List<String> saveDelete(Users users, Locale locale) {
        List<String> errorList = new ArrayList<>();
        ResourceBundle messageSource = ResourceBundle.getBundle(MESSAGES, locale);
        if (users.getUserId() == null || users.getUserId() == 0) {
            errorList.add(messageSource.getString("user_id_required"));
        }
        if (errorList.isEmpty()) {
            try {
                List<UsersRoles> usersRolesList = usersRolesDaoBean.selectByUserId(users.getUserId());
                usersRolesList.stream().forEach((usersRoles) -> {
                    usersRolesDaoBean.delete(usersRoles.getUsersRolesPK());
                });

                usersDaoBean.delete(users.getUserId());
            } catch (Exception ex) {
                errorList.add(ex.toString());
                LOG.severe(ex.toString());
            }
        }
        return errorList;
    }
}
