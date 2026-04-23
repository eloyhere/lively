import type {Functional} from "semantic-typescript";

export interface UseDateFormat{
    (date: Date): string;
    (date: Date, format: string): string;
}
export const useDateFormat: UseDateFormat = (date: Date, format?: string): string => {
    const formatter: string = format || "YYYY-MM-DD HH:mm:ss";
    if (isNaN(date.getTime())) {
        throw new TypeError("Invalid date.");
    }
    const year: number = date.getFullYear();
    const month: number = date.getMonth() + 1;
    const day: number = date.getDate();
    const hours: number = date.getHours();
    const minutes: number = date.getMinutes();
    const seconds: number = date.getSeconds();
    const padZero: Functional<number, string> = (value: number): string => value.toString().padStart(2, "0");
    const replacements: Record<string, string> = {
        "YYYY": year.toString(),
        "YY": year.toString().slice(-2),
        "MM": padZero(month),
        "M": month.toString(),
        "DD": padZero(day),
        "D": day.toString(),
        "HH": padZero(hours),
        "H": hours.toString(),
        "hh": padZero(hours % 12 || 12), // 12小时制
        "h": (hours % 12 || 12).toString(),
        "mm": padZero(minutes),
        "m": minutes.toString(),
        "ss": padZero(seconds),
        "s": seconds.toString(),
        "A": hours < 12 ? "AM" : "PM",
        "a": hours < 12 ? "am" : "pm",
    };
    let formatted = formatter;
    for (const [pattern, replacement] of Object.entries(replacements)) {
        formatted = formatted.replace(new RegExp(pattern, 'g'), replacement);
    }
    return formatted;
}