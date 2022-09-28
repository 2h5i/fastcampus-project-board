package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
})
//@EntityListeners(AuditingEntityListener.class)
@Entity
public class Article extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000)
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그 nullable 은 false가 기본

    @ToString.Exclude // 순환 참조의 문제가 생기기에 투스트링 예외를 시켜줘야한다. 둘 중 하나에서 끊어야하는데, 보통 하위에서 끊는다.
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    // 현업에서는 일부러 cascade를 제외한 데이터설계를 하기도 한다. 일부러 foreign key를 안 거는 경우도 있음
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime createdAt; // 생성일시
//
//    @CreatedBy
//    @Column(nullable = false, length = 100)
//    private String createdBy; // 생성자
//
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt; // 수정일시
//
//    @LastModifiedBy
//    @Column(nullable = false, length = 100)
//    private String modifiedBy; // 수정자


    protected Article() {
    } // hibernate는 기본 빈 생성자가 있어야 한다. public 혹은 protected

    private Article(String title, String content, String hashtag) { // 도메인과 관련된 정보만 오픈
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) { // Article 생성 시 이 프로퍼티들이 필요함을 가이드함. Factory method
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) { // 엔티티를 디비에 영속화 시키고 연결시키는 환경에서 서로 다른 엔티티가 같은 조건이 무엇인지?
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
//        Article article = (Article) o;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
