package pers.eloyhere.lively.repository;

import org.springframework.stereotype.Repository;
import pers.eloyhere.lively.entity.Announcement;

@Repository("announcementRepository")
public interface AnnouncementRepository extends BaseRepository<Announcement> {
}