import {defineStore, type PiniaPluginContext, type StateTree, type StoreDefinition} from "pinia";
import type {Authentication, Authority, Consumer, Role, Token} from "@/interaction/entity.ts";
import {ref, type Ref, watch} from "vue";
import {invalidate, type MaybeInvalid, Optional, validate} from "semantic-typescript";
import {type Serializer, useSerializer} from "@/hooks/entity.ts";
import {useGet} from "@/hooks/network.ts";
import {ElMessage} from "element-plus";
import router from "@/router";

const serializer = useSerializer<Authentication>();
export const useAuthenticationStore = defineStore(
    "authentication",
    {
        state: () => ({
            authentication: ref<MaybeInvalid<Authentication>>()
        }),
        getters: {
            authenticated(): boolean {
                return this.authentication !== undefined && this.authentication !== null;
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
            roles(): Set<Role> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): Set<Role> => consumer.roles)
                    .get(new Set<Role>());
            },
            authorities(): Set<Authority> {
                return Optional.of(this.authentication)
                    .flatMap<Consumer>((authentication) => Optional.of(authentication.principal))
                    .map((consumer: Consumer): Set<Role> => consumer.roles)
                    .map((roles: Set<Role>) => {
                        let authorities: Set<Authority> = new Set<Authority>();
                        roles.forEach((role) => {
                            role.authorities.forEach((authority) => {
                                authorities.add(authority);
                            });
                        });
                        return authorities;
                    })
                    .get(new Set<Authority>());
            }
        },
        actions: {
            setAuthentication(authentication: Authentication): void {
                this.authentication = authentication;
            },
            removeAuthentication(): void {
                this.authentication = null;
            },
            hasAuthority(authority: string): boolean {
                return false;
            },
            auto(){
                useGet("http://localhost/authentication/auto")
                    .then((response) => {
                        if(response.status === 200){
                            let serializer: Serializer<Authentication> = useSerializer<Authentication>();
                            response.text().then((text) => {
                                this.setAuthentication(serializer.deserialize(text));
                            });
                        }else{
                            ElMessage({
                                message: "身份信息失效，请重新登录。",
                                type: "info"
                            });
                            router.replace({
                                path: "/authentication/account"
                            });
                            this.removeAuthentication()
                        }
                    });
            }
        },
        persist: {
            key: "authentication",
            storage: localStorage,
            serializer: {
                serialize: (data: StateTree) => {
                    return serializer.serialize(data as unknown as Authentication);
                },
                deserialize: (data: string): Authentication => {
                    return serializer.deserialize(data);
                }
            },
            beforeHydrate(context: PiniaPluginContext) {

            },
            afterHydrate(context: PiniaPluginContext) {

            },
        }
    }
)