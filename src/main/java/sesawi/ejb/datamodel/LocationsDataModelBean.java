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

package sesawi.ejb.datamodel;

import javax.ejb.Stateless;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Stateless
public class LocationsDataModelBean extends BaseDataModelBean {

    public static final String SELECT_ALL = "Locations.selectAll";
    public static final String SELECT_ALL_COUNT = "Locations.selectAllCount";
}
