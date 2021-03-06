package com.lacussoft.sosifod.security;

import com.lacussoft.utils.HibernateUtil;
import com.lacussoft.sosifod.model.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Query;
import org.hibernate.Session;

@Stateless
public class AuthenticationBean implements Serializable {

    private Session session;

    @Inject
    private ExternalContext externalContext;

    @Inject
    private SecurityContext securityContext;

    @PostConstruct
    public void onInit() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    @PreDestroy
    public void onDestroy() {
        session.close();
    }

    public AuthenticationStatus attempt(String cpf, String password) {
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        UsernamePasswordCredential credential = new UsernamePasswordCredential(cpf, password);
        AuthenticationParameters authParams = AuthenticationParameters.withParams().credential(credential);

        AuthenticationStatus status = securityContext.authenticate(request, response, authParams);

        return status;
    }

    public void logout() {
        try {
            HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
            request.logout();
        } catch (ServletException ex) {
            throw new RuntimeException(ex);
        }
    }

    public User validate(String login, String password) {
        String hashedPassword = User.hashPassword(password);
        String hql = "FROM User u WHERE u.cpf = :cpf AND u.password = :password";

        Query query = session.createQuery(hql);
        query.setString("password", hashedPassword);
        query.setString("cpf", login);

        return (User) query.uniqueResult();
    }
}
