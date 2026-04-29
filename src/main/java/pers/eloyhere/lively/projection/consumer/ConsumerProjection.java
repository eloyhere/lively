package pers.eloyhere.lively.projection.consumer;

import pers.eloyhere.lively.projection.BaseProjection;

public interface ConsumerProjection extends BaseProjection {

    public String getUsername();

    public String getNickname();

    public String getAvatar();


}
