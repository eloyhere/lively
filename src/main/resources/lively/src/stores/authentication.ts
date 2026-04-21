import {defineStore, type PiniaPluginContext, type StateTree} from "pinia";
import {type Ref, ref} from "vue";
import {isString, type MaybeInvalid, Optional, validate} from "semantic-typescript";
import {useSerialization} from "@/hooks/serialization.ts";
import {useGet, usePost} from "@/hooks/network.ts";
import {ElMessage} from "element-plus";
import type {Authentication, Authority, Consumer, Role} from "@/declaration/entity.ts";
import type {Serializer} from "@/declaration/serialization.ts";
import {useOrigin} from "@/hooks/url.ts";

const serializer = useSerialization<Authentication>();
export const useAuthenticationStore = defineStore(
    "authentication",
    {
        state: () => ({
            authentication: ref<MaybeInvalid<Authentication>>() as Ref<MaybeInvalid<Authentication>>
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
            },
            hasAuthority(authority: string): boolean {
                return false;
            },
            auto(): Promise<boolean>{
                let serializer: Serializer<Authentication> = useSerialization();
                return new Promise<boolean>((resolve, reject) => {
                    window.document.cookie = "";
                    useGet(`${useOrigin()}/authentication/auto`)
                        .then((response: Response): Promise<string> => response.text())
                        .then((text: string): Optional<Authentication> => {
                            if(isString(text) && text.length > 0){
                                return Optional.of(serializer.deserialize(text));
                            }
                            return Optional.empty();
                        })
                        .then((optional: Optional<Authentication>) => {
                            optional.ifPresent((authentication: Authentication) => {
                                if(authentication.name !== "guest"){
                                    this.setAuthentication(authentication);
                                    resolve(true);
                                }
                            });
                        })
                        .catch(reject);
                });
            },
            usernameAndPasswordLogin(username: string, password: string, remember: boolean): Promise<void> {
                let serializer: Serializer<Authentication> = useSerialization();
                return new Promise<void>((resolve, reject) => {
                    let parameters: URLSearchParams = new URLSearchParams();
                    parameters.append("username", username);
                    parameters.append("password", password);
                    parameters.append("remember", String(remember));
                    usePost(`${useOrigin()}/authentication/login`, parameters)
                        .then((response: Response): void => {
                            if(response.status === 200){
                                response.text().then((text) => {

                                    useAuthenticationStore().setAuthentication(serializer.deserialize(text));
                                    resolve();
                                });
                            }else{
                                reject(response);
                            }
                        }, reject);
                });
            }
        },
        persist: true
    }
)