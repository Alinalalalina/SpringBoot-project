package ru.grineva.CourseProject.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.grineva.CourseProject.entity.Song;

import java.util.List;

@Repository
public interface SongsRepository extends JpaRepository<Song, Long> {
    List<Song> findByAuthor(String email);
}
