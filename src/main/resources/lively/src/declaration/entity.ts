import type {MaybeInvalid} from "semantic-typescript";
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

export type Direction = "ASC" | "DESC";
export interface Query<E extends BaseEntity> {
    page: number;
    size: number;
    total: number;
    direction: Direction;
    target: E | Partial<E>;
}

export interface Authority extends BaseEntity{
    authority: string;
}

export interface Role extends BaseEntity{
    name: string;
    authorities: Array<Authority>;
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
    content: string;
    role: "User" | "Assistant" | "System";
    seen: Array<Consumer>;
}

export interface Chat extends BaseEntity{
    name: string;
    messages: Array<Message>;
    description: string;
    consumer: Consumer;
}

export interface Answer extends BaseEntity{
    proper: boolean;
    content: string;
}

export interface Choice extends BaseEntity{
    question: string;
    answers: Array<Answer>;
}

export interface Question extends BaseEntity{
    question: string;
    answer: string;
}

export interface Operation extends BaseEntity{
    consumer: Consumer;
    url: string;
}

export interface Visit  extends BaseEntity{
    ip: string;
    device: string;
    platform: string;
    system: string;
    os: string;
    client: string;
}

// ========== 中医刷题 ==========
export interface TcmQuestion extends BaseEntity {
    subject: string;       // 科目: basic/diagnostics/herbology/prescriptions/acupuncture
    type: string;          // 题型: single/multiple/judge
    question: string;      // 题目内容
    options: string;       // 选项 JSON 数组字符串
    answer: string;        // 正确答案 JSON 数组字符串
    explanation: string;   // 解析
    keyPoint: string;      // 考点
    difficulty: number;    // 难度: 1/2/3
}