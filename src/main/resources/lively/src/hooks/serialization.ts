import {
    isObject,
    isPrimitive, isString,
    type MaybeInvalid,
    useStringify,
    useToBigInt, validate
} from "semantic-typescript";
import type {Serializer} from "@/declaration/serialization.ts";
import type {KeyOf, ValueOf} from "@/hooks/utility.ts";


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
                        let serializer: Serializer<V> = map.get(key as K)!;
                        return serializer.deserialize(value);
                    }
                    return isString(value) && regex.test(value) ? new Date(value) : value;
                });
            },
            serialize(target: E | Partial<E>): string {
                if(isString(target)){
                    return target;
                }else if(isPrimitive(target)){
                    return String(target);
                }else if(isObject(target)){
                    return useStringify(target);
                }
                // @ts-ignore
                return useStringify(target as object, (key, value) => {
                    if(map.has(key as unknown as K) && !isString(value)){
                        let serializer: Serializer<ValueOf<E>> = map.get(key as unknown as K)!;
                        // @ts-ignore
                        return serializer.serialize(value);
                    }
                    return value;
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
            if(isString(target)){
                return target;
            }else if(isPrimitive(target)){
                return String(target);
            }else if(isObject(target)){
                return useStringify(target);
            }
            // @ts-ignore
            return useStringify(target as object, (key, value) => {
                return value;
            });
        }
    };
};