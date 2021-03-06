package org.sgs.stashbot.service.impl;

import java.math.BigInteger;

import org.sgs.stashbot.dao.AuthTimeServiceDao;
import org.sgs.stashbot.model.AuthPollingTime;
import org.sgs.stashbot.service.AuthTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AuthTimeServiceImpl implements AuthTimeService {
    final AuthTimeServiceDao dao;


    @Autowired
    public AuthTimeServiceImpl(AuthTimeServiceDao dao) {
        this.dao = dao;
    }


    @Override
    public AuthPollingTime getLastSuccessfulAuth() {
        return dao.getLastSuccessfulAuth();
    }


    @Override
    public AuthPollingTime findById(BigInteger id) {
        return dao.findById(id);
    }


    @Override
    public void save(AuthPollingTime authPollingTime) {
        dao.save(authPollingTime);
    }


    @Override
    public void delete(AuthPollingTime authPollingTime) {
        dao.delete(authPollingTime);
    }


    @Override
    public void update(AuthPollingTime authPollingTime) {
        dao.update(authPollingTime);
    }

}
