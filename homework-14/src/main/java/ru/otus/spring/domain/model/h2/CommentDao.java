package ru.otus.spring.domain.model.h2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDao {
    @Id
    private Long id;
    private String text;
    private Long bookId;
}
