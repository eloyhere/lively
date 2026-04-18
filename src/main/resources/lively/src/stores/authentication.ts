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
                            role.authorities.forEach((authority) => {
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
                                message: "身份信息过期，请重新登录。",
                                type: "info"
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