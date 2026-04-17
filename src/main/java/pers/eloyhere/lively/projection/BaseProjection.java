package pers.eloyhere.lively.projection;

import java.time.LocalDateTime;

public interface BaseProjection {

    LocalDateTime getSpawn();

    LocalDateTime getEdit();

    Long getVersion();

    LocalDateTime getBan();


}
