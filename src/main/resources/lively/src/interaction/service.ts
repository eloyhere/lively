import type {
    Authority,
    BaseEntity,
    Consumer,
    Role,
    Invitation,
    Token,
    Announcement,
    Query,
    Page, Book, Chapter, Authentication, Menu, Message, Route, Chat
} from "@/declaration/entity";
import {useDelete, useGet, usePost, usePut} from "@/hooks/network";
import {type Serializer} from "@/declaration/serialization";
import {type Consumer as FConsumer, isObject, isString, type Optional} from "semantic-typescript";
import {useSerialization} from "@/hooks/serialization";
import {useAuthenticationStore} from "@/stores/authentication.ts";

export class BaseService<E extends BaseEntity>{

    private readonly prefix: string = "http://localhost:8080";

    private readonly module: string;

    private readonly serializer: Serializer<E> = useSerialization<E>();

    public constructor(module: string) {
        this.module = module;
    }

    public async countBy(entity: E): Promise<number>;
    public async countBy(entity: Partial<E>): Promise<number>;
    public async countBy(entity: E | Partial<E>): Promise<number> {
        let url: string = `${this.prefix}/${this.module}/countBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<number>((resolve: FConsumer<number>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.text().then((value: string) => {
                                resolve(Number(value));
                            });
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async deleteAll(): Promise<void> {
        let url: string = `${this.prefix}/${this.module}/deleteAll`;
        return new Promise<void>((resolve: FConsumer<void>, reject: FConsumer<unknown>) => {
            try{
                useDelete(url)
                    .then((response: Response) => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async deleteByIdentifier(identifier: string): Promise<void> {
        let url: string = `${this.prefix}/${this.module}/deleteByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifier", identifier);
        return new Promise<void>((resolve: FConsumer<void>, reject: FConsumer<unknown>) => {
            try{
                useDelete(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async deleteBy(entity: E): Promise<void>;
    public async deleteBy(entity: Partial<E>): Promise<void>;
    public async deleteBy(entity: E | Partial<E>): Promise<void> {
        let url: string = `${this.prefix}/${this.module}/deleteBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<void>((resolve: FConsumer<void>, reject: FConsumer<unknown>) => {
            try{
                useDelete(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async deleteAllByIdentifiers(identifiers: Iterable<string>): Promise<void> {
        let url: string = `${this.prefix}/${this.module}/deleteAllByIdentifiers`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", JSON.stringify(identifiers));
        return new Promise<void>((resolve: FConsumer<void>, reject: FConsumer<unknown>) => {
            try{
                useDelete(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async existsBy(entity: E): Promise<boolean>;
    public async existsBy(entity: Partial<E>): Promise<boolean>;
    public async existsBy(entity: E | Partial<E>): Promise<boolean> {
        let url: string = `${this.prefix}/${this.module}/existsBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<boolean>((resolve: FConsumer<boolean>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.json().then(resolve);
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findByIdentifier(identifier: string): Promise<E> {
        let url: string = `${this.prefix}/${this.module}/findByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifier", identifier);
        return new Promise<E>((resolve: FConsumer<E>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.text()
                                .then(this.serializer.deserialize)
                                .then(resolve);
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findOneBy(entity: E): Promise<E>;
    public async findOneBy(entity: Partial<E>): Promise<E>;
    public async findOneBy(entity: E | Partial<E>): Promise<E> {
        let url: string = `${this.prefix}/${this.module}/findOneBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<E>((resolve: FConsumer<E>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.text()
                                .then(this.serializer.deserialize)
                                .then(resolve);
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    });
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findAllByIdentifiers(identifiers: Iterable<string>): Promise<Array<E>> {
        let url: string = `${this.prefix}/${this.module}/findByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifiers", JSON.stringify(identifiers));
        let serializer: Serializer<Array<E>> = useSerialization();
        return new Promise<Array<E>>((resolve: FConsumer<Array<E>>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.text()
                                .then(serializer.deserialize)
                                .then(resolve);
                        }else{
                            if(response.status === 401){
                                useAuthenticationStore().removeAuthentication();
                            }
                            reject(response);
                        }
                    })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findAll(): Promise<Array<E>>{
        let url: string = `${this.prefix}/${this.module}/findAll`;
        return new Promise<Array<E>>((resolve: FConsumer<Array<E>>, reject: FConsumer<unknown>) => {
            try {
                let serializer: Serializer<Array<E>> = useSerialization();
                useGet(url).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findAllBy(entity: E): Promise<Array<E>>;
    public async findAllBy(entity: Partial<E>): Promise<Array<E>>
    public async findAllBy(entity: E | Partial<E>): Promise<Array<E>>{
        let url: string = `${this.prefix}/${this.module}/findAllBy`;
        return new Promise<Array<E>>((resolve: FConsumer<Array<E>>, reject: FConsumer<unknown>) => {
            try {
                let serializer: Serializer<Array<E>> = useSerialization();
                useGet(url).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async findAllPagedBy(query: Query<E>): Promise<Page<E>>{
        let url: string = `${this.prefix}/${this.module}/findAllPagedBy`;
        let serializer: Serializer<E> = useSerialization();
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", serializer.serialize(query.target));
        parameters.append("size", String(query.size));
        parameters.append("page", String(Math.max(query.page - 1, 0)));
        return new Promise<Page<E>>((resolve: FConsumer<Page<E>>, reject: FConsumer<unknown>) => {
            try {
                let serializer: Serializer<Page<E>> = useSerialization<Page<E>>();
                useGet(url, parameters).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async insert(entity: E): Promise<E>;
    public async insert(entity: Partial<E>): Promise<E>;
    public async insert(entity: E | Partial<E>): Promise<E>{
        let url: string = `${this.prefix}/${this.module}/insert`;
        let serializer: Serializer<E> = useSerialization();
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", serializer.serialize(entity));
        return new Promise<E>((resolve: FConsumer<E>, reject: FConsumer<unknown>) => {
            try {
                usePut(url, parameters).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                })
            }catch (e) {
                reject(e);
            }
        });
    }

    public async update(entity: E): Promise<E>;
    public async update(entity: Partial<E>): Promise<E>;
    public async update(entity: E | Partial<E>): Promise<E>{
        let url: string = `${this.prefix}/${this.module}/update`;
        let serializer: Serializer<E> = useSerialization();
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", serializer.serialize(entity));
        return new Promise<E>((resolve: FConsumer<E>, reject: FConsumer<unknown>) => {
            try {
                usePut(url, parameters).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                })
            }catch (e) {
                reject(e);
            }
        });
    }
}

export class AuthorityService extends BaseService<Authority>{

    public constructor() {
        super("authority");
    }
}

export class RoleService extends BaseService<Role>{

    public constructor() {
        super("role");
    }
}

interface AuthenticationForm{
    username: string;
    password: string;
    remember: boolean;
}
export class ConsumerService extends BaseService<Consumer>{

    public constructor() {
        super("consumer");
    }

    public async identity():Promise<Authentication> {
        let serializer: Serializer<Authentication> = useSerialization();
        return await new Promise<Authentication>((resolve, reject) => {
            useGet("http://localhost:8080/authentication/identity")
                .then((response: Response): void => {
                    if(response.status === 200){
                        response.text().then((text) => {
                            let authentication: Authentication = serializer.deserialize(text);
                            useAuthenticationStore().setAuthentication(authentication);
                            resolve(authentication);
                        });
                    }else{
                        if(response.status === 401){
                            useAuthenticationStore().removeAuthentication();
                        }
                        reject(response);
                    }
                }, reject);
        });
    }

    public async login(consumer: AuthenticationForm): Promise<void>;
    public async login(username: string, password: string, remember?: boolean): Promise<void>;
    public async login(parameter1: string | AuthenticationForm, parameter2?: string, parameter3?: boolean): Promise<void>{
        if(isString(parameter1) && isString(parameter2)){
            let username: string = parameter1;
            let password: string = parameter2;
            let remember: boolean = parameter3 === true;
            return await new Promise<void>((resolve, reject) => {
                let parameters: URLSearchParams = new URLSearchParams();
                parameters.append("username", username);
                parameters.append("password", password);
                parameters.append("remember", String(remember));
                usePost("http://localhost:8080/authentication/login", parameters)
                    .then((response: Response): void => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            reject(response);
                        }
                    }, reject);
            });
        }
        if(isObject(parameter1) && isString(parameter1.username) && isString(parameter1.password)){
            return await new Promise<void>((resolve, reject) => {
                let parameters: URLSearchParams = new URLSearchParams();
                parameters.append("username", parameter1.username);
                parameters.append("password", parameter1.password);
                parameters.append("remember", parameter1.remember? "true" : "false");
                usePost(`http://localhost:8080/authentication/login`, parameters)
                    .then((response: Response): void => {
                        if(response.status === 200){
                            resolve();
                        }else{
                            reject(response);
                        }
                    }, reject);
            });
        }
        return Promise.reject();
    }

    public async auto(): Promise<Authentication> {
        let serializer: Serializer<Authentication> = useSerialization();
        return new Promise<Authentication>((resolve, reject) => {
            useGet(`http://localhost:8080/authentication/auto`)
                .then((response: Response): Promise<string> => response.text())
                .then((text: string): void => {
                    if(isString(text) && text.length > 0){
                        let authentication: Authentication = serializer.deserialize(text);
                        useAuthenticationStore().setAuthentication(authentication);
                        resolve(authentication);
                    }else{
                        reject(text);
                    }
                })
                .catch(reject);
        });
    }

    public async logout(): Promise<void> {
        return await new Promise<void>((resolve, reject) => {
            usePost(`http://localhost:8080/authentication/logout`).then((response) => {
                if(response.status === 200){
                    useAuthenticationStore().removeAuthentication();
                    window.localStorage.removeItem("authentication");
                    resolve();
                }else{
                    reject(response);
                }
            });
        });
    }

    public async register(consumer: Pick<Consumer, "username" | "nickname" | "password" | "avatar"> & { invitation: string}): Promise<Authentication>{
        let serializer: Serializer<Authentication> = useSerialization();
        if(isString(consumer.username) && isString(consumer.nickname) && isString(consumer.password) && isString(consumer.avatar)){
            let username: string = consumer.username;
            let password: string = consumer.password;
            let nickname: string = consumer.nickname;
            let avatar: string = consumer.avatar;
            let invitation: string = consumer.invitation;
            return await new Promise<Authentication>((resolve, reject) => {
                let parameters: URLSearchParams = new URLSearchParams();
                parameters.append("username", username);
                parameters.append("password", password);
                parameters.append("nickname", nickname);
                parameters.append("avatar", avatar);
                parameters.append("invitation", invitation);
                usePost("http://localhost:8080/authentication/login", parameters)
                    .then((response: Response): void => {
                        if(response.status === 200){
                            response.text().then((text) => {
                                let authentication: Authentication = serializer.deserialize(text);
                                useAuthenticationStore().setAuthentication(authentication);
                                resolve(authentication);
                            });
                        }else{
                            reject(response);
                        }
                    }, reject);
            });
        }
        return Promise.reject();
    }
}

export class InvitationService extends BaseService<Invitation>{

    public constructor() {
        super("invitation");
    }
}

export class TokenService extends BaseService<Token>{

    public constructor() {
        super("token");
    }
}

export class AnnouncementService extends BaseService<Announcement>{

    public constructor() {
        super("announcement");
    }
}

export class BookService extends BaseService<Book>{

    public constructor() {
        super("book");
    }
}

export class ChapterService extends BaseService<Chapter>{

    public constructor() {
        super("chapter");
    }
}

export class MessageService extends BaseService<Message>{

    public constructor() {
        super("message");
    }
}

export class ChatService extends BaseService<Chat>{

    public constructor() {
        super("chat");
    }
}

export class MenuService extends BaseService<Menu>{

    public constructor() {
        super("menu");
    }
}


export class RouteService extends BaseService<Route>{

    public constructor() {
        super("route");
    }
}
