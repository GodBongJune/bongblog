package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;

@Repository
public class UserRepository {

    @Autowired
    private EntityManager em;

    public User findByUsername(String username) {
        try {
            Query query = em.createNativeQuery("select * from user_tb where username=:username", User.class);
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsernameOrPassword(LoginDTO loginDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=:username and password=:password",
                User.class);
        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());
        return (User) query.getSingleResult();
    }

    @Transactional
    public void save(JoinDTO joinDTO) {
        System.out.println("테스트1");
        Query query = em
                .createNativeQuery("insert into user_tb(username,password,email) values(:username,:password,:email)");
        System.out.println("테스트2");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        System.out.println("테스트3");
        query.executeUpdate(); // 쿼리를전송(DBMS)
        System.out.println("테스트4");
    }

}
