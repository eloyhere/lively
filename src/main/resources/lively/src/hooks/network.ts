import {type Consumer, isObject, isPrimitive, validate} from "semantic-typescript";

interface UseGet{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;
}
export const useGet: UseGet = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
        try{
            if(validate(parameters)){
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "GET",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            } else{
                window.fetch(url, {
                    method: "GET",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            }
        }catch (e) {
            reject(e);
        }
    });
};

interface UsePost{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;

    (url: string, parameters: object): Promise<Response>;
    (url: string, parameters: object, headers: Headers): Promise<Response>;
}
export const usePost: UsePost = (url: string, parameters?: FormData | URLSearchParams | object, headers?: Headers): Promise<Response> => {
    return new Promise<Response>((resolve, reject): void => {
        if(validate(parameters)){
            if(parameters instanceof FormData){
                try{
                    window.fetch(url, {
                        method: "POST",
                        body: parameters,
                        credentials: "include",
                        headers: headers || new Headers()
                    }).then((response) => {
                        if(response.status === 200){
                            resolve(response);
                        }else{
                            reject(response);
                        }
                    }, reject);
                }catch (e) {
                    reject(e);
                }
            }else if(parameters instanceof URLSearchParams){
                try {
                    window.fetch(url, {
                        method: "POST",
                        body: parameters,
                        credentials: "include",
                        headers: headers || new Headers({
                            "Content-Type": "application/x-www-form-urlencoded"
                        })
                    }).then((response) => {
                        if(response.status === 200){
                            resolve(response);
                        }else{
                            reject(response);
                        }
                    }, reject);
                }catch (e) {
                    reject(e);
                }
            }else if(isObject(parameters)){
                try {
                    let form: URLSearchParams = new URLSearchParams();
                    for(let key in parameters){
                        let value: unknown = Reflect.get(parameters, key);
                        if(isPrimitive(value)){
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
                    }).then((response) => {
                        if(response.status === 200){
                            resolve(response);
                        }else{
                            reject(response);
                        }
                    }, reject);
                }catch (e) {
                    reject(e);
                }
            }
        }else{
            try{
                window.fetch(url, {
                    method: "POST",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            }catch (e) {
                reject(e);
            }
        }
    });

};

interface UsePut{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;
}
export const usePut: UsePut = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    return new Promise<Response>((resolve, reject): void => {
        try {
            if(validate(parameters)){
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "PUT",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            } else {
                window.fetch(url, {
                    method: "PUT",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            }
        }catch (e) {
            reject(e);
        }
    });
};

interface UseDelete{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;
}
export const useDelete: UseDelete = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
        try {
            if(validate(parameters)){
                window.fetch(`${url}?${parameters.toString()}`, {
                    method: "DELETE",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            } else{
                window.fetch(url, {
                    method: "DELETE",
                    credentials: "include",
                    headers: headers || new Headers()
                }).then((response) => {
                    if(response.status === 200){
                        resolve(response);
                    }else{
                        reject(response);
                    }
                }, reject);
            }
        }catch (e) {
            reject(e);
        }
    });

};