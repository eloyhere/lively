package pers.eloyhere.lively.controller;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.annotation.Authenticated;
import pers.eloyhere.lively.annotation.Everyone;
import pers.eloyhere.lively.annotation.Guest;
import pers.eloyhere.lively.annotation.Unauthenticated;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.entity.consumer.Invitation;
import pers.eloyhere.lively.entity.consumer.Token;
import pers.eloyhere.lively.service.authentication.LivelyPersistentTokenBasedRememberMeServices;
import pers.eloyhere.lively.service.consumer.ConsumerService;
import pers.eloyhere.lively.service.consumer.InvitationService;
import pers.eloyhere.lively.service.consumer.TokenService;

import javax.security.auth.login.CredentialNotFoundException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController("authenticationController")
@RequestMapping("authentication")
class AuthenticationController {

    private final SecurityContextRepository repository;

    private final ConsumerService consumerService;

    private final InvitationService invitationService;

    private final HttpServletRequest request;

    private final HttpServletResponse response;

    private final LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices;

    AuthenticationController(SecurityContextRepository repository, ConsumerService consumerService, InvitationService invitationService, HttpServletRequest request, HttpServletResponse response, LivelyPersistentTokenBasedRememberMeServices livelyPersistentTokenBasedRememberMeServices) {
        this.repository = repository;
        this.consumerService = consumerService;
        this.invitationService = invitationService;
        this.request = request;
        this.response = response;
        this.livelyPersistentTokenBasedRememberMeServices = livelyPersistentTokenBasedRememberMeServices;
    }

    @Everyone
    @GetMapping("auto")
    public ResponseEntity<Authentication> auto(){
        try{
            Authentication authentication = livelyPersistentTokenBasedRememberMeServices.autoLogin(request, response);
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(authentication);
            strategy.setContext(context);
            repository.saveContext(context, request, response);
            return ResponseEntity.ok(authentication);
        }catch (AuthenticationException exception){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (Exception exception){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @MessageMapping("heartbeat")
    @SendTo("/queue/authentication")
    public Principal heartbeat(Principal principal){
        return principal;
    }

    @Authenticated
    @RequestMapping("identity")
    public ResponseEntity<Authentication> identity(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        return ResponseEntity.ok(authentication);
    }

    @Unauthenticated
    @PostMapping(value = "register")
    public ResponseEntity<Authentication> register(Consumer consumer, @RequestParam String invitation){
        Invitation example = new Invitation();
        example.setCode(invitation);
        Optional<Invitation> entity = invitationService.findOneBy(example);
        if(entity.isPresent()){
            LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Shanghai"));
            Invitation value = entity.get();
            value.setLock(now.plusYears(100));
            invitationService.update(value);

            Consumer trust = consumerService.insert(consumer);
            UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.authenticated(consumer, consumer.getPassword(), trust.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(token);
            SecurityContextHolder.setContext(context);
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();
            strategy.setContext(context);
            repository.saveContext(context, request, response);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("device")
    public ResponseEntity<Map<String, Object>> device(){
        Map<String, Object> map = new HashMap<>();
        map.put("protocol", request.getProtocol());
        map.put("method", request.getMethod());
        map.put("serverName", request.getServerName());
        map.put("serverPort", request.getServerPort());
        map.put("contextPath", request.getContextPath());
        map.put("remoteHost", request.getRemoteHost());
        map.put("remotePort", request.getRemotePort());
        map.put("locale", request.getLocale());
        map.put("characterEncoding", request.getCharacterEncoding());
        map.put("timezone", request.getHeader("Time-Zone"));
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
        map.put("ip", address.get());

        Supplier<String> system = ()-> {
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                map.put("system", "Unknown");
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
        map.put("os", system.get());

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
        map.put("platform", platform.get());

        Supplier<String> browser = () -> {
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
        map.put("browser", browser.get());

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
        map.put("device", device.get());
        return ResponseEntity.ok(map);
    }

    @Authenticated
    @GetMapping(value = "expire")
    public ResponseEntity<String> expire(){
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(value = "lock")
    public ResponseEntity<String> lock(){
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "ban")
    public ResponseEntity<String> ban(){
        return ResponseEntity.ok(null);
    }
}
