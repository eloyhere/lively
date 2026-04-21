import type {BaseEntity} from "@/declaration/entity.ts";

export interface Insert<E extends BaseEntity> {
    show: boolean;
    target: E | Partial<E>;
}

export interface Update<E extends BaseEntity> {
    show: boolean;
    target: E | Partial<E>;
}

export interface Operator<E extends BaseEntity>{
    ready(): void;
    dismiss(): void;
    reset(): void;
    validate(): Promise<void>;
    perform(): Promise<void>;
}