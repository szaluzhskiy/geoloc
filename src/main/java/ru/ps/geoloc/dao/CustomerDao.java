package ru.ps.geoloc.dao;

import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.ps.geoloc.model.domain.GeoCell;
import ru.ps.geoloc.model.domain.GeoLabel;
import ru.ps.geoloc.utils.LonLatToMetersConverter;
import ru.ps.geoloc.web.domain.CreateUpdateCustomerRequest;

import java.util.List;

@Transactional
public class CustomerDao implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);

    private HibernateTemplate hibernateTemplate;

    public boolean createUpdateCustomer(CreateUpdateCustomerRequest request) {
        if (request != null && request.getUserID() != 0 && !checkIfCustomerExists(request.getUserID())) {
            if (LOG.isDebugEnabled())
                LOG.debug("Create new user id = " + request.getUserID() + " lon = " + request.getLon() + " lat = " + request.getLat() + "");
            GeoLabel gl = new GeoLabel();
            gl.setUserId(request.getUserID());
            gl.setLon(request.getLon());
            gl.setLat(request.getLat());
            gl.setCellX(request.getLon().intValue());
            gl.setCellY(request.getLat().intValue());
            hibernateTemplate.saveOrUpdate(gl);
            hibernateTemplate.flush();
            if (LOG.isDebugEnabled())
                LOG.debug("User with id = " + request.getUserID() + " created");
            return true;
        }
        return false;
    }

    public boolean updateExistingCustomer(CreateUpdateCustomerRequest customer) {
        if (customer != null && checkIfCustomerExists(customer.getUserID())) {
            GeoLabel gl = new GeoLabel();
            gl.setUserId(customer.getUserID());
            gl.setLon(customer.getLon());
            gl.setLat(customer.getLat());
            gl.setCellX(LonLatToMetersConverter.lon2xCell(customer.getLon()));
            gl.setCellY(LonLatToMetersConverter.lat2yCell(customer.getLat().intValue()));
            GeoLabel mergedGl = hibernateTemplate.merge(gl);
            hibernateTemplate.saveOrUpdate(mergedGl);
            hibernateTemplate.flush();
            return true;
        }
        return false;
    }

    public GeoLabel findCustomerByUserId(Integer userId) {
        List users = hibernateTemplate.find("from geo_user_labels where user_id = ?", userId);
        return users != null && users.size() > 0 ? (GeoLabel) users.get(0) : null;
    }

    public Integer findNumberOfCustomers() {
        return DataAccessUtils.intResult(hibernateTemplate.execute(
                (HibernateCallback<List>) s -> {
                    SQLQuery sql = s.createSQLQuery("SELECT COUNT(*) FROM geo_user_labels");
                    return sql.list();
                }
        ));
    }

    public GeoCell findGeoCell(int lon, int lat) {
        List geoCells = hibernateTemplate.find("from geo_network where tile_x = ? and tile_y = ?", lon, lat);
        return geoCells != null && geoCells.size() > 0 ? (GeoCell) geoCells.get(0) : null;
    }

    public boolean deleteExistingCustomer(Integer userId) {
        if (userId != null && checkIfCustomerExists(userId)) {
            StringBuilder hql = new StringBuilder();
            hql.append("delete ").append(" from ").append("geo_user_labels").append(" where ").append("id=").append(userId.intValue());
            hibernateTemplate.bulkUpdate(hql.toString());
            return true;
        }
        return false;
    }

    public boolean checkIfCustomerExists(Integer userId) {
        List users = hibernateTemplate.find("from geo_user_labels where user_id = ?", userId);
        return users != null && users.size() > 0 ? true : false;
    }

    public Integer findNumberOfCustomersInCell(Integer cellX, Integer cellY) {
        return DataAccessUtils.intResult(hibernateTemplate.find("select count(1) from geo_user_labels where title_x = ? and title_y = ?", cellX, cellY));
    }

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.getHibernateTemplate() == null) {
            throw new IllegalArgumentException("Property \'hibernateTemplate\' is required");
        }
    }
}
