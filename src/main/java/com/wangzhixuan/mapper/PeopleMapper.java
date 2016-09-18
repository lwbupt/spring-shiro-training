package com.wangzhixuan.mapper;

import com.wangzhixuan.model.People;
import com.wangzhixuan.utils.PageInfo;

import java.util.List;

/**
 * Created by liushaoyang on 2016/9/8.
 */
public interface PeopleMapper {

    /**
     * 删除人员
     *
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 添加人员
     *
     * @param people
     * @return
     */
    int insert(People people);

    /**
     * 修改人员
     *
     * @param people
     * @return
     */
    int updatePeople(People people);

    /**
     * 根据人员名查询人员
     *
     * @param name
     * @return
     */
    People findPeopleByName(String name);

    /**
     * 根据人员id查询人员
     *
     * @param id
     * @return
     */
    People findPeopleById(Long id);

    /**
     * 人员列表
     *
     * @param pageInfo
     * @return
     */
    List findPeoplePageCondition(PageInfo pageInfo);

    /**
     * 统计人员
     *
     * @param pageInfo
     * @return
     */
    int findPeoplePageCount(PageInfo pageInfo);
}