/*
 * Copyright 2014 Samuel Franklyn <sfranklyn at gmail.com>.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import sesawi.api.PricesRpc;
import sesawi.ejb.dao.PricesDaoBean;
import sesawi.jpa.Prices;

/**
 *
 * @author Samuel Franklyn <sfranklyn at gmail.com>
 */
@Stateless
public class PricesRpcBean implements PricesRpc {

    @EJB
    PricesDaoBean pricesDaoBean;

    @Override
    public List<Map> receive() {
        List<Prices> pricesList = pricesDaoBean.selectAll();
        return convertToListMap(pricesList);
    }

    private List<Map> convertToListMap(List<Prices> pricesList) {
        List<Map> pricesListMap = new ArrayList<>();
        for (Prices prices : pricesList) {
            Map<String, Object> pricesMap = new HashMap<>();
            pricesMap.put("priceCode", prices.getPriceCode());
            pricesMap.put("priceEntry", prices.getPriceEntry().toPlainString());
            pricesMap.put("priceEntryHour", prices.getPriceEntryHour().toPlainString());
            pricesMap.put("pricePerHour", prices.getPricePerHour().toPlainString());
            pricesMap.put("priceLost", prices.getPriceLost().toPlainString());
            pricesListMap.add(pricesMap);
        }
        return pricesListMap;
    }

}
