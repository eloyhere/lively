
export const fetchPicture: (url: string) => Promise<string> = async (url: string): Promise<string> => {
    const response = await window.fetch(url);
    return response.blob().then(URL.createObjectURL);
};