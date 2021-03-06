package org.sgs.stashbot.dao.impl;

import java.math.BigInteger;

import org.sgs.stashbot.dao.AuthTimeServiceDao;
import org.sgs.stashbot.model.AuthPollingTime;
import org.springframework.stereotype.Repository;


@Repository
public class AuthTimeServiceDaoImpl extends AbstractDao<BigInteger, AuthPollingTime> implements AuthTimeServiceDao {
    private static final String SELECT_BY_LAST_SUCCESS = "select t from AuthPollingTime t where id = (select max(id) from AuthPollingTime where success = true)";


    @Override
    public AuthPollingTime getLastSuccessfulAuth() {
        return (AuthPollingTime) getEntityManager()
                .createQuery(SELECT_BY_LAST_SUCCESS)
                .getSingleResult();
    }


    @Override
    public AuthPollingTime findById(BigInteger id) {
        return getByKey(id);
    }


    @Override
    public void save(AuthPollingTime authPollingTime) {
        persist(authPollingTime);
    }


    @Override
    public void delete(AuthPollingTime authPollingTime) {
        authPollingTime = getEntityManager().contains(authPollingTime) ? authPollingTime : getEntityManager().merge(authPollingTime);
        super.delete(authPollingTime);
    }


    @Override
    public void update(AuthPollingTime authPollingTime) {
        super.update(authPollingTime);
    }

}
