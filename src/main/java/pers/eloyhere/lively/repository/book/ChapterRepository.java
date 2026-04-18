package pers.eloyhere.lively.repository.book;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.book.Chapter;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("chapterRepository")
public interface ChapterRepository extends BaseRepository<Chapter> {
}