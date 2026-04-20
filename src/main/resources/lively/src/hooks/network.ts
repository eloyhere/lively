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
            let link: URL = new URL(url);
            if(validate(parameters)){
                for (const [k, v] of parameters){
                    if(isPrimitive(v)){
                        link.searchParams.append(k, v);
                    }
                }
                window.fetch(link, {
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
                window.fetch(link, {
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
    let link: URL = new URL(url);
    if(validate(parameters)){
        if(parameters instanceof FormData){
            return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
                try{
                    window.fetch(link, {
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
            });
        }else if(parameters instanceof URLSearchParams){
            return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
               try {
                   window.fetch(link, {
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
            });
        }else if(isObject(parameters)){
            return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
                try {
                    let form: URLSearchParams = new URLSearchParams();
                    for(let key in parameters){
                        let value: unknown = Reflect.get(parameters, key);
                        if(isPrimitive(value)){
                            form.append(key, String(value));
                        }
                    }
                    window.fetch(link, {
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
            });
        }
    }
    return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
        try{
            window.fetch(link, {
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
    return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
        try {
            let link: URL = new URL(url);
            if(validate(parameters)){
                for (const [k, v] of parameters){
                    if(isPrimitive(v)){
                        link.searchParams.append(k, v);
                    }
                }
                window.fetch(link, {
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
                window.fetch(link, {
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
export const useDelete: UseGet = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    return new Promise<Response>((resolve: Consumer<Response>, reject: Consumer<unknown>) => {
        try {
            let link: URL = new URL(url);
            if(validate(parameters)){
                for (const [k, v] of parameters){
                    if(isPrimitive(v)){
                        link.searchParams.append(k, v);
                    }
                }
                window.fetch(link, {
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
                window.fetch(link, {
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