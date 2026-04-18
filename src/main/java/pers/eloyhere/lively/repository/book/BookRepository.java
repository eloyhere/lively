package pers.eloyhere.lively.repository.book;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.book.Book;
import pers.eloyhere.lively.repository.BaseRepository;

@Repository("bookRepository")
public interface BookRepository extends BaseRepository<Book> {
}