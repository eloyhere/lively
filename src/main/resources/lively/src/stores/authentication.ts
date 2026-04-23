import {defineStore, type PiniaPluginContext, type StateTree} from "pinia";
import {ref} from "vue";
import {type MaybeInvalid, Optional, validate} from "semantic-typescript";
import type {Authentication, Authority, Consumer, Role} from "@/declaration/entity";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";

export const useAuthenticationStore = defineStore(
    "authentication",
    {
        state: () => ({
            authentication: ref<MaybeInvalid<Authentication>>()
        }),
        getters: {
            value(): Authentication {
                if(validate(this.authentication)){
                    return this.authentication;
                }
                return {
                    authorities: [],
                    credentials: null,
                    name: "",
                    principal: {
                        ban: new Date(),
                        edit: new Date(),
                        id: "",
                        lock: new Date(),
                        nickname: "游客",
                        roles: [],
                        spawn: new Date(),
                        tokens: [],
                        username: "游客",
                        version: 0n,
                        avatar: ""

                    },
                    authenticated: false

                };
            },
            authenticated(): boolean {
                let authentication: MaybeInvalid<Authentication> = this.authentication;
                return validate(authentication) && authentication.name !== "guest";
            },
            principal(): Optional<Consumer> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication): Optional<Consumer> => Optional.of(authentication.principal));
            },
            avatar(): Optional<string>{
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.avatar);
            },
            username(): Optional<string>{
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.username);
            },
            nickname(): Optional<string>{
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.nickname);
            },
            roles(): Array<Role> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): Array<Role> => consumer.roles)
                    .get(new Array<Role>());
            },
            authorities(): Array<Authority> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): Array<Role> => consumer.roles)
                    .map((roles: Array<Role>) => {
                        let authorities: Array<Authority> = new Array<Authority>();
                        roles.forEach((role) => {
                            role.authorities.forEach((authority: any) => {
                                authorities.push(authority);
                            });
                        });
                        return authorities;
                    })
                    .get(new Array<Authority>());
            }
        },
        actions: {
            setAuthentication(authentication: Authentication): void {
                this.authentication = authentication;
            },
            removeAuthentication(): void {
                localStorage.removeItem("authentication");
                this.authentication = null;
                ElMessage({
                    message: "身份信息已失效",
                    type: "info"
                });
                useRouter().push("/authentication");
            },
            hasAuthority(authority: string): boolean {
                return false;
            },
        },
        persist: true
    }
)