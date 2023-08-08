package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

@Repository
public class BoardRepository {

    @Autowired
    private EntityManager em;

    // select id, title from board_tb
    // resultClass 안붙이고 직접 파싱하려면
    // Object[]로 리턴됨
    // Object[0]=1
    // Object[1]=제목1
    public int count() {
        // Entity 타입이(Board,User) 아니어도, 기본 자료도 안됨
        Query query = em.createNativeQuery("select count(*) from board_tb");
        // 원래는 Object 배열로 리턴 받는다 Object 배열의 칼럼의 연속
        // 그룹함수를 써서 하나의칼럼을 조회하면 Object로 리턴
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    public int count2() {
        // Entity 타입이(Board,User) 아니어도, 기본 자료도 안됨
        Query query = em.createNativeQuery("select from board_tb", Board.class);
        return (Integer) query.getMaxResults();
    }

    // 페이징쿼리
    // localhost:8080?page=0
    public List<Board> findAll(int page) {
        final int SIZE = 3; // 상수라서 하나만바꾸면 오류가 발생하기때문에 이렇게 바꿔준다,상수는 대문자
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page,:size ", Board.class);
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
        return query.getResultList();
    }

    @Transactional
    public void save(WriteDTO writeDTO, Integer userId) {
        Query query = em
                .createNativeQuery(
                        "insert into board_tb(title,content,user_id,created_at) values(:title,:content,:userId,now())");
        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void findByIdJoinReply(int boardId) {

        String sql = "select ";
        sql += "b.id board_id, ";
        sql += "b.content board_content, ";
        sql += "b.title board_title, ";
        sql += "b.user_id board_user_id, ";
        sql += "r.id reply_id, ";
        sql += "r.comment reply_comment, ";
        sql += "r.user_id reply_user_id, ";
        sql += "ru.username reply_user_username ";
        sql += "from board_tb b left outer join reply_tb r ";
        sql += "on b.id = r.board_id ";
        sql += "inner join user_tb ru ";
        sql += "on r.user_id = ru.id ";
        sql += "where b.id = 1";
        // Query query = em.createNativeQuery(sql);

    }

    public Board findById(Integer id) {
        Query query = em.createNativeQuery("select *from board_tb where id=:id", Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

    @Transactional
    public void deleteById(Integer id) {
        Query query = em.createNativeQuery("delete from board_tb where id = :id", Board.class);
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @Transactional
    public void update(UpdateDTO updateDTO, Integer id) {
        Query query = em.createNativeQuery("update board_tb set title = :title,content=:content where id = :id",
                Board.class);
        query.setParameter("id", id);
        query.setParameter("title", updateDTO.getTitle());
        query.setParameter("content", updateDTO.getContent());
        query.executeUpdate();
    }

}
