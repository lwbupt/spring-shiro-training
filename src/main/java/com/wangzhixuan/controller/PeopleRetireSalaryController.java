package com.wangzhixuan.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.wangzhixuan.code.Result;
import com.wangzhixuan.model.PeopleRetire;
import com.wangzhixuan.model.PeopleRetireSalary;
import com.wangzhixuan.service.PeopleRetireSalaryService;
import com.wangzhixuan.service.PeopleRetireService;
import com.wangzhixuan.utils.PageInfo;
import com.wangzhixuan.vo.PeopleContractSalaryVo;
import com.wangzhixuan.vo.PeopleContractVo;
import com.wangzhixuan.vo.PeopleRetireSalaryVo;

/**
 * Created by sterm on 2017/1/13.
 */
@Controller
@RequestMapping("/peopleRetireSalary")
public class PeopleRetireSalaryController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PeopleRetireSalaryService peopleRetireSalaryService;

	@Autowired
	private PeopleRetireService peopleRetireService;

	@RequestMapping(value = "/manager", method = RequestMethod.GET)
	public String manager() {
		return "/admin/peopleRetireSalary/people";
	}

	@RequestMapping(value = "/dataGrid", method = RequestMethod.POST)
	@ResponseBody
	public PageInfo dataGrid(HttpServletRequest request, PeopleContractVo peopleVo, Integer page, Integer rows,
			String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows);
		Map<String, Object> condition = PeopleContractVo.CreateCondition(peopleVo);
		pageInfo.setCondition(condition);

		// LOGGER.info(JSON.toJSONString(pageInfo));
		peopleRetireService.findDataGrid(pageInfo);

		// LOGGER.info(JSON.toJSONString(pageInfo));

		return pageInfo;
	}

	@RequestMapping(value = "/salaryListPage", method = RequestMethod.GET)
	public String salaryListPage(Long id, Model model) {

		PeopleRetire people = peopleRetireService.findPeopleRetireById(id);
		logger.info(JSON.toJSONString(people));

		if (people != null) {
			model.addAttribute("code", people.getCode());
			model.addAttribute("name", people.getName());
		} else {
			model.addAttribute("code", "");
			model.addAttribute("name", "");
		}
		return "/admin/peopleRetireSalary/peopleSalaryList";
	}

	@RequestMapping(value = "/salaryGrid", method = RequestMethod.POST)
	@ResponseBody
	public PageInfo salaryGrid(HttpServletRequest request, Integer page, Integer rows, String sort, String order) {
		PageInfo pageInfo = new PageInfo(page, rows);
		String peopleCode = request.getParameter("code");
		Map<String, Object> condition = Maps.newHashMap();
		condition.put("peopleCode", peopleCode);
		pageInfo.setCondition(condition);

		peopleRetireSalaryService.findDataGrid(pageInfo, request);

		logger.info("salaryGrid123:" + JSON.toJSONString(pageInfo));
		return pageInfo;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Result delete(Long id) {
		Result result = new Result();
		try {
			peopleRetireSalaryService.deleteSalaryById(id);

			result.setMsg("删除成功！");
			result.setSuccess(true);
			return result;
		} catch (RuntimeException e) {
			logger.error("删除工资记录失败：{}", e);
			result.setMsg(e.getMessage());
			return result;
		}
	}

	@RequestMapping("/addPage")
	public String addPage(String peopleCode, Model model) {
		PeopleRetire peopleRetire = peopleRetireService.findPeopleRetireByCode(peopleCode);
		if (peopleRetire == null) {
			peopleRetire = new PeopleRetire();
		}
		model.addAttribute("people", peopleRetire);
		return "/admin/peopleRetireSalary/peopleSalaryAdd";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Result add(PeopleRetireSalary peopleRetireSalary,
			@RequestParam(value = "fileName", required = false) CommonsMultipartFile file) {
		Result result = new Result();
		try {
			peopleRetireSalaryService.addSalary(peopleRetireSalary);

			result.setSuccess(true);
			result.setMsg("添加成功!");
			return result;
		} catch (Exception e) {
			logger.error("添加工资失败：{}", e);
			result.setMsg(e.getMessage());
			return result;
		}
	}

	@RequestMapping("/editPage")
	public String editPage(Long id, Model model) {
		PeopleRetireSalaryVo peopleContractVo = peopleRetireSalaryService.findPeopleRetireSalaryVoById(id);
		model.addAttribute("peopleRetireSalary", peopleContractVo);

		logger.info("peopleRetireSalary:" + JSON.toJSONString(peopleContractVo));
		return "/admin/peopleRetireSalary/peopleSalaryEdit";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public Result edit(PeopleRetireSalary peopleRetireSalary) {
		Result result = new Result();
		try {
			logger.info("PeopleRetireSalary123:" + JSON.toJSONString(peopleRetireSalary));
			peopleRetireSalaryService.updateSalary(peopleRetireSalary);
			result.setSuccess(true);
			result.setMsg("修改成功!");
			return result;
		} catch (Exception e) {
			logger.error("修改工资失败：{}", e);
			result.setMsg(e.getMessage());
			return result;
		}
	}
}
