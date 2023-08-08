package shop.mtcoding.blog.dto;

import lombok.Getter;
import lombok.Setter;

/* 프엔한테 줄거
 * 글쓰기 API
 * 1. URL : http://localhost:8080/board/save
 * 2. method : POST
 * 3. 요청 body(request body): title=값(String)&content=값(String)
 * 4. MIME 타입 : x-www-form-unlencoded
 * 5. 응답 : view(html)를 응답함 detail해당번호페이지 (글 수정했으니 그 글로 간다)
 */

@Getter
@Setter
public class WriteDTO {
    private String title;
    private String content;
}
