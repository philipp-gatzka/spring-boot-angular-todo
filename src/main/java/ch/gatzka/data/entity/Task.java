package ch.gatzka.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@NoArgsConstructor
public final class Task {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotNull(message = "Title must not be null")
    @NotEmpty(message = "Title must not be empty")
    @Length(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    @Column(name = "title", length = 100, nullable = false)
    private String title;

    @NotNull(message = "Description must not be null")
    @NotEmpty(message = "Description must not be empty")
    @Length(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    @Column(name = "description", length = 1000, nullable = false)
    private String description;

    @NotNull(message = "Done must not be null")
    @Column(name = "done", nullable = false)
    private Boolean done;

    public Task(String title, String description, Boolean done) {
        this.title = title;
        this.description = description;
        this.done = done;
    }

}
