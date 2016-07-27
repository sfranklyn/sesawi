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
package sesawi.xmlrpc;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.api.UsersRpc;
import sesawi.ejb.service.UsersServiceBean;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class UsersRpcBean implements UsersRpc {

    private static final Logger LOG = Logger.getLogger(UsersRpcBean.class.getName());
    @EJB
    private UsersServiceBean usersServiceBean;

    @Override
    public List<String> logIn(String userName, String userPassword,
            String computerName) {
        return usersServiceBean.logIn(userName, userPassword, 
                computerName, Locale.ENGLISH);
    }

    @Override
    public List<String> openShift(String userName, String userPassword,
            String computerName) {
        return usersServiceBean.openShift(userName, userPassword,
                computerName, Locale.ENGLISH);
    }

    @Override
    public List<String> closeShift(String userName, String userPassword, 
            String computerName) {
        return usersServiceBean.closeShift(userName, userPassword,
                computerName, Locale.ENGLISH);
    }

}
