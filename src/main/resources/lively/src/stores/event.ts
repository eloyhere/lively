import { defineStore } from "pinia";
import type { Consumer } from "semantic-typescript";
import { reactive, type Reactive } from "vue";

interface EventStore {
    handlers: Reactive<Map<string, Array<Consumer<unknown>>>>;
}
export const eventStore = defineStore(
    "event",
    {
        state: (): EventStore => ({
            handlers: reactive<Map<string, Array<Consumer<unknown>>>>(new Map<string, Array<Consumer<unknown>>>())
        }),
        getters: {
            events(): Array<string> {
                return [...this.handlers.keys()];
            }
        },
        actions: {
            subscribe(event: string, handler: Consumer<unknown>): void {
                let group: Array<Consumer<unknown>> = this.handlers.get(event) || [];
                group.push(handler);
                this.handlers.set(event, group);
            },
            unsubscribe(event: string, handler: Consumer<unknown>): void {
                let group: Array<Consumer<unknown>> = this.handlers.get(event) || [];
                group = group.filter(h => h !== handler);
                this.handlers.set(event, group);
            },
            dispatch(event: string, data: unknown): void {
                let group: Array<Consumer<unknown>> = this.handlers.get(event) || [];
                group.forEach((handler: Consumer<unknown>) => handler(data));
            },
            set(event: string, handlers: Array<Consumer<unknown>>): void {
                this.handlers.set(event, handlers);
            },

        }
    }
);