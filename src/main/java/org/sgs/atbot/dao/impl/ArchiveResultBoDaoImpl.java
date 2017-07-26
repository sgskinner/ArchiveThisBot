package org.sgs.atbot.dao.impl;

import java.math.BigInteger;

import org.hibernate.Hibernate;
import org.sgs.atbot.dao.AbstractDao;
import org.sgs.atbot.dao.ArchiveResultBoDao;
import org.sgs.atbot.model.ArchiveResultBo;
import org.springframework.stereotype.Repository;


@Repository("archiveResultBo")
public class ArchiveResultBoDaoImpl extends AbstractDao<BigInteger, ArchiveResultBo> implements ArchiveResultBoDao {
    private static final String PARENT_COMMENT_ID_KEY = "parentCommentId";
    private static final String SELECT_BY_PARENT_ID = String.format("select a from ArchiveResultBo a where parent_comment_id = :%s", PARENT_COMMENT_ID_KEY);


    @Override
    public ArchiveResultBo findById(BigInteger id) {
        ArchiveResultBo archiveResultBo = getByKey(id);
        Hibernate.initialize(archiveResultBo);
        return archiveResultBo;
    }


    @Override
    public void save(ArchiveResultBo archiveResultBo) {
        persist(archiveResultBo);
    }


    @Override
    public void update(ArchiveResultBo archiveResultBo) {
        super.update(archiveResultBo);
    }


    @Override
    public void delete(ArchiveResultBo archiveResultBo) {
        super.delete(archiveResultBo);
    }


    @SuppressWarnings("unchecked")//getSingleResult()
    @Override
    public ArchiveResultBo findByParentCommentId(String parentCommentId) {
        ArchiveResultBo archiveResultBo = (ArchiveResultBo) getEntityManager()
                .createQuery(SELECT_BY_PARENT_ID)
                .setParameter(PARENT_COMMENT_ID_KEY, parentCommentId)
                .getSingleResult();
        Hibernate.initialize(archiveResultBo);
        return archiveResultBo;
    }


    @Override
    public boolean existsByParentCommentId(String parentCommentId) {
        ArchiveResultBo archiveResultBo = findByParentCommentId(parentCommentId);
        return archiveResultBo != null;
    }

}
