/**
 * Package ru.hiber.prog for
 *
 * @author Maksim Tiunchik
 */
package ru.hiber.prog;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.hiber.model.RightGroup;

import java.util.Set;

/**
 * Class CreateRights -
 *
 * @author Maksim Tiunchik (senebh@gmail.com)
 * @version 0.1
 * @since 08.04.2020
 */
public enum CreateRights {
    RIGHTS_CREATOR;

    /**
     * error message
     */
    private static final String ERROR_MESSAGE = "Right insertion errors";

    /**
     * name for first group of user rights
     */
    private static final String USER_GROUP_NAME = "USER_RIGHTS";

    /**
     * name for second group of user rights
     */
    private static final String ADMIN_GROUP_NAME = "ADMIN_RIGHTS";

    /**
     * inner logger
     */
    private static final Logger LOG = LogManager.getLogger(CreateRights.class.getName());

    /**
     * link to hibernate class for uploading information
     */
    private static final HiberDB DB = HiberDB.getBD();


    /**
     * main method of class - take connection to HD and fill in information of right groups
     */
    public void create() {
        Transaction tran = null;
        RightGroup usersGroup = DB.gerRights(USER_GROUP_NAME);
        if (usersGroup == null) {
            try (Session session = DB.getFactory().openSession()) {
                tran = session.getTransaction();
                tran.begin();
                RightGroup admin = new RightGroup();
                admin.setGroupName(ADMIN_GROUP_NAME);
                admin.setActions(Set.of("P", "C", "D", "U", "V"));
                RightGroup user = new RightGroup();
                user.setGroupName(USER_GROUP_NAME);
                user.setActions(Set.of("C", "U", "V"));
                session.persist(admin);
                session.persist(user);
                tran.commit();
            } catch (Exception e) {
                if (tran != null) {
                    tran.rollback();
                }
                LOG.error(ERROR_MESSAGE, e);
            }
        }
    }
}
