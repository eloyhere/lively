package pers.eloyhere.lively.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pers.eloyhere.lively.entity.log.Visit;
import pers.eloyhere.lively.service.log.VisitService;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VisitAspect implements BaseAspect{

    private final VisitService visitService;


    public VisitAspect(VisitService visitService) {
        this.visitService = visitService;
    }


    @Override
    @Pointcut("execution(* pers.eloyhere.lively.controller..*.*(..)) || @within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController) && (!execution(* pers.eloyhere.lively.controller.log..*.*(..)))")
    public void pointcut() {

    }

    @Override
    @After("pointcut()")
    public void after(JoinPoint point) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(Objects.nonNull(attributes)){
            HttpServletRequest request = attributes.getRequest();
            HttpSession session = request.getSession(false);
            if(Objects.isNull(session.getAttribute("visited"))){
                session.setAttribute("visited", "true");
                Visit visit = new Visit();
                Supplier<String> address = () -> {
                    String ip = request.getHeader("X-Forwarded-For");

                    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("Proxy-Client-IP");
                    }

                    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("WL-Proxy-Client-IP");
                    }

                    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("HTTP_CLIENT_IP");
                    }

                    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                    }

                    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
                        ip = request.getRemoteAddr();
                    }
                    if (ip != null && ip.contains(",")) {
                        ip = ip.split(",")[0].trim();
                    }
                    return ip;
                };
                visit.setIp(address.get());

                Supplier<String> system = ()-> {
                    String userAgent = request.getHeader("User-Agent");
                    if (userAgent == null) {
                        return "Unknown";
                    }else{
                        userAgent = userAgent.toLowerCase();
                        if (userAgent.contains("windows")) {
                            if (userAgent.contains("windows nt 10.0") || userAgent.contains("windows 10")) {
                                return "Windows 10";
                            } else if (userAgent.contains("windows nt 6.3") || userAgent.contains("windows 8.1")) {
                                return "Windows 8.1";
                            } else if (userAgent.contains("windows nt 6.2") || userAgent.contains("windows 8")) {
                                return "Windows 8";
                            } else if (userAgent.contains("windows nt 6.1") || userAgent.contains("windows 7")) {
                                return "Windows 7";
                            } else if (userAgent.contains("windows nt 6.0")) {
                                return "Windows Vista";
                            } else if (userAgent.contains("windows nt 5.1") || userAgent.contains("windows xp")) {
                                return "Windows XP";
                            } else if (userAgent.contains("windows nt 5.0")) {
                                return "Windows 2000";
                            } else {
                                return "Windows";
                            }
                        } else if (userAgent.contains("mac os x") || userAgent.contains("macintosh")) {
                            if (userAgent.contains("mac os x 10_15") || userAgent.contains("catalina")) {
                                return "macOS Catalina";
                            } else if (userAgent.contains("mac os x 10_14") || userAgent.contains("mojave")) {
                                return "macOS Mojave";
                            } else if (userAgent.contains("mac os x 10_13") || userAgent.contains("high sierra")) {
                                return "macOS High Sierra";
                            } else if (userAgent.contains("mac os x 10_12") || userAgent.contains("sierra")) {
                                return "macOS Sierra";
                            } else {
                                return "macOS";
                            }
                        } else if (userAgent.contains("linux")) {
                            if (userAgent.contains("ubuntu")) {
                                return "Ubuntu Linux";
                            } else if (userAgent.contains("fedora")) {
                                return "Fedora Linux";
                            } else if (userAgent.contains("debian")) {
                                return "Debian Linux";
                            } else if (userAgent.contains("android")) {
                                return "Android";
                            } else {
                                return "Linux";
                            }
                        } else if (userAgent.contains("android")) {
                            Pattern pattern = Pattern.compile("android (\\d+(\\.\\d+)*)");
                            Matcher matcher = pattern.matcher(userAgent);
                            if (matcher.find()) {
                                return "Android " + matcher.group(1);
                            }
                            return "Android";
                        } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) {
                            Pattern pattern = Pattern.compile("os (\\d+_\\d+_?\\d*)");
                            Matcher matcher = pattern.matcher(userAgent);
                            if (matcher.find()) {
                                return "iOS " + matcher.group(1).replace("_", ".");
                            }
                            return "iOS";
                        } else if (userAgent.contains("freebsd")) {
                            return "FreeBSD";
                        } else if (userAgent.contains("openbsd")) {
                            return "OpenBSD";
                        } else if (userAgent.contains("netbsd")) {
                            return "NetBSD";
                        } else if (userAgent.contains("sunos")) {
                            return "Solaris";
                        } else if (userAgent.contains("aix")) {
                            return "AIX";
                        } else if (userAgent.contains("hp-ux")) {
                            return "HP-UX";
                        } else if (userAgent.contains("blackberry")) {
                            return "BlackBerry OS";
                        } else if (userAgent.contains("symbian")) {
                            return "Symbian OS";
                        }
                    }
                    return "Unknown";
                };
                visit.setSystem(system.get());

                Supplier<String> platform = () -> {
                    String userAgent = request.getHeader("User-Agent");
                    if (userAgent == null) {
                        return "Unknown";
                    }
                    userAgent = userAgent.toLowerCase();
                    if (userAgent.contains("mobile") || userAgent.contains("phone")) {
                        return "Mobile";
                    } else if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
                        return "Tablet";
                    } else if (userAgent.contains("tv") || userAgent.contains("smart-tv") || userAgent.contains("smarttv")) {
                        return "TV";
                    } else if (userAgent.contains("bot") || userAgent.contains("crawler") || userAgent.contains("spider")) {
                        return "Bot";
                    } else if (userAgent.contains("watch") || userAgent.contains("wearable")) {
                        return "Wearable";
                    } else if (userAgent.contains("console") || userAgent.contains("playstation") ||
                            userAgent.contains("xbox") || userAgent.contains("nintendo")) {
                        return "Game Console";
                    } else if (userAgent.contains("car") || userAgent.contains("automotive")) {
                        return "Automotive";
                    } else if (userAgent.contains("desktop")) {
                        return "Desktop";
                    } else if (userAgent.contains("android") && !userAgent.contains("mobile")) {
                        return "Android Tablet";
                    } else {
                        if (userAgent.contains("windows") || userAgent.contains("mac") || userAgent.contains("linux") &&
                                !userAgent.contains("android") && !userAgent.contains("iphone") && !userAgent.contains("ipad")) {
                            return "Desktop";
                        } else if (userAgent.contains("android") || userAgent.contains("iphone")) {
                            return "Mobile";
                        }
                    }
                    return "Desktop";
                };
                visit.setPlatform(platform.get());

                Supplier<String> client = () -> {
                    String userAgent = request.getHeader("User-Agent");
                    if (userAgent == null) {
                        return "Unknown";
                    }
                    userAgent = userAgent.toLowerCase();
                    if (userAgent.contains("edge") || userAgent.contains("edg/")) {
                        Pattern pattern = Pattern.compile("edg(?:e|a|ios)?[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Microsoft Edge " + matcher.group(1);
                        }
                        return "Microsoft Edge";
                    } else if (userAgent.contains("chrome") && !userAgent.contains("chromium") && !userAgent.contains("edg")) {
                        Pattern pattern = Pattern.compile("chrome[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Chrome " + matcher.group(1);
                        }
                        return "Chrome";
                    } else if (userAgent.contains("firefox") || userAgent.contains("fxios")) {
                        Pattern pattern = Pattern.compile("firefox[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Firefox " + matcher.group(1);
                        }
                        return "Firefox";
                    } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
                        Pattern pattern = Pattern.compile("version[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Safari " + matcher.group(1);
                        }
                        return "Safari";
                    } else if (userAgent.contains("opera") || userAgent.contains("opr/")) {
                        Pattern pattern = Pattern.compile("(?:opera|opr)[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Opera " + matcher.group(1);
                        }
                        return "Opera";
                    } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
                        if (userAgent.contains("msie 10")) {
                            return "Internet Explorer 10";
                        } else if (userAgent.contains("msie 9")) {
                            return "Internet Explorer 9";
                        } else if (userAgent.contains("msie 8")) {
                            return "Internet Explorer 8";
                        } else if (userAgent.contains("msie 7")) {
                            return "Internet Explorer 7";
                        } else if (userAgent.contains("msie 6")) {
                            return "Internet Explorer 6";
                        } else {
                            return "Internet Explorer";
                        }
                    } else if (userAgent.contains("ucbrowser") || userAgent.contains("uc browser")) {
                        Pattern pattern = Pattern.compile("ucbrowser[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "UC Browser " + matcher.group(1);
                        }
                        return "UC Browser";
                    } else if (userAgent.contains("qqbrowser") || userAgent.contains("qq/")) {
                        Pattern pattern = Pattern.compile("qqbrowser[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "QQ Browser " + matcher.group(1);
                        }
                        return "QQ Browser";
                    } else if (userAgent.contains("wechat")) {
                        Pattern pattern = Pattern.compile("micromessenger[\\s/](\\d+(\\.\\d+)*)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "WeChat " + matcher.group(1);
                        }
                        return "WeChat";
                    } else if (userAgent.contains("baidu")) {
                        return "Baidu Browser";
                    } else if (userAgent.contains("sogou")) {
                        return "Sogou Browser";
                    } else if (userAgent.contains("2345explorer")) {
                        return "2345 Browser";
                    } else if (userAgent.contains("maxthon")) {
                        return "Maxthon";
                    } else if (userAgent.contains("vivaldi")) {
                        return "Vivaldi";
                    } else if (userAgent.contains("yabrowser") || userAgent.contains("yandex")) {
                        return "Yandex Browser";
                    } else if (userAgent.contains("samsungbrowser")) {
                        return "Samsung Internet";
                    } else if (userAgent.contains("miuibrowser")) {
                        return "MIUI Browser";
                    }
                    return "Unknown Browser";
                };
                visit.setClient(client.get());

                Supplier<String> device = () -> {
                    String userAgent = request.getHeader("User-Agent");
                    if (userAgent == null) {
                        return "Unknown";
                    }
                    userAgent = userAgent.toLowerCase();
                    if (userAgent.contains("iphone")) {
                        Pattern pattern = Pattern.compile("iphone(?:\\s+)?(?:os)?[\\s_]?(\\d+)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "iPhone " + matcher.group(1);
                        }
                        return "iPhone";
                    } else if (userAgent.contains("ipad")) {
                        Pattern pattern = Pattern.compile("ipad(?:\\s+)?(?:os)?[\\s_]?(\\d+)");
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "iPad " + matcher.group(1);
                        }
                        return "iPad";
                    } else if (userAgent.contains("ipod")) {
                        return "iPod";
                    } else if (userAgent.contains("macbook")) {
                        return "MacBook";
                    } else if (userAgent.contains("imac")) {
                        return "iMac";
                    } else if (userAgent.contains("mac pro")) {
                        return "Mac Pro";
                    } else if (userAgent.contains("mac mini")) {
                        return "Mac mini";
                    }
                    if (userAgent.contains("android")) {
                        if (userAgent.contains("samsung") || userAgent.contains("sm-")) {
                            if (userAgent.contains("galaxy")) {
                                Pattern pattern = Pattern.compile("galaxy[\\s_]([a-z0-9]+)", Pattern.CASE_INSENSITIVE);
                                Matcher matcher = pattern.matcher(userAgent);
                                if (matcher.find()) {
                                    return "Samsung Galaxy " + matcher.group(1);
                                }
                            }
                            return "Samsung Device";
                        }
                        else if (userAgent.contains("huawei") || userAgent.contains("honor")) {
                            if (userAgent.contains("mate")) {
                                return "Huawei Mate";
                            } else if (userAgent.contains("p") && userAgent.matches(".*p\\d+.*")) {
                                Pattern pattern = Pattern.compile("p(\\d+)");
                                Matcher matcher = pattern.matcher(userAgent);
                                if (matcher.find()) {
                                    return "Huawei P" + matcher.group(1);
                                }
                            }
                            return "Huawei Device";
                        }
                        else if (userAgent.contains("mi ") || userAgent.contains("redmi") || userAgent.contains("xiaomi")) {
                            if (userAgent.contains("mi ")) {
                                Pattern pattern = Pattern.compile("mi[\\s_](\\d+)");
                                Matcher matcher = pattern.matcher(userAgent);
                                if (matcher.find()) {
                                    return "Xiaomi Mi " + matcher.group(1);
                                }
                            }
                            return "Xiaomi Device";
                        }
                        else if (userAgent.contains("oppo")) {
                            return "OPPO Device";
                        }
                        else if (userAgent.contains("vivo")) {
                            return "VIVO Device";
                        }
                        else if (userAgent.contains("oneplus")) {
                            Pattern pattern = Pattern.compile("oneplus[\\s_](\\d+)");
                            Matcher matcher = pattern.matcher(userAgent);
                            if (matcher.find()) {
                                return "OnePlus " + matcher.group(1);
                            }
                            return "OnePlus Device";
                        }
                        else if (userAgent.contains("pixel")) {
                            Pattern pattern = Pattern.compile("pixel[\\s_](\\d+)");
                            Matcher matcher = pattern.matcher(userAgent);
                            if (matcher.find()) {
                                return "Google Pixel " + matcher.group(1);
                            }
                            return "Google Pixel";
                        }
                        return "Android Device";
                    }
                    if (userAgent.contains("surface")) {
                        Pattern pattern = Pattern.compile("surface[\\s_]([a-z0-9]+)", Pattern.CASE_INSENSITIVE);
                        Matcher matcher = pattern.matcher(userAgent);
                        if (matcher.find()) {
                            return "Microsoft Surface " + matcher.group(1);
                        }
                        return "Microsoft Surface";
                    }
                    return "Unknown Device";
                };
                visit.setDevice(device.get());

                //visitService.insert(visit);
            }
        }

    }
}
