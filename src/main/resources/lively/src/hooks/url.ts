import {isString, type Supplier, validate} from "semantic-typescript";

export interface UseDataUrl{
    (file: File): Promise<string>;
    (blob: Blob): Promise<string>;
}
export const useDataUrl: UseDataUrl = (parameter: Blob | File): Promise<string> =>{
    return new Promise<string>((resolve, reject) => {
        let reader: FileReader = new FileReader();
        reader.readAsDataURL(parameter);
        reader.addEventListener("load", (event: ProgressEvent<FileReader>) => {
            if(validate(reader.result) && isString(reader.result) && reader.result.length > 0){
                resolve(reader.result);
            }else{
                reject();
            }
        });
    })
};

export interface UseOrigin{
    (): string;
    (protocol: string): string;
}
export const useOrigin: UseOrigin = (protocol?: string): string => {
    if(isString(protocol)){
        return protocol + "://" + window.location.host;
    }
    return window.location.origin;
};