package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

// user - reply  1-n
// board - reply 1-n 

@Setter
@Getter
@Table(name = "reply_tb")
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String comment; // 댓글내용

    @JoinColumn(name = "user_id") // JoinColumn을 걸어 원하는 컬럼명 설정
    @ManyToOne
    private User user; // Fk user_id
    @ManyToOne
    private Board board; // Fk board_id
}
// dev로 가서 ddl-auto create확인
