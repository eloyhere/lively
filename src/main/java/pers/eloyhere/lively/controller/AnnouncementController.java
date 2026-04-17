package pers.eloyhere.lively.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.entity.Announcement;
import pers.eloyhere.lively.repository.AnnouncementRepository;
import pers.eloyhere.lively.service.AnnouncementService;

@RestController("announcementController")
@RequestMapping("/announcement")
class AnnouncementController extends BaseController<Announcement, AnnouncementRepository, AnnouncementService> {

    public AnnouncementController(AnnouncementService service) {
        super(service);
    }
}
