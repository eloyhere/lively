import {
    isObject,
    isPrimitive, isString,
    type MaybeInvalid,
    useStringify,
    useToBigInt, validate
} from "semantic-typescript";

export interface Serializer<E>{
    deserialize(text: string): E;
    serialize(entity: E): string;
    serialize(entity: Partial<E>): string;
}

type KeyOf<T> = keyof T;
type ValueOf<T> = T[keyof T];
interface UseSerialization{
    <E>(): Serializer<E>;
    <E>(map: Map<KeyOf<E>, Serializer<ValueOf<E>>>): E;
}
export const useSerialization: UseSerialization = <E>(map?: MaybeInvalid<Map<KeyOf<E>, Serializer<ValueOf<E>>>>): Serializer<E> => {
    type K = KeyOf<E>;
    type V = ValueOf<E>;
    if(validate(map) && map.size > 0){
        return {
            deserialize(text: string): E {
                let regex: RegExp = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?$/;
                return JSON.parse(text, (key, value) => {
                    if(map.has(key as K)){
                        let serializer: Serializer<ValueOf<E>> = map.get(key as K)!;
                        return serializer.deserialize(value);
                    }
                    return isString(value) && regex.test(value) ? new Date(value) : value;
                });
            },
            serialize(target: E | Partial<E>): string {
                if(isPrimitive(target)){
                    return String(target);
                }else if(isObject(target)){
                    return useStringify(target);
                }
                // @ts-ignore
                return useStringify(target as object, (key, value) => {
                    if(map.has(key as unknown as K)){
                        let serializer: Serializer<ValueOf<E>> = map.get(key as unknown as K)!;
                        // @ts-ignore
                        return serializer.deserialize(value);
                    }
                    return value
                });
            }
        };
    }
    return {
        deserialize(text: string): E {
            let regex: RegExp = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?$/;
            return JSON.parse(text, (key, value) => {
                return isString(value) && regex.test(value) ? new Date(value) : value;
            });
        },
        serialize(target: E | Partial<E>): string {
            if(isPrimitive(target)){
                return String(target);
            }else if(isObject(target)){
                return useStringify(target);
            }
            return useStringify(target as object);
        }
    };
};

interface UseEntitySerialization{
    <E>(): Serializer<E>;
}
export const useEntitySerialization: UseEntitySerialization = <E>(): Serializer<E> => {
    return {
        deserialize(text: string): E {
            let dateTimePropertyKeys: Set<string> = new Set(["lock", "spawn", "edit", "last"]);
            let bigintPropertyKeys: Set<string> = new Set(["version"]);
            return JSON.parse(text, (key, value ): any => {
                if(dateTimePropertyKeys.has(key)){
                    return new Date(value);
                }
                if(bigintPropertyKeys.has(key)){
                    return useToBigInt(value);
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
};