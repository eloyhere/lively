import type {MaybeInvalid} from "semantic-typescript";
import type {Serializer} from "@/declaration/serialization.ts";
import {useSerialization} from "@/hooks/serialization.ts";
import type {Reactive, Ref} from "vue";
export interface BaseEntity{
    id: string;
    lock: Date;
    spawn: Date;
    ban: Date;
    edit: Date;
    version: bigint
}

export interface Sort{
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
}

export interface Pager{
    empty: boolean;
    first: boolean;
    last: boolean;
    number: number;
    numberOfElements: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export interface Page<E extends BaseEntity> {
    content: Array<E>;
    page: Pager;
}

export interface Query<E extends BaseEntity> {
    page: number;
    size: number;
    total: number;
    target: E | Partial<E>;
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
    tokens: Array<Token>;
    roles: Array<Role>;
}

export interface Authentication {
    authenticated: boolean;
    authorities: Array<Authority>;
    credentials: MaybeInvalid<string>;
    name: string;
    principal: MaybeInvalid<Consumer>;
}

export interface Invitation extends BaseEntity{
    code: string;
}

export interface Announcement extends BaseEntity{
    title: string;
    content: string;
}

export interface Chapter extends BaseEntity{
    title: string;
    content: string;
    index: number;
    summary: number;
}

export interface Book extends BaseEntity{
    name: string;
    author: string;
    publisher: string;
    chapters: Array<Chapter>;
}

export interface Message extends BaseEntity{

}

