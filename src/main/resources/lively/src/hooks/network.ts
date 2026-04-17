import {isPrimitive, validate} from "semantic-typescript";

interface UseGet{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;
}
export const useGet: UseGet = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    console.log(url);
    let link: URL = new URL(url);
    if(validate(parameters)){
        for (const [k, v] of parameters){
            if(isPrimitive(v)){
                link.searchParams.append(k, v);
            }
        }
        return window.fetch(link, {
            method: "GET",
            headers: headers || new Headers({
                Cookie: window.document.cookie
            })
        });
    }
    return window.fetch(link, {
        method: "GET",
        headers: headers || new Headers({
            Cookie: window.document.cookie
        })
    });
};

interface UsePost{
    (url: string): Promise<Response>;

    (url: string, parameters: FormData): Promise<Response>;
    (url: string, parameters: FormData, headers: Headers): Promise<Response>;

    (url: string, parameters: URLSearchParams): Promise<Response>;
    (url: string, parameters: URLSearchParams, headers: Headers): Promise<Response>;
}
export const usePost: UsePost = (url: string, parameters?: FormData | URLSearchParams, headers?: Headers): Promise<Response> => {
    let link: URL = new URL(url);
    if(validate(parameters)){
        if(parameters instanceof FormData){
            return window.fetch(link, {
                method: "POST",
                body: parameters,
                headers: headers || new Headers({
                    Cookie: window.document.cookie,
                })
            });
        }
        return window.fetch(link, {
            method: "POST",
            body: parameters,
            headers: headers || new Headers({
                Cookie: window.document.cookie,
                "Content-Type": "application/x-www-form-urlencoded"
            })
        });
    }
    return window.fetch(link, {
        method: "POST",
        headers: headers || new Headers({
            Cookie: window.document.cookie,
            "Content-Type": "application/x-www-form-urlencoded"
        })
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
    let link: URL = new URL(url);
    if(validate(parameters)){
        for (const [k, v] of parameters){
            if(isPrimitive(v)){
                link.searchParams.append(k, v);
            }
        }
        return window.fetch(link, {
            method: "PUT",
            headers: headers || new Headers({
                Cookie: window.document.cookie
            })
        });
    }
    return window.fetch(link, {
        method: "PUT",
        headers: headers || new Headers({
            Cookie: window.document.cookie
        })
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
    let link: URL = new URL(url);
    if(validate(parameters)){
        for (const [k, v] of parameters){
            if(isPrimitive(v)){
                link.searchParams.append(k, v);
            }
        }
        return window.fetch(link, {
            method: "DELETE",
            headers: headers || new Headers({
                Cookie: window.document.cookie
            })
        });
    }
    return window.fetch(link, {
        method: "DELETE",
        headers: headers || new Headers({
            Cookie: window.document.cookie
        })
    });
};