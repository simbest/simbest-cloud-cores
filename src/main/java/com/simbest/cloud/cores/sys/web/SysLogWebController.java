/*
 * 版权所有 © 北京晟壁科技有限公司 2008-2027。保留一切权利!
 */
package com.simbest.cloud.cores.sys.web;

import com.simbest.cloud.cores.base.web.controller.GenericController;
import com.simbest.cloud.cores.response.JsonResponse;
import com.simbest.cloud.cores.sys.model.SysLogWeb;
import com.simbest.cloud.cores.sys.service.ISysLogWebService;
import com.simbest.cloud.cores.utils.DateUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用途：请求接口日志控制器
 * 作者: lishuyi
 * 时间: 2018/2/22  10:14
 */
@Tag(name = "SysLogWebController", description = "系统管理-请求接口日志管理")
@RestController
@RequestMapping("/sys/log/web")
public class SysLogWebController extends GenericController<SysLogWeb, String> {

    private ISysLogWebService service;

    @Autowired
    public SysLogWebController(ISysLogWebService service) {
        super(service);
        this.service = service;
    }

    @GetMapping(value = "/queryTodayFail")
    public JsonResponse queryTodayFail() {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.getCurrent()).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.getCurrent()).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return JsonResponse.success(service.findAllNoPage(specification));
    }

    @GetMapping(value = "/countTodayFail/anonymous")
    public Long countTodayFail() {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.getCurrent()).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.getCurrent()).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return service.count(specification);
    }



    @GetMapping(value = "/queryDayFail")
    public JsonResponse queryDayFail(String day) {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.parseDate(day)).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.parseDate(day)).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return JsonResponse.success(service.findAllNoPage(specification));
    }

    @GetMapping(value = "/countDayFail/anonymous")
    public Long countDayFail(String day) {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.parseDate(day)).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.parseDate(day)).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return service.count(specification);
    }



    @GetMapping(value = "/queryBetweenDayFail")
    public JsonResponse queryBetweenDayFail(String sDate, String eDate) {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.parseDate(sDate)).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.parseDate(eDate)).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return JsonResponse.success(service.findAllNoPage(specification));
    }

    @GetMapping(value = "/countBetweenDayFail/anonymous")
    public Long countBetweenDayFail(String sDate, String eDate) {
        Date startDate = DateUtil.startTimeOfDay(DateUtil.parseDate(sDate)).toDate();
        Date endDate = DateUtil.endTimeOfDay(DateUtil.parseDate(eDate)).toDate();
        Specification<SysLogWeb> specification = (root, query, cb) -> {
            Predicate failedPredicate = cb.equal(root.get("failed"), true);
            Predicate createdTimePredicate = cb.between(root.get("createdTime"), DateUtil.date2LocalDateTime(startDate), DateUtil.date2LocalDateTime(endDate));
            return cb.and(new Predicate[]{failedPredicate, createdTimePredicate});
        };
        return service.count(specification);
    }
}
