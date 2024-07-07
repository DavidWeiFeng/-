package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j

public class ShopController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value(value = "${sky.shop.status.key}")
    private String KEY;
    @PutMapping("/{status}")
    public Result setStatus(@PathVariable Integer status) {
        log.info("店铺status: {}", status);
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取店铺状态为：{}",status==1?"开":"关");
        return Result.success(status);
    }

}
