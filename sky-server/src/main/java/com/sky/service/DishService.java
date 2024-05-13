package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和口味数据
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根據主鍵批量刪除菜品數據
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根據主鍵調整菜品狀態
     * @param status
     * @param dishId
     */

    void startOrStop(Integer status, Long dishId);


    DishVO getByIdWithFlavors(Long id);

    /**
     * 修改菜品信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);
}
