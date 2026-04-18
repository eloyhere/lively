package pers.eloyhere.lively.controller.book;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.book.Book;
import pers.eloyhere.lively.repository.book.BookRepository;
import pers.eloyhere.lively.service.book.BookService;

@RestController("bookController")
@RequestMapping("/book")
class BookController extends BaseController<Book, BookRepository, BookService> {

    public BookController(BookService service) {
        super(service);
    }
}
