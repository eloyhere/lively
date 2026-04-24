import type { Authentication, Authority } from "@/declaration/entity";
import { authenticationStore } from "@/stores/authentication";
import { isString, Optional, validate } from "semantic-typescript";
import type { App, DirectiveBinding, VNode } from "vue";

interface AuthorityDirectiveBinding extends DirectiveBinding<string | Array<string>> {
    value: string | Array<string>;
}

export default (application: App<Element>): void => {

    application.directive("authority", {
        mounted: (element: HTMLElement, binding: AuthorityDirectiveBinding, vnode: VNode): void => {
            authenticationStore().$subscribe((mutation, state) => {
                let authorities: Array<string> = authenticationStore().authorities.map((authority:Authority): string => authority.authority);
                if (isString(binding.value)) {
                    if (authorities.includes(binding.value)) {
                        element.style.setProperty("display", "none");
                    } else {
                        element.style.setProperty("display", "block");
                    }
                }else if(Array.isArray(binding.value)){
                    if (authorities.some(binding.value.includes)) {
                        element.style.setProperty("display", "none");
                    } else {
                        element.style.setProperty("display", "block");
                    }
                }
            });
        },
        unmounted: (element: HTMLElement, binding: AuthorityDirectiveBinding, vnode: VNode): void => {

        },
        beforeMount: (element: HTMLElement, binding: AuthorityDirectiveBinding, vnode: VNode): void => {

        }
    });
}