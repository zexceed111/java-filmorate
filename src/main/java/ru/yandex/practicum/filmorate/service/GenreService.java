package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.dto.GenreDto;
import ru.yandex.practicum.filmorate.dto.GenreRequest;
import ru.yandex.practicum.filmorate.exception.DuplicateDataException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GenreService {

    @Autowired
    private final GenreDbStorage genreDbStorage;

    public GenreDto createGenre(GenreRequest request) {
        if (genreDbStorage.findByName(request.getName()).isPresent()) {
            log.warn("\nNot created genre {}", request);
            throw new DuplicateDataException("Genre " + request.getName() + " already exists.", request);
        }
        Genre genre = GenreMapper.mapToGenre(request);
        return GenreMapper.mapToGenreDto(genreDbStorage.createGenre(genre));
    }

    public GenreDto changeGenreData(long id, GenreRequest request) {
        genreDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA id = " + id + " not found ", request));
        Genre genre;
        genre = genreDbStorage.findByName(request.getName()).orElse(null);
        if ((genre != null) && (genre.getId() != id)) {
            throw new DuplicateDataException("Name " + request.getName() + " is already used.", request);
        }
        genre = GenreMapper.mapToGenre(request);
        Genre newGenre = new Genre(id, genre.getName(), genre.getDescription());
        return GenreMapper.mapToGenreDto(genreDbStorage.modifyGenre(newGenre));
    }

    public GenreDto deleteGenre(long id) {
        Genre genre = genreDbStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Genre id = " + id + " not founs ", id));
        return GenreMapper.mapToGenreDto(genreDbStorage.deleteGenre(genre));
    }

    public List<GenreDto> getAllGenre() {
        return genreDbStorage.getAllGenre().stream().map(GenreMapper::mapToGenreDto).toList();
    }

    public GenreDto getGenreById(long l) {
        return GenreMapper.mapToGenreDto(genreDbStorage.findById(l)
                .orElseThrow(() -> new NotFoundException("Genre id = " + l + " not found ", l)));
    }

}
