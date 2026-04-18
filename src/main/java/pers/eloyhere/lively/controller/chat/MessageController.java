package pers.eloyhere.lively.controller.chat;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.controller.BaseController;
import pers.eloyhere.lively.entity.book.Book;
import pers.eloyhere.lively.entity.chat.Message;
import pers.eloyhere.lively.repository.book.BookRepository;
import pers.eloyhere.lively.repository.chat.MessageRepository;
import pers.eloyhere.lively.service.book.BookService;
import pers.eloyhere.lively.service.chat.MessageService;

@RestController("messageController")
@RequestMapping("/message")
class MessageController extends BaseController<Message, MessageRepository, MessageService> {

    public MessageController(MessageService service) {
        super(service);
    }
}
