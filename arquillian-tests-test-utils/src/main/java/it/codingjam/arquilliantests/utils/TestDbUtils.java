package it.codingjam.arquilliantests.utils;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pizzo on 07/07/16.
 */
public class TestDbUtils {

    private static final Logger LOGGER = Logger.getLogger(TestDbUtils.class.getName());

    @Inject
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public void cleanDb() throws Exception {
        try {
            userTransaction.begin();
            entityManager.createQuery("DELETE FROM Task").executeUpdate();
            entityManager.createQuery("DELETE FROM User").executeUpdate();
            userTransaction.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cleaning db", e);
            userTransaction.rollback();
        }
    }
}
