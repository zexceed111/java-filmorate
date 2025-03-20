package ru.yandex.practicum.filmorate.storage.FilmGenre;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("inMemoryFilmGenreStorage")
public class InMemoryFilmGenreStorage implements FilmGenreStorage {

    private Map<Long, FilmGenre> filmGenres = new HashMap<>();

    @Override
    public List<FilmGenre> getGenresOfFilm(Long id) {
        return filmGenres.values().stream().filter(filmGenre -> (filmGenre.getFilmId() == id)).toList();
    }

    public void addFilmGenres(Long id, List<Long> values) {
        values.forEach(i -> {
            Long fgId = getGenreNextId();
            FilmGenre fg = new FilmGenre(fgId, id, i);
            filmGenres.put(fgId, fg);
        });
    }

    @Override
    public void deleteGenreOfFilms(long id) {
        for (Long i : filmGenres.keySet()) {
            if (filmGenres.get(i).getFilmId() == id) filmGenres.remove(i);
        }
    }

    public long getGenreNextId() {
        long currentMaxId = filmGenres.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}