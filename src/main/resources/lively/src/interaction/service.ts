import type {
    Authority,
    BaseEntity,
    Consumer,
    Role,
    Invitation,
    Token,
    Announcement,
    Query,
    Page, Book, Chapter
} from "@/declaration/entity";
import {useDelete, useGet, usePut} from "@/hooks/network";
import {type Serializer} from "@/declaration/serialization";
import {type Consumer as FConsumer} from "semantic-typescript";
import {useSerialization} from "@/hooks/serialization";
import Chat from "@/views/management/chat/Chat.vue";



export class BaseService<E extends BaseEntity>{

    private readonly prefix: string = "http://localhost:8080";

    private readonly module: string;

    private readonly serializer: Serializer<E> = useSerialization<E>();

    public constructor(module: string) {
        this.module = module;
    }

    public async countBy(entity: E): Promise<bigint>;
    public async countBy(entity: Partial<E>): Promise<bigint>;
    public async countBy(entity: E | Partial<E>): Promise<bigint> {
        let url: string = `${this.prefix}/${this.module}/countBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<bigint>((resolve: FConsumer<bigint>, reject: FConsumer<unknown>) => {
            try{
                useGet(url, parameters)
                    .then((response: Response) => {
                        if(response.status === 200){
                            response.json().then(BigInt).then(resolve);
                        }else{
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                            reject(response.statusText);
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
                        reject(response.statusText);
                    }
                })
            }catch (e) {
                console.log("11111", e);
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
                        reject(response.statusText);
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
        parameters.append("payload", encodeURIComponent(serializer.serialize(query.target)));
        parameters.append("size", encodeURIComponent(query.size));
        parameters.append("page", encodeURIComponent(query.page));
        return new Promise<Page<E>>((resolve: FConsumer<Page<E>>, reject: FConsumer<unknown>) => {
            try {
                let serializer: Serializer<Page<E>> = useSerialization<Page<E>>();
                useGet(url, parameters).then((response: Response) => {
                    if(response.status === 200){
                        response.text()
                            .then(serializer.deserialize)
                            .then(resolve);
                    }else{
                        reject(response.statusText);
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
                        reject(response.statusText);
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
                        reject(response.statusText);
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

export class ConsumerService extends BaseService<Consumer>{

    public constructor() {
        super("consumer");
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


