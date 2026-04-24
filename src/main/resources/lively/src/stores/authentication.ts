import { defineStore } from "pinia";
import { type Ref, ref } from "vue";
import { type MaybeInvalid, Optional, validate } from "semantic-typescript";
import type { Authentication, Authority, Consumer, Role} from "@/declaration/entity";
import { ElMessage } from "element-plus";
import type { Router } from "vue-router";
import router from "@/router";

interface AuthenticationStore {
    authentication: Ref<MaybeInvalid<Authentication>>;
}
export const authenticationStore = defineStore(
    "authentication",
    {
        state: (): AuthenticationStore => ({
            authentication: ref<MaybeInvalid<Authentication>>()
        }),
        getters: {
            value(): Authentication {
                if (validate(this.authentication)) {
                    return this.authentication;
                }
                let now: Date = new Date();
                return {
                    authorities: [],
                    credentials: null,
                    name: "guest",
                    principal: {
                        ban: new Date(),
                        edit: new Date(),
                        id: "",
                        lock: new Date(),
                        nickname: "游客",
                        roles: [
                            {
                                name: "guest",
                                authorities: [],
                                id: "",
                                lock: now,
                                spawn: now,
                                ban: now,
                                edit: now,
                                version: 0n
                            }
                        ],
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
            avatar(): Optional<string> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.avatar);
            },
            username(): Optional<string> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.username);
            },
            nickname(): Optional<string> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): string => consumer.nickname);
            },
            roles(): Array<Role> {
                let now: Date = new Date();
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): Array<Role> => consumer.roles)
                    .get([
                        {
                            name: "guest",
                            authorities: [],
                            id: "",
                            lock: now,
                            spawn: now,
                            ban: now,
                            edit: now,
                            version: 0n
                        }
                    ]);
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
            },
        },
        actions: {
            setAuthentication(authentication: Authentication): void {
                this.authentication = authentication;
            },
            removeAuthentication(): void {
                localStorage.removeItem("authentication");
                this.authentication = null;
            },
            expire(): void {
                localStorage.removeItem("authentication");
                this.authentication = null;
            },
            hasAuthority(authority: string): boolean {
                return false;
            },
        },
        persist: true
    }
)