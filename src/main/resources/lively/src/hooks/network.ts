import { type Consumer, isFunction, isObject, isPrimitive, isString, type MaybeInvalid, validate } from "semantic-typescript";
import { useSerialization } from "@/hooks/serialization.ts";
import type { Serializer } from "@/declaration/serialization.ts";
import { authenticationStore } from "@/stores/authentication";
import { eventStore } from "@/stores/event";

interface UseResponse {
    <D = unknown>(response: Response): Promise<D>;
}
export const useResponse: UseResponse = <D>(response: Response): Promise<D> => {
    return new Promise<D>((resolve, reject): void => {
        try {
            if (response.status > 300) {
                switch (response.status) {
                    case 400:
                        eventStore().dispatch("Bad Request", response.statusText);
                        break;
                    case 401:
                        if(authenticationStore().authenticated){
                            eventStore().dispatch("Expire", response.statusText);
                        }else{
                            eventStore().dispatch("Unauthorized", response.statusText);
                        }
                        break;
                    case 403:
                        eventStore().dispatch("Forbidden", response.statusText);
                        break;
                    case 404:
                        eventStore().dispatch("Not Found", response.statusText);
                    case 405:
                        eventStore().dispatch("Method Not Allowed", response.statusText);
                        break;
                    case 500:
                        eventStore().dispatch("Internal Server Error", response.statusText);
                        break;
                    case 501:
                        eventStore().dispatch("Not Implemented", response.statusText);
                    case 502:
                        eventStore().dispatch("Bad Gateway", response.statusText);
                }
            }
        } catch (e) {

        }
        if (response.ok) {
            try {
                let contentType: string = response.headers.get("Content-Type") || "";
                if (contentType.includes("application/json")) {
                    let serialization: Serializer<D> = useSerialization<D>();
                    response.text().then(serialization.deserialize).then(resolve, reject);
                } else if (contentType.includes("text/")) {
                    response.text().then((text: string): void => resolve(text as unknown as D), reject);
                } else if (contentType.includes("image/") || contentType.includes("application/octet-stream")) {
                    response.blob().then((text: Blob): void => resolve(text as unknown as D), reject);
                } else {
                    response.arrayBuffer().then((text: ArrayBuffer): void => resolve(text as unknown as D), reject);
                }
            } catch (error) {
                reject(error);
            }
        } else {
            reject(response);
        }
    });
};

interface UseGet {
    <D = unknown>(url: string): Promise<D>;

    <D = unknown>(url: string, parameters: FormData): Promise<D>;
    <D = unknown>(url: string, parameters: FormData, headers: Headers): Promise<D>;

    <D = unknown>(url: string, parameters: URLSearchParams): Promise<D>;
    <D = unknown>(url: string, parameters: URLSearchParams, headers: Headers): Promise<D>;
}
export const useGet: UseGet = <D = unknown>(url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<D> => {
    return new Promise<D>((resolve: Consumer<D>, reject: Consumer<unknown>) => {
        let controller = new AbortController();
        let timeout = setTimeout(() => controller.abort(), 5000);
        try {
            if (validate(parameters)) {
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "GET",
                    credentials: "include",
                    signal: controller.signal,
                    headers: headers || new Headers()
                }).then((response: Response) => {
                    clearTimeout(timeout);
                    return useResponse<D>(response);
                }).then(resolve, reject);
            } else {
                window.fetch(url, {
                    method: "GET",
                    credentials: "include",
                    signal: controller.signal,
                    headers: headers || new Headers()
                }).then((response: Response) => {
                    clearTimeout(timeout);
                    return useResponse<D>(response);
                }).then(resolve, reject);
            }
        } catch (e: unknown) {
            clearTimeout(timeout);
            if(e instanceof Error && e.name === "AbortError"){
                eventStore().dispatch("Timeout", "请求超时");
            }
            reject(e);
        }
    });
};

interface UsePost {
    <D = unknown>(url: string): Promise<D>;

    <D = unknown>(url: string, parameters: FormData): Promise<D>;
    <D = unknown>(url: string, parameters: FormData, headers: Headers): Promise<D>;

    <D = unknown>(url: string, parameters: URLSearchParams): Promise<D>;
    <D = unknown>(url: string, parameters: URLSearchParams, headers: Headers): Promise<D>;

    <D = unknown>(url: string, parameters: object): Promise<D>;
    <D = unknown>(url: string, parameters: object, headers: Headers): Promise<D>;
}
export const usePost: UsePost = <D = unknown>(url: string, parameters?: FormData | URLSearchParams | object, headers?: Headers): Promise<D> => {
    return new Promise<D>((resolve, reject): void => {
        if (validate(parameters)) {
            if (parameters instanceof FormData) {
                try {
                    window.fetch(url, {
                        method: "POST",
                        body: parameters,
                        credentials: "include",
                        headers: headers || new Headers()
                    }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
                } catch (e) {
                    reject(e);
                }
            } else if (parameters instanceof URLSearchParams) {
                try {
                    window.fetch(url, {
                        method: "POST",
                        body: parameters,
                        credentials: "include",
                        headers: headers || new Headers({
                            "Content-Type": "application/x-www-form-urlencoded"
                        })
                    }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
                } catch (e) {
                    reject(e);
                }
            } else if (isObject(parameters)) {
                try {
                    let form: URLSearchParams = new URLSearchParams();
                    for (let key in parameters) {
                        let value: unknown = Reflect.get(parameters, key);
                        if (isPrimitive(value)) {
                            form.append(key, String(value));
                        }
                    }
                    window.fetch(url, {
                        method: "POST",
                        body: form,
                        credentials: "include",
                        headers: headers || new Headers({
                            "Content-Type": "application/x-www-form-urlencoded"
                        })
                    }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
                } catch (e) {
                    reject(e);
                }
            }
        } else {
            try {
                window.fetch(url, {
                    method: "POST",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            } catch (e) {
                reject(e);
            }
        }
    });
};

interface UsePut {
    <D = unknown>(url: string): Promise<D>;

    <D = unknown>(url: string, parameters: FormData): Promise<D>;
    <D = unknown>(url: string, parameters: FormData, headers: Headers): Promise<D>;

    <D = unknown>(url: string, parameters: URLSearchParams): Promise<D>;
    <D = unknown>(url: string, parameters: URLSearchParams, headers: Headers): Promise<D>;
}
export const usePut: UsePut = <D = unknown>(url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<D> => {
    return new Promise<D>((resolve, reject): void => {
        try {
            if (validate(parameters)) {
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "PUT",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            } else {
                window.fetch(url, {
                    method: "PUT",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            }
        } catch (e) {
            reject(e);
        }
    });
};

interface UseDelete {
    <D = unknown>(url: string): Promise<D>;

    <D = unknown>(url: string, parameters: FormData): Promise<D>;
    <D = unknown>(url: string, parameters: FormData, headers: Headers): Promise<D>;

    <D = unknown>(url: string, parameters: URLSearchParams): Promise<D>;
    <D = unknown>(url: string, parameters: URLSearchParams, headers: Headers): Promise<D>;
}
export const useDelete: UseDelete = <D = unknown>(url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<D> => {
    return new Promise<D>((resolve: Consumer<D>, reject: Consumer<unknown>) => {
        try {
            if (validate(parameters)) {
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "DELETE",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            } else {
                window.fetch(url, {
                    method: "DELETE",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            }
        } catch (e) {
            reject(e);
        }
    });

};