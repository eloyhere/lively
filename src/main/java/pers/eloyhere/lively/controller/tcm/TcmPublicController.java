package pers.eloyhere.lively.controller.tcm;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pers.eloyhere.lively.annotation.Everyone;
import pers.eloyhere.lively.entity.tcm.TcmQuestion;
import pers.eloyhere.lively.service.tcm.TcmQuestionService;

import java.util.Collection;
import java.util.UUID;

/**
 * 中医刷题公开 API - 供前端刷题页面使用
 * 与 TcmQuestionController（管理后台）分离，便于独立控制权限和缓存
 */
@RestController("tcmPublicController")
@RequestMapping("/tcm/public/question")
public class TcmPublicController {

    private final TcmQuestionService tcmQuestionService;

    public TcmPublicController(TcmQuestionService tcmQuestionService) {
        this.tcmQuestionService = tcmQuestionService;
    }

    /**
     * 获取所有题目（刷题页面加载题库）
     */
    @Everyone
    @GetMapping("findAll")
    public ResponseEntity<Collection<TcmQuestion>> findAll() {
        return ResponseEntity.ok(tcmQuestionService.findAll());
    }

    /**
     * 按科目获取题目
     */
    @Everyone
    @GetMapping("findBySubject")
    public ResponseEntity<Collection<TcmQuestion>> findBySubject(
            @RequestParam String subject) {
        TcmQuestion query = new TcmQuestion();
        query.setSubject(subject);
        return ResponseEntity.ok(tcmQuestionService.findAllBy(query));
    }

    /**
     * 按科目和题型获取题目
     */
    @Everyone
    @GetMapping("findBySubjectAndType")
    public ResponseEntity<Collection<TcmQuestion>> findBySubjectAndType(
            @RequestParam String subject,
            @RequestParam String type) {
        TcmQuestion query = new TcmQuestion();
        query.setSubject(subject);
        query.setType(type);
        return ResponseEntity.ok(tcmQuestionService.findAllBy(query));
    }

    /**
     * 根据题目数量统计
     */
    @Everyone
    @GetMapping("count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(tcmQuestionService.count());
    }

    /**
     * 按科目统计题目数量
     */
    @Everyone
    @GetMapping("countBySubject")
    public ResponseEntity<Long> countBySubject(@RequestParam String subject) {
        TcmQuestion query = new TcmQuestion();
        query.setSubject(subject);
        return ResponseEntity.ok(tcmQuestionService.countBy(query));
    }
}
