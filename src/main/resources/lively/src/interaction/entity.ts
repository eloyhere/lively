export interface BaseEntity{
    id: string;
    lock: Date;
    spawn: Date;
    edit: Date;
    version: bigint
}

export interface Authority{
    authority: string;
}

export interface Role{
    name: string;
    authorities: Set<Authority>;
}

