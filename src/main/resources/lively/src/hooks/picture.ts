
export const fetchPicture: (url: string) => Promise<string> = async (url: string): Promise<string> => {
    const response = await window.fetch(url);
    const blob = await response.blob();
    return URL.createObjectURL(blob);
};