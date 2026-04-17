import type {BaseEntity} from "@/interaction/entity.ts";
import {isIterable, useStringify, useToBigInt} from "semantic-typescript";

interface Resolver<E extends BaseEntity>{
    resolve(text: string): E;
    stringify(entity: E): string;
}
export const useEntity: <E extends BaseEntity>(json: string) => Resolver<E> = <E extends BaseEntity>(json: string): Resolver<E> => {
    return {
        resolve(text: string): E {
            let dateTimePropertyKeys: Set<string> = new Set(["lock", "spawn", "edit"]);
            let bigintPropertyKeys: Set<string> = new Set(["version"]);
            let setPropertyKeys: Set<string> = new Set(["authorities"])
            return JSON.parse(json, (key, value): any => {
                if(dateTimePropertyKeys.has(key)){
                    return new Date(value);
                }
                if(bigintPropertyKeys.has(key)){
                    return useToBigInt(value);
                }
                if(setPropertyKeys.has(key)){
                    if(isIterable(value)){
                        return new Set(value);
                    }
                    return value;
                }
                return value;
            });
        },
        stringify(entity: E): string {
            return useStringify(entity);
        }
    };
}