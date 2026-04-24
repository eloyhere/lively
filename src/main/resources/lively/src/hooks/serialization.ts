import {
    type BiFunctional, isFunction,
    isObject,
    isPrimitive, isString,
    type MaybeInvalid, typeOf,
    validate
} from "semantic-typescript";
import type {Serializer} from "@/declaration/serialization.ts";
import type {KeyOf, ValueOf} from "@/hooks/utility.ts";

interface UseStringify{
    (target: any): string;
    (target: any, callback: BiFunctional<string, any, string>): string;
}
export const useStringify: UseStringify = (target, callback?: BiFunctional<string, any, string>): string => {
    switch (typeOf(target)) {
        case "object":
            let seen: WeakSet<object> = new WeakSet<object>();
            return JSON.stringify(target, (key, value) => {
                if(seen.has(value)){
                    return (void 0);
                }
                if(isObject(value)){
                    seen.add(value);
                }
                if(isFunction(callback)){
                    return callback(key, value);
                }
                return value;
            });
        case "string":
            return target;
        case "bigint":
        case "number":
        case "boolean":
            return String(target);
        case "null":
        case "undefined":
            return "";
        default:
            return "";
    }
};

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
                let regex: RegExp =/^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(\.\d+)?(Z|[+-]\d{2}:?\d{2})?$/;
                return JSON.parse(text, (key, value) => {
                    if(map.has(key as K)){
                        let serializer: Serializer<V> = map.get(key as K)!;
                        return serializer.deserialize(value);
                    }
                    return isString(value) && regex.test(value) ? new Date(value) : value;
                });
            },
            serialize(target: E | Partial<E>): string {
                return useStringify(target);
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
            return useStringify(target);
        }
    };
};