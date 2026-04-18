package pers.eloyhere.lively.service.book;

import org.springframework.stereotype.Service;
import pers.eloyhere.lively.entity.book.Book;
import pers.eloyhere.lively.repository.book.BookRepository;
import pers.eloyhere.lively.service.BaseService;

@Service("bookService")
public class BookService extends BaseService<Book, BookRepository> {
    public BookService(BookRepository repository) {
        super(repository);
    }
}
