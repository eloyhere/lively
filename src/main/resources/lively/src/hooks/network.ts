import { type Consumer, isFunction, isObject, isPrimitive, type MaybeInvalid, validate } from "semantic-typescript";
import { useSerialization } from "@/hooks/serialization.ts";
import type { Serializer } from "@/declaration/serialization.ts";
import { authenticationStore } from "@/stores/authentication";

const listener: Array<Consumer<Response>> = [
    (response: Response): void => {
        if (response.status === 401) {
            authenticationStore().expire();
        }
    }
];
export const useListenResponse = (callback: Consumer<Response>): void => {
    listener.push(callback);
};
interface UseResponse {
    <D = unknown>(response: Response): Promise<D>;
}
export const useResponse: UseResponse = <D>(response: Response): Promise<D> => {
    return new Promise<D>((resolve, reject): void => {
        try{
            listener.forEach((callback) => {
                if(isFunction(callback)){
                    callback(response);
                }
            });
        }catch(e){

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
        try {
            if (validate(parameters)) {
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "GET",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            } else {
                window.fetch(url, {
                    method: "GET",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response: Response) => useResponse<D>(response)).then(resolve, reject);
            }
        } catch (e) {
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