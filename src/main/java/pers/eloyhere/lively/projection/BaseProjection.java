package pers.eloyhere.lively.projection;

import java.time.LocalDateTime;
import java.util.UUID;

public interface BaseProjection {

    UUID getId();

    LocalDateTime getLock();

    LocalDateTime getSpawn();

    LocalDateTime getEdit();

    Long getVersion();

    LocalDateTime getBan();
}
