package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.service.DishService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishServiceImpl implements DishService {
    /**
     * 新增菜品和口味
     * @param dishDTO
     */
    //开启事务注解
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        //TODO 完成新增菜品

        //向菜品表插入一条数据
        //向口味表插入n条数据
    }
}
