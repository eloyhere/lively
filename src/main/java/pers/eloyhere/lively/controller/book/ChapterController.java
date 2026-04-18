package pers.eloyhere.lively.controller.book;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.book.Chapter;
import pers.eloyhere.lively.repository.book.ChapterRepository;
import pers.eloyhere.lively.service.book.ChapterService;

@RestController("chapterController")
@RequestMapping("/chapter")
class ChapterController extends BaseController<Chapter, ChapterRepository, ChapterService> {

    public ChapterController(ChapterService service) {
        super(service);
    }
}
