package com.wangzhixuan.controller;

import com.wangzhixuan.code.Result;
import com.wangzhixuan.model.People;
import com.wangzhixuan.model.PeopleTransfer;
import com.wangzhixuan.service.PeopleService;
import com.wangzhixuan.service.PeopleTransferService;
import com.wangzhixuan.utils.ConstUtil;
import com.wangzhixuan.utils.PageInfo;
import com.wangzhixuan.vo.PeopleVo;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by liushaoyang on 2016/9/8.
 * 人员信息管理
 */

@Controller
@RequestMapping("/timesheet")
public class TimeSheetController extends BaseController{
    private static Logger LOGGER = LoggerFactory.getLogger(TimeSheetController.class);

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private PeopleTransferService peopleTransferService;

    /**
     * 人员管理页
     *
     * @return
     */
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String manager() {
        return "/admin/timeSheet/people";
    }

    /**
     * 人员管理列表
     *
     * @param
     * @param page
     * @param rows
     * @param sort
     * @param order
     * @return
     */
    @RequestMapping(value="/dataGrid", method=RequestMethod.POST)
    @ResponseBody
    public PageInfo dataGrid(HttpServletRequest request, PeopleVo peoplevo, Integer page, Integer rows, String sort, String order){
        PageInfo pageInfo = new PageInfo(page, rows);
        Map<String,Object> condition = PeopleVo.CreateCondition(peoplevo);
        pageInfo.setCondition(condition);
        peopleService.findDataGrid(pageInfo, request);

        return pageInfo;
    }

    @RequestMapping(value="/advSearchPage", method = RequestMethod.GET)
    public String advSearchPage(){
        return "admin/people/peopleSearch";
    }

    @RequestMapping(value="/exportSearchPage", method = RequestMethod.GET)
    public String exportSearchPage() { return "admin/people/peopleSearch";}

    @RequestMapping(value="/exportSearch", method = RequestMethod.POST)
    @ResponseBody
    public Result exportSearch(HttpServletResponse response, PeopleVo peoplevo) {

        Result result = new Result();

        Map<String,Object> condition = PeopleVo.CreateCondition(peoplevo);

        PageInfo pageInfo = new PageInfo();
        pageInfo.setCondition(condition);
        String ids = peopleService.findPeopleIDsByCondition(pageInfo);

        if (StringUtils.isBlank(ids)){
            result.setSuccess(false);
            result.setMsg("未找到有效数据");
            LOGGER.error("Excel:{}","无有效数据");
            return result;
        }
        try{
            result.setSuccess(true);
            result.setObj(ids);
        }catch(Exception exp){
            result.setSuccess(false);
            result.setMsg("导出Excel失败");
            LOGGER.error("导出Excel失败:{}",exp);
        }

        return result;
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
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
    public Result add(PeopleVo peoplevo,@RequestParam(value="fileName",required=false)CommonsMultipartFile file) {
        Result result = new Result();
        try {

            peopleService.addPeople(peoplevo,file);
            result.setSuccess(true);
            result.setMsg("添加成功");
            return result;
        } catch (Exception e) {
            LOGGER.error("添加用户失败：{}", e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/editPage")
    public String editPage(Long id, Model model){
        PeopleVo peopleVo = peopleService.findPeopleVoById(id);
        model.addAttribute("peopleVo",peopleVo);
        return "/admin/people/peopleEdit";
    }

    @RequestMapping("/edit")
    @ResponseBody
    public Result edit(PeopleVo peoplevo, @RequestParam(value="fileName",required=false)CommonsMultipartFile file){
        Result result = new Result();
        try{
            peopleService.updatePeople(peoplevo,file);
            result.setSuccess(true);
            result.setMsg("修改成功!");
            return result;
        }catch(Exception e){
            LOGGER.error("修改人员失败：{}",e);
            result.setMsg(e.getMessage());
            return result;
        }
    }

    @RequestMapping("/transferPage")
    public String transferPage(Long id, Model model){
        PeopleVo peopleVo = peopleService.findPeopleVoById(id);
        model.addAttribute("peopleVo", peopleVo);
        return "/admin/people/peopleTransfer";
    }

    @RequestMapping("/transfer")
    @ResponseBody
    public Result transfer(PeopleTransfer peopleTransfer){
        Result result = new Result();
        try{
            People people = peopleService.findPeopleById(peopleTransfer.getId());

            //更新人员状态
            if (people != null){
                people.setStatus(ConstUtil.PEOPLE_TRANSFER);
                peopleService.updatePeople(people);
                //添加一条人员调动记录
                peopleTransferService.addPeopleTransfer(peopleTransfer,null);
            }

            result.setSuccess(true);
            result.setMsg("调动成功");
            return result;
        }catch(Exception e){
            LOGGER.error("调动人员失败:{}",e);
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

    @RequestMapping("/batchRetire")
    @ResponseBody
    public Result batchRetire(String ids){
        Result result = new Result();

        if (StringUtils.isEmpty(ids)){
            result.setSuccess(true);
            result.setMsg("请选择至少一个人");
            return result;
        }

        try{
            String[] idList = ids.split(",");
            peopleService.batchRetirePeopleByIds(idList);
            result.setSuccess(true);
            result.setMsg("批量转入退休人员成功");
            return result;
        }catch(Exception exp){
            LOGGER.error("批量转入退休人员失败:{}",exp);
            result.setMsg(exp.getMessage());
            return result;
        }
    }

    @RequestMapping("/batchDeath")
    @ResponseBody
    public Result batchDeath(String ids){
        Result result = new Result();

        if (StringUtils.isEmpty(ids)){
            result.setSuccess(true);
            result.setMsg("请选择至少一个人");
            return result;
        }

        try{
            String[] idList = ids.split(",");
            peopleService.batchDeathPeopleByIds(idList);
            result.setSuccess(true);
            result.setMsg("批量转入已故人员成功");
            return result;
        }catch(Exception exp){
            LOGGER.error("批量转入已故人员失败:{}",exp);
            result.setMsg(exp.getMessage());
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
     * 批量调入W
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
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,String ids){

        if (StringUtils.isBlank(ids)){
            LOGGER.error("Excel:{}","请选择有效数据!");
        }
        try{
            peopleService.exportExcel(response,ids.split(","));
        }catch(Exception exp){
            LOGGER.error("导出Excel失败:{}",exp);
        }
    }
    /**
     * 导出Word
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
