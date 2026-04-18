package pers.eloyhere.lively.service.book;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.book.Chapter;
import pers.eloyhere.lively.repository.book.ChapterRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("chapterService")
public class ChapterService extends BaseService<Chapter, ChapterRepository> {
    public ChapterService(ChapterRepository repository) {
        super(repository);
    }
}
