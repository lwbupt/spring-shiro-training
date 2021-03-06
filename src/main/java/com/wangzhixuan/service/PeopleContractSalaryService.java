package com.wangzhixuan.service;

import com.wangzhixuan.model.PeopleContractSalary;
import com.wangzhixuan.utils.PageInfo;
import com.wangzhixuan.vo.PeopleContractSalaryVo;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by sterm on 2017/1/13.
 */
public interface PeopleContractSalaryService {

    void findDataGrid(PageInfo pageInfo, HttpServletRequest request);

    void addSalary(PeopleContractSalary peopleSalary);

    void updateSalary(PeopleContractSalary peopleSalary);

    void deleteSalaryById(Long id);

    PeopleContractSalaryVo findPeopleContractSalaryVoById(Long id);
}
