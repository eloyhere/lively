export type Size = "large" | "default" | "small";

export type LinkType = "primary" | "success" | "warning" | "danger" | "info" | "default";
export interface Link{
    type?: LinkType;
    text: string;
    href?: string;
    icon?: string;
    target?: "_blank" | "_self" | "_parent" | "_top";
    disabled?: boolean;
    hidden?: boolean;
}

export type TagType = "primary" | "success" | "info" | "warning" | "danger";
export type TagEffect = "dark" | "light" | "plain";
export interface Tag{
    type: LinkType;
    closable?: boolean;
    hit?: boolean;
    color?: string;
    effect?: TagEffect;
    round?: boolean;
    disabled?: boolean;
    text?: string;
    size: Size;
}