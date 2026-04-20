package pers.eloyhere.lively.projection.consumer;

import pers.eloyhere.lively.projection.BaseProjection;

public interface AuthenticationConsumer extends BaseProjection {

    public String getUsername();

    public String getPassword();

    public String getNickname();

    public String getAvatar();
}
