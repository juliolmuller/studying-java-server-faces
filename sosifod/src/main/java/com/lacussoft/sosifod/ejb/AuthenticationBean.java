package com.lacussoft.sosifod.ejb;

import com.lacussoft.sosifod.model.User;
import com.lacussoft.utils.HibernateUtil;
import com.lacussoft.utils.SecurityUtil;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import org.hibernate.Query;
import org.hibernate.Session;

@Stateless
public class AuthenticationBean {
    
    private Session session;
    
    @PostConstruct
    public void onInit() {
        session = HibernateUtil.getSessionFactory().openSession();
    }
    
    @PreDestroy
    public void onDestroy() {
        session.close();
    }
    
    public User createUser(User user) {
        String hashedPassword = SecurityUtil.encryptPassword(user.getPassword());
        user.setPassword(hashedPassword);
        
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        
        return user;
    }
    
    public User validate(String username, String password) {
        String hashedPassword = SecurityUtil.encryptPassword(password);
        String hql = "FROM User u WHERE u.name = :username AND u.password = :password";
        
        Query query = this.session.createQuery(hql);
        query.setString("password", hashedPassword);
        query.setString("username", username);
        
        return (User) query.uniqueResult();
    }
}
