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
import sesawi.ejb.dao.PricesDaoBean;
import sesawi.ejb.datamodel.PricesDataModelBean;
import sesawi.ejb.service.PricesServiceBean;
import sesawi.jpa.Prices;
import sesawi.jsf.model.DatabaseDataModel;

/**
 *
 * @author Samuel Franklyn <sfranklyn@gmail.com>
 */
@Named("prices")
@SessionScoped
public class PricesBean implements Serializable {

    private static final long serialVersionUID = 6737367934989669484L;
    private final Integer noOfRows = 15;
    private final Integer fastStep = 10;
    private final DatabaseDataModel dataModel = new DatabaseDataModel();
    private Prices prices = null;
    @Inject
    private VisitBean visit;
    @EJB
    private PricesDaoBean pricesDaoBean;
    @EJB
    private PricesServiceBean pricesServiceBean;
    @EJB
    private PricesDataModelBean pricesDataModelBean;

    @PostConstruct
    public void init() {
        dataModel.setSelect(PricesDataModelBean.SELECT_ALL);
        dataModel.setSelectCount(PricesDataModelBean.SELECT_ALL_COUNT);
        dataModel.setSelectParam(null);
        dataModel.setWrappedData(pricesDataModelBean);
        prices = new Prices();
    }

    public void findPrice() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.
                getCurrentInstance().getExternalContext().getRequest();
        String priceId = (req.getParameter("priceId"));
        if (priceId != null) {
            Integer priceIdInt = Integer.valueOf(priceId);
            prices = pricesDaoBean.find(priceIdInt);
        }
    }

    public String create() {
        prices = new Prices();
        return "/secure/pricesCreate?faces-redirect=true";
    }

    public String saveCreate() {
        String result = "/secure/pricesCreate";

        List<String> errorList = pricesServiceBean.saveCreate(prices, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/prices?faces-redirect=true";
        }

        return result;
    }

    public String saveUpdate() {
        String result = "/secure/pricesUpdate?priceId=" + prices.getPriceId();

        List<String> errorList = pricesServiceBean.saveUpdate(prices, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/prices?faces-redirect=true";
        }

        return result;
    }

    public String saveDelete() {
        String result = "/secure/pricesDelete";

        List<String> errorList = pricesServiceBean.saveDelete(prices, visit.getLocale());
        if (errorList.size() > 0) {
            for (String error : errorList) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                error, ""));
            }
        } else {
            result = "/secure/prices?faces-redirect=true";
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

    public Prices getPrices() {
        return prices;
    }

    public void setPrices(Prices prices) {
        this.prices = prices;
    }

    public VisitBean getVisit() {
        return visit;
    }

    public void setVisit(VisitBean visit) {
        this.visit = visit;
    }

}
