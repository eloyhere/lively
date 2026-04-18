import type {MaybeInvalid} from "semantic-typescript";

export interface BaseEntity{
    id: string;
    lock: Date;
    spawn: Date;
    edit: Date;
    version: bigint
}

export interface Authority extends BaseEntity{
    authority: string;
}

export interface Role extends BaseEntity{
    name: string;
    authorities: Set<Authority>;
}

export interface Token extends BaseEntity{
    series: String;
    token: String;
    last: Date;
}

export interface Consumer extends BaseEntity{
    username: string;
    nickname: string;
    password?: string;
    avatar: string;
    tokens: Set<Token>;
    roles: Set<Role>;
}

export interface Authentication extends BaseEntity{
    authenticated: boolean;
    authorities: Array<Authority>;
    credentials: MaybeInvalid<string>;
    name: string;
    principal: MaybeInvalid<Consumer>;
}

export interface Invitation extends BaseEntity{
    code: string;
}

