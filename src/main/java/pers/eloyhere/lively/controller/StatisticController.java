package pers.eloyhere.lively.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.eloyhere.lively.annotation.Administrator;
import pers.eloyhere.lively.entity.consumer.Consumer;
import pers.eloyhere.lively.service.consumer.ConsumerService;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("statistic")
class StatisticController {

    private final ConsumerService consumerService;

    StatisticController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Administrator
    @GetMapping("consumer")
    public ResponseEntity<Map<String, Object>> consumer(){
        Map<String, Object> map = new HashMap<>();
        LocalDateTime now = LocalDateTime.now(Clock.systemDefaultZone());
        map.put("yearly", consumerService.findSpawnBetween(now.minusYears(10), now).stream().collect(Collectors.groupingBy(consumer -> consumer.getSpawn().getYear(), Collectors.counting())));
        map.put("monthly", consumerService.findSpawnBetween(now.minusYears(1), now).stream().collect(Collectors.groupingBy(consumer -> consumer.getSpawn().getMonthValue(), Collectors.counting())));
        map.put("weekly", consumerService.findSpawnBetween(now.minusWeeks(2), now).stream().collect(Collectors.groupingBy(consumer -> {
            ChronoField field = ChronoField.ALIGNED_WEEK_OF_MONTH;
            return consumer.getSpawn().get(field);
        }, Collectors.counting())));
        map.put("daily", consumerService.findSpawnBetween(now.minusDays(7), now).stream().collect(Collectors.groupingBy(consumer -> consumer.getSpawn().getDayOfMonth(), Collectors.counting())));
        map.put("hourly", consumerService.findSpawnBetween(now.minusDays(1), now).stream().collect(Collectors.groupingBy(consumer -> consumer.getSpawn().getHour(), Collectors.counting())));
        return ResponseEntity.ok(map);
    }
}
