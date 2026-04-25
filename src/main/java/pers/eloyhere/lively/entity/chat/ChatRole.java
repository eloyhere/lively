package pers.eloyhere.lively.entity.chat;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Locale;
import java.util.Objects;

public enum ChatRole {
    System("system"), User("user"), Assistant("assistant");

    private final String value;


    ChatRole(String value) {
        this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
        return this.value;
    }

    public static ChatRole of(String name){
        if(Objects.nonNull(name)){
            switch (name.toLowerCase(Locale.ROOT)){
                case "system":
                    return System;
                case "user":
                    return User;
                case "assistant":
                    return Assistant;
            }
        }
        return User;
    }
}
