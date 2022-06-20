package com.mlf.yygh.cmn.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by Administrator on 2021/10/5.
 */
@FeignClient("service-cmn")
@Repository
public interface DictFeignClient {
    //根据dictcode和value查询返回name值(间接获取医院等级名称  name)
    @GetMapping("/admin/cmn/dict/getName/{dictCode}/value")
    String getName(@PathVariable("dictCode") String dictCode,
                          @PathVariable("value") String value);

    //根据value查询，直接查询name值，
    @GetMapping("/admin/cmn/dict/getName/value")
    String getName(@PathVariable("value") String value);

}
