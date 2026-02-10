package jpa.basic.advancedschedule.comment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jpa.basic.advancedschedule.config.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "deleted")
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "작성자명은 필수값입니다.")
    @Size(max = 20, message = "작성자명은 최대 20자까지 가능합니다.")
    @Column(length = 20, nullable = false, unique = true)
    private String author;

    @NotBlank(message = "일정 제목은 필수값입니다.")
    @Size(max = 50, message = "일정 제목은 최대 50자까지 가능합니다.")
    @Column(length = 50, nullable = false)
    private String title;

    @NotBlank(message = "일정 내용은 필수값입니다.")
    @Size(max = 50, message = "일정 내용은 최대 50자까지 가능합니다.")
    @Column(length = 50, nullable = false)
    private String content;

    @Builder
    public Comment(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public void change(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}
