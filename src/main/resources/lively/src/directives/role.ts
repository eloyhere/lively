import type { Authentication, Authority } from "@/declaration/entity";
import { authenticationStore } from "@/stores/authentication";
import { isString, Optional, validate } from "semantic-typescript";
import type { App, DirectiveBinding, VNode } from "vue";

interface RoleDirectiveBinding extends DirectiveBinding<string | Array<string>> {
    value: string | Array<string>;
}

export default (application: App<Element>): void => {

    application.directive("role", {
        mounted: (element: HTMLElement, binding: RoleDirectiveBinding, vnode: VNode): void => {
            authenticationStore().$subscribe((mutation, state) => {
                let roles: Array<string> = authenticationStore().roles.map((role): string => role.name);
                if (isString(binding.value)) {
                    if (roles.includes(binding.value)) {
                        element.style.setProperty("display", "none");
                    } else {
                        element.style.setProperty("display", "block");
                    }
                }else if(Array.isArray(binding.value)){
                    if (roles.some(binding.value.includes)) {
                        element.style.setProperty("display", "none");
                    } else {
                        element.style.setProperty("display", "block");
                    }
                }
            });
        },
        unmounted: (element: HTMLElement, binding: RoleDirectiveBinding, vnode: VNode): void => {

        },
        beforeMount: (element: HTMLElement, binding: RoleDirectiveBinding, vnode: VNode): void => {

        }
    });
}