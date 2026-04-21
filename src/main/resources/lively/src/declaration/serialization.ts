export interface Serializer<E>{
    deserialize(text: string): E;
    serialize(entity: E): string;
    serialize(entity: Partial<E>): string;
}