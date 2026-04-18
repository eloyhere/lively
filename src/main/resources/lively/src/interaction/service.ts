import type {Authority, BaseEntity, Consumer, Role, Invitation, Token, Announcement} from "@/interaction/entity.ts";
import {useDelete, useGet} from "@/hooks/network.ts";
import {isBoolean, isNumber, isPrimitive, isString} from "semantic-typescript";
import {type Serializer, useSerializer} from "@/hooks/entity.ts";

export class BaseService<E extends BaseEntity>{

    private readonly module: string;

    private readonly serializer: Serializer<E> = useSerializer<E>();

    public constructor(module: string) {
        this.module = module;
    }

    public countBy(entity: Partial<E>): Promise<bigint> {
        let url: string = `http://localhost/${this.module}/countBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<bigint>((resolve, reject) => {
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

    public deleteByIdentifier(identifier: string): Promise<void> {
        let url: string = `http://localhost/${this.module}/deleteByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifier", identifier);
        return new Promise<void>((resolve, reject) => {
            try{
                useGet(url, parameters)
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

    public deleteBy(entity: Partial<E>): Promise<void> {
        let url: string = `http://localhost/${this.module}/deleteBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<void>((resolve, reject) => {
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

    public deleteAllByIdentifiers(identifiers: Iterable<string>): Promise<void> {
        let url: string = `http://localhost/${this.module}/deleteAllByIdentifiers`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", JSON.stringify(identifiers));
        return new Promise<void>((resolve, reject) => {
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

    public existsBy(entity: Partial<E>): Promise<boolean> {
        let url: string = `http://localhost/${this.module}/existsBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<boolean>((resolve, reject) => {
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

    public findByIdentifier(identifier: string): Promise<E> {
        let url: string = `http://localhost/${this.module}/findByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifier", identifier);
        return new Promise<E>((resolve, reject) => {
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

    public findOneBy(entity: Partial<E>): Promise<E> {
        let url: string = `http://localhost/${this.module}/findOneBy`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("payload", this.serializer.serialize(entity));
        return new Promise<E>((resolve, reject) => {
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

    public findAllByIdentifiers(identifiers: Iterable<string>): Promise<Array<E>> {
        let url: string = `http://localhost/${this.module}/findByIdentifier`;
        let parameters: URLSearchParams = new URLSearchParams();
        parameters.append("identifiers", JSON.stringify(identifiers));
        let serializer: Serializer<Array<E>> = useSerializer();
        return new Promise<Array<E>>((resolve, reject) => {
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

    public findAll(): Promise<Array<E>>{
        let url: string = `http://localhost/${this.module}/findAll`;
        return new Promise((resolve, reject) => {
            try {
                let serializer: Serializer<Array<E>> = useSerializer();
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