package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    //Поскольку контроллер теперь принимает FilmRequest, аннотации перенесены в этот класс
    private Long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Long duration; //пока предполагаем продолжительность в минутах

    //Новое поле для классификации по МПА
    private Long mpaId;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer count;

    //Множество id лайкнувших юзеров в порядке увеличения id
    private Set<Long> usersLikes = new TreeSet<>((l1, l2) -> Math.toIntExact(l1 - l2));

    public Film(String name, String description, LocalDate localDate, long duration, long mpaId) {
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }

    public Film(Long id, String name, String description, LocalDate localDate, long duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = localDate;
        this.duration = duration;
    }
}
