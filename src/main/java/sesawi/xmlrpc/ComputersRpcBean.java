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
package sesawi.xmlrpc;

import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.api.ComputersRpc;
import sesawi.ejb.dao.ComputersDaoBean;
import sesawi.jpa.Computers;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class ComputersRpcBean implements ComputersRpc {

    @EJB
    private ComputersDaoBean computersDaoBean;

    @Override
    public Map receiveLocation(String computerName) {
        Computers computers = computersDaoBean.selectByComputerName(computerName);
        Map locationMap = new HashMap();
        if (computers.getLocations() != null) {
            locationMap.put("locationName", computers.getLocations().getLocationName());
            locationMap.put("locationDesc", computers.getLocations().getLocationDesc());
        }
        return locationMap;
    }

}
