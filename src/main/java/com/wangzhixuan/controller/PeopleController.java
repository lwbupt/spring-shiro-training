package com.wangzhixuan.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.google.common.collect.Maps;
import com.wangzhixuan.code.Result;
import com.wangzhixuan.model.People;
import com.wangzhixuan.service.PeopleService;
import com.wangzhixuan.utils.PageInfo;

/**
 * Created by liushaoyang on 2016/9/8.
 * 人员信息管理
 */

@Controller
@RequestMapping("/people")
public class PeopleController extends BaseController{
    private static Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);

    @Autowired
    private PeopleService peopleService;

    /**
     * 人员管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/admin/people/people";
    }

    /**
     * 人员管理列表
     *
     * @param people
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value="/dataGrid", method=RequestMethod.POST)
    @ResponseBody
    public PageInfo dataGrid(HttpServletRequest request, People people, Integer page, Integer rows, String sort, String order){
        PageInfo pageInfo = new PageInfo(page, rows);
        Map<String, Object> condition = Maps.newHashMap();


        if(StringUtils.isNoneBlank(people.getName())){
            condition.put("name", people.getName());
        }

        if(StringUtils.isNoneBlank(people.getJob())){
            condition.put("job", people.getJob());
        }

        if (people.getSex() != null){
            condition.put("sex", people.getSex());
        }

        if (StringUtils.isNoneBlank(request.getParameter("birthdayStart"))){
            condition.put("birthdayStart",request.getParameter("birthdayStart"));
        }

        if(StringUtils.isNoneBlank(request.getParameter("birthdayEnd"))){
            condition.put("birthdayEnd",request.getParameter("birthdayEnd"));
        }

        pageInfo.setCondition(condition);
        peopleService.findDataGrid(pageInfo);

        return pageInfo;
    }

    @RequestMapping(value="/addPage", method=RequestMethod.GET)
    public String addPage(){
        return "admin/people/peopleAdd";
    }
    @RequestMapping(value="/importExcelPage", method=RequestMethod.GET)
    public String importExcelPage(){
        return "admin/people/importExcelPage";
    }
    /**
     * 添加用户
     *
     * @param people
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
    public Result add(People people,@RequestParam(value="fileName",required=false)CommonsMultipartFile file) {
        Result result = new Result();
        try {
            peopleService.addPeople(people,file);
            result.setSuccess(true);
            result.setMsg("添加成功");
            return result;
        } catch (RuntimeException e) {
            LOGGER.error("添加用户失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/editPage")
    public String editPage(Long id, Model model){
        People people = peopleService.findPeopleById(id);
        model.addAttribute("people",people);
        return "/admin/people/peopleEdit";
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Result edit(People people, @RequestParam(value="fileName",required=false)CommonsMultipartFile file){
        Result result = new Result();
        try{
            peopleService.updatePeople(people,file);
            result.setSuccess(true);
            result.setMsg("修改成功!");
            return result;
        }catch(RuntimeException e){
            LOGGER.error("修改人员失败：{}",e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public Result delete(Long id){
        Result result = new Result();
        try{
            peopleService.deletePeopleById(id);
            result.setMsg("删除成功！");
            result.setSuccess(true);
            return result;
        }catch(RuntimeException e){
            LOGGER.error("删除人员失败：{}",e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/batchDel")
    @ResponseBody
    public Result batchDel(String ids){
        Result result = new Result();

        if (StringUtils.isEmpty(ids)){
            result.setSuccess(true);
            result.setMsg("请选择至少一个人");
            return result;
        }

        try{
            String[] idList = ids.split(",");
            peopleService.batchDeletePeopleByIds(idList);
            result.setSuccess(true);
            result.setMsg("批量删除人员成功");
            return result;
        }catch(Exception exp){
            LOGGER.error("批量删除人员失败:{}",exp);
            result.setMsg(exp.getMessage());
            return result;
        }
    }
    /**
     * 批量调入
     * @param file
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST, headers = "Accept=application/json")
   	@ResponseBody
    public Result importExcel(@RequestParam(value="fileName",required=false)CommonsMultipartFile[] files){
    	Result result = new Result();
    	if(files!=null&&files.length>0){
    		boolean flag=peopleService.insertByImport(files);
    		result.setSuccess(flag);
    		if(!flag){
    			result.setMsg("系统繁忙，请稍后再试！");
    		}
    	}else{
    		result.setSuccess(false);
    		result.setMsg("请选择附件！");
    	}
    	return result;
    }
    /**
     * 导出Excel
     * @param id
     * @return
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,String ids){

        if (StringUtils.isEmpty(ids)){
        	 LOGGER.error("导出Word:{}","请选择有效数据!");
        }
        try{
            String[] idList = ids.split(",");
            peopleService.exportExcel(response,ids);
        }catch(Exception exp){
            LOGGER.error("导出Word:{}",exp);
        }
    }
    /**
     * 导出Word
     * @param id
     * @return
     */
    @RequestMapping("/exportWord")
    public void exportWord(HttpServletResponse response,String ids){

        if (StringUtils.isEmpty(ids)){
        	 LOGGER.error("导出Word:{}","请选择一条有效数据!");
        }
        try{
            peopleService.exportWord(response,ids);
        }catch(Exception exp){
            LOGGER.error("导出Word:{}",exp);
        }
    }
}
