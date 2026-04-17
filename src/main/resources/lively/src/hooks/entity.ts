import {isIterable, isObject, isPrimitive, useStringify, useToBigInt} from "semantic-typescript";

export interface Serializer<E>{
    deserialize(text: string): E;
    serialize(entity: E): string;
    serialize(entity: Partial<E>): string;
}
export const useSerializer: <E>() => Serializer<E> = <E>(): Serializer<E> => {
    return {
        deserialize(text: string): E {
            let dateTimePropertyKeys: Set<string> = new Set(["lock", "spawn", "edit", "last"]);
            let bigintPropertyKeys: Set<string> = new Set(["version"]);
            let setPropertyKeys: Set<string> = new Set(["authorities", "roles", "tokens"])
            return JSON.parse(text, (key, value ): any => {
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
        serialize(entity: E | Partial<E>): string {
            if(isPrimitive(entity)){
                return String(entity);
            }else if(isObject(entity)){
                return useStringify(entity);
            }
            return Object.prototype.toString.call(entity);
        }
    };
}