import Cookies from "js-cookie";
import {validate} from "semantic-typescript";

interface Handler{
    close(): void;
    send(data: BufferSource): void;
    send(data: string): void;
    send(data: Blob): void;
}
interface UseWebsocket{
    (url: string): Handler;
    (url: string, listener: Partial<WebSocketEventMap>): Handler;
}
export const useWebsocket = (address: string, listeners?: Partial<WebSocketEventMap>): Handler => {
    let cookie: Record<string, string> = Cookies.get();
    let remember: string = Reflect.get(cookie, "remember") || "";
    let authentication: string = Reflect.get(cookie, "Authentication") || "";
    let shutdown: boolean = false;

    let url: URL = new URL(address);
    url.searchParams.append("remember", remember);
    url.searchParams.append("Authentication", authentication);
    let websocket: WebSocket = new WebSocket(url);

    if(validate(listeners)){
        for(let key in listeners){
            let listener: EventListener = Reflect.get(listeners, key);
            websocket.addEventListener(key, listener);
        }
    }

    websocket.addEventListener("message", () => {
        if(websocket.readyState === WebSocket.OPEN && shutdown){
            websocket.close(0, "exit");
        }
    })

    return {
        close: () => shutdown = true,
        send: (data: BufferSource | string | Blob): void => {
            websocket.send(data);
        }
    };
}