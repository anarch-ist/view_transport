package ru.logistica.tms.dao.userDao;

import org.testng.annotations.Test;
import ru.logistica.tms.HibernateTest;
import ru.logistica.tms.dao.HibernateUtils;

public class UserDaoImplTest extends HibernateTest {

    @Test
    public void testSave() throws Exception {
        HibernateUtils.beginTransaction();
        User user = new User();
        user.setUserLogin("user6");
        user.setPassword("testtest");
        UserRoleDaoImpl userRoleDao = new UserRoleDaoImpl();
        user.setUserRole(userRoleDao.findById(UserRole.class, UserRoles.SUPPLIER_MANAGER));
        user.setUserName("Fed");
        user.setPhoneNumber("8-916-376-90-43");
        user.setEmail("f@mail.ru");
        user.setPosition("position6");
        UserDao userDao = new UserDaoImpl();
        userDao.save(user);
        HibernateUtils.commitTransaction();
    }

    @Test
    public void testPersist() throws Exception {
        HibernateUtils.beginTransaction();
        HibernateUtils.commitTransaction();
    }

    @Test(dependsOnMethods = {"testSave"})
    public void testFindByLogin() throws Exception {
        HibernateUtils.beginTransaction();
        UserDao userDao = new UserDaoImpl();
        User user6 = userDao.findByLogin(User.class, "user6");
        System.out.println(user6);

        HibernateUtils.commitTransaction();
    }

    @Test
    public void testUpdate() throws Exception {
        HibernateUtils.beginTransaction();
        HibernateUtils.commitTransaction();
    }

    @Test
    public void testDelete() throws Exception {
        HibernateUtils.beginTransaction();
        HibernateUtils.commitTransaction();
    }

    @Test
    public void testFindAll() throws Exception {
        HibernateUtils.beginTransaction();
        HibernateUtils.commitTransaction();
    }

    @Test
    public void testFindById() throws Exception {
        HibernateUtils.beginTransaction();
        HibernateUtils.commitTransaction();
    }
}