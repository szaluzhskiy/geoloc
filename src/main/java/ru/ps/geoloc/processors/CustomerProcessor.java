package ru.ps.geoloc.processors;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ps.geoloc.dao.CustomerDao;
import ru.ps.geoloc.model.domain.GeoCell;
import ru.ps.geoloc.model.domain.GeoLabel;
import ru.ps.geoloc.utils.LonLatToMetersConverter;
import ru.ps.geoloc.web.domain.CreateUpdateCustomerRequest;

public class CustomerProcessor {
    private final static String SUCCESS_OPERATION_RESPONSE = "OK";
    private final static String FAILED_OPERATION_RESPONSE = "NOK";

    private CustomerDao customerDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String createNewCustomer(CreateUpdateCustomerRequest customer) {
        return customerDao.createUpdateCustomer(GeoLabelBuilder.buildGeoLabel(customer)) ? SUCCESS_OPERATION_RESPONSE : FAILED_OPERATION_RESPONSE;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String deleteCustomer(Integer userId) {
        return customerDao.deleteExistingCustomer(userId) ? SUCCESS_OPERATION_RESPONSE : FAILED_OPERATION_RESPONSE;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String updateCustomer(CreateUpdateCustomerRequest customer) {
        return customerDao.updateExistingCustomer(GeoLabelBuilder.buildGeoLabel(customer)) ? SUCCESS_OPERATION_RESPONSE : FAILED_OPERATION_RESPONSE;
    }

    /**
     *
     * @param userId
     * @return X, Y of geo cell in which user is located
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String findCustomerLocation(Integer userId) {
        String responseString;
        GeoLabel geoLabel = customerDao.findCustomerByUserId(userId);
        if (geoLabel != null) {
            int geoLabelLon = LonLatToMetersConverter.lon2xCell(geoLabel.getLon());
            int geoLabelLat = LonLatToMetersConverter.lat2yCell(geoLabel.getLat());
            GeoCell geoCell = customerDao.findGeoCell(geoLabelLon, geoLabelLat);
            if (((geoLabel.getLon() - geoLabel.getLon().intValue()) > geoCell.getDistance_error()) || ((geoLabel.getLat() - geoLabel.getLat().intValue()) > geoCell.getDistance_error())) {
                responseString = "User location is outside of distance error. Cell coordinates x=" + geoCell.getTile_x() + ";y=" + geoCell.getTile_y();
            } else {
                responseString = "User location is inside cell. Cell coordinates x=" + geoCell.getTile_x() + ";y=" + geoCell.getTile_y();
            }
            return responseString;
        }
        return "User not found";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Integer findNumberOfUsersInCell(Integer title_x, Integer title_y) {
        return customerDao.findNumberOfCustomersInCell(title_x, title_y);
    }

    public Integer findNumberOfCustomers(){
        return customerDao.findNumberOfCustomers();
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    private static class GeoLabelBuilder {
        public static GeoLabel buildGeoLabel(CreateUpdateCustomerRequest customer) {
            GeoLabel gl = new GeoLabel();
            if(customer != null) {
                gl.setUserId(customer.getUserID());
                gl.setLon(customer.getLon());
                gl.setLat(customer.getLat());
                gl.setCellX(LonLatToMetersConverter.lon2xCell(customer.getLon()));
                gl.setCellY(LonLatToMetersConverter.lat2yCell(customer.getLat()));
            }
            System.out.println("GeoLabel = " + gl.toString());
            return gl;
        }
    }
}
