package com.wangzhixuan.service.impl;

import com.wangzhixuan.mapper.DictMapper;
import com.wangzhixuan.mapper.PeoplePartyMapper;
import com.wangzhixuan.model.PeopleParty;
import com.wangzhixuan.service.PeoplePartyService;
import com.wangzhixuan.utils.PageInfo;
import com.wangzhixuan.utils.StringUtilExtra;
import com.wangzhixuan.utils.UploadUtil;
import com.wangzhixuan.utils.WordUtil;
import com.wangzhixuan.vo.PeoplePartyVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by administrator_cernet on 2016/11/27.
 */
@Service
public class PeoplePartyServiceImpl implements PeoplePartyService{


    @Autowired
    private PeoplePartyMapper peoplePartyMapper;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public PeopleParty findPeoplePartyById(Long id) {
        return peoplePartyMapper.findPeoplePartyById(id);
    }

    @Override
    public PeopleParty findPeoplePartyByName(String name) {
        return peoplePartyMapper.findPeoplePartyByName(name);
    }

    @Override
    public void findDataGrid(PageInfo pageInfo) {
        pageInfo.setRows(peoplePartyMapper.findPeoplePartyPageCondition(pageInfo));
        pageInfo.setTotal(peoplePartyMapper.findPeoplePartyPageCount(pageInfo));
    }

    @Override
    public void addPeopleParty(PeopleParty peopleParty,CommonsMultipartFile file) {

        //当birthday不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getBirthday())){
                peopleParty.setBirthday(null);
            }
        }

        //当workDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getWorkDate())){
                peopleParty.setWorkDate(null);
            }
        }

        //当partyDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyDate())){
                peopleParty.setPartyDate(null);
            }
        }

        //当jobDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getJobDate())){
                peopleParty.setJobDate(null);
            }
        }

        //当partyInDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyInDate())){
                peopleParty.setPartyInDate(null);
            }
        }

        //当partyOutDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyOutDate())){
                peopleParty.setPartyOutDate(null);
            }
        }

        peopleParty.setPeopleCode(StringUtilExtra.generateUUID());

        if(file!=null){//上传附件
            //获取头像上传路径
            String filePath = StringUtilExtra.getPictureUploadPath();
            String uploadPath = UploadUtil.pictureUpLoad(filePath,file);
            if(StringUtils.isNotEmpty(uploadPath) ){
                peoplePartyMapper.insert(peopleParty);
            }
        }else{
            peoplePartyMapper.insert(peopleParty);
        }
    }

    @Override
    public void updatePeopleParty(PeopleParty peopleParty, CommonsMultipartFile file) {

        //当birthday不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getBirthday())){
                peopleParty.setBirthday(null);
            }
        }

        //当workDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getWorkDate())){
                peopleParty.setWorkDate(null);
            }
        }

        //当partyDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyDate())){
                peopleParty.setPartyDate(null);
            }
        }

        //当jobDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getJobDate())){
                peopleParty.setJobDate(null);
            }
        }

        //当partyInDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyInDate())){
                peopleParty.setPartyInDate(null);
            }
        }

        //当partyOutDate不为空，而是""的时候，需要修改为null，否则插入会有错误
        if (peopleParty != null){
            if (StringUtils.isEmpty(peopleParty.getPartyOutDate())){
                peopleParty.setPartyOutDate(null);
            }
        }

        if (file != null){
            //获取头像上传路径
            String filePath = StringUtilExtra.getPictureUploadPath();
            String uploadPath = UploadUtil.pictureUpLoad(filePath,file);
            if(StringUtils.isNotEmpty(uploadPath)){
                peoplePartyMapper.updatePeopleParty(peopleParty);
            }
        }else{
            peoplePartyMapper.updatePeopleParty(peopleParty);
        }
    }

    @Override
    public void deletePeoplePartyById(Long id) {
        peoplePartyMapper.deleteById(id);
    }

    @Override
    public void batchDeletePeoplePartyByIds(String[] ids){
        peoplePartyMapper.batchDeleteByIds(ids);
    }

    @Override
    public boolean insertByImport(CommonsMultipartFile[] files){
        boolean flag=false;
        if(files!=null && files.length>0){

            List<PeopleParty> list = new ArrayList<PeopleParty>();

            String filePath = this.getClass().getResource("/").getPath();//文件临时路径

            for(int i=0; i<files.length; i++){

                String path= UploadUtil.fileUpload(filePath, files[i]);

                if( StringUtils.isNotBlank(path)){
                    list=getPeoplePartyInfoByExcel(list,path);
                }
            }
            if(list.size()>0){
                flag=peoplePartyMapper.insertByImport(list)>0;
            }
        }
        return flag;
    }
    /**
     * 文件读取
     * @param list
     * @param path
     * @return
     */
    private List<PeopleParty> getPeoplePartyInfoByExcel(List<PeopleParty> list,String path){
        try {
            XSSFWorkbook xwb = new XSSFWorkbook(path);
            XSSFSheet sheet = xwb.getSheetAt(0);
            //
            List<XSSFPictureData> pictureList = xwb.getAllPictures();

            XSSFRow row;
            for (int i = sheet.getFirstRowNum()+1; i < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                PeopleParty p=new PeopleParty();

                //将Excel中的图片插入到数据库中
                if (pictureList != null && pictureList.size() > 0){
                    XSSFPictureData picture = pictureList.get(0);
                    String filePath = StringUtilExtra.getPictureUploadPath();
                    String uploadPath = UploadUtil.pictureUpLoad(filePath,picture);

                    if (StringUtils.isNotBlank(uploadPath)){
                        p.setPhoto(uploadPath);
                    }
                }

                //人员编码
                p.setPeopleCode(StringUtilExtra.generateUUID());

                //人员类别
                if(row.getCell(1)!=null&&!row.getCell(1).toString().trim().equals("")){
                    String peopleType=row.getCell(1).toString().trim();
                    p.setPeopleType(peopleType);
                }

                //人员姓名
                if(row.getCell(2)!=null&&!row.getCell(2).toString().trim().equals("")){
                    String peopleName=row.getCell(2).toString().trim();
                    p.setPeopleName(peopleName);
                }

                //所在支部
                if(row.getCell(3) != null && !row.getCell(3).toString().trim().equals("")){
                    String branchName = row.getCell(3).toString().trim();

                    try{
                        Integer branchId = dictMapper.findBranchIdByName(branchName);
                        if (branchId != null){
                            p.setBranchId(branchId);
                        }
                    }catch(Exception exp){

                    }
                }

                //部门
                if(row.getCell(4) != null && !row.getCell(4).toString().trim().equals("")){
                    String departmentName = row.getCell(4).toString().trim();

                    try{
                        Integer departmentId = dictMapper.findDepartmentIdByName(departmentName);
                        if (departmentId != null){
                            p.setDepartmentId(departmentId);
                        }
                    }catch(Exception exp){

                    }
                }

                //性别
                if(row.getCell(5)!=null&&!row.getCell(5).toString().trim().equals("")){
                    String sex=row.getCell(5).toString().trim();
                    p.setSex(sex.equals("女")?1:0);
                }

                //民族
                if(row.getCell(6) != null && !row.getCell(6).toString().trim().equals("")){
                    String nationalName = row.getCell(6).toString().trim();

                    try{
                        Integer nationalId = dictMapper.findNationalIdByName(nationalName);
                        if (nationalId != null){
                            p.setNationalId(nationalId);
                        }
                    }catch(Exception exp){

                    }
                }

                //出生日期
                if(row.getCell(7)!=null&&!row.getCell(7).toString().trim().equals("")){
                    String birthday=row.getCell(7).toString().trim();
                    p.setBirthday(birthday);
                }

                //籍贯
                if(row.getCell(8)!=null&&!row.getCell(8).toString().trim().equals("")){
                    String nativeName=row.getCell(8).toString().trim();
                    p.setNativeName(nativeName);
                }

                //党员状态
                if(row.getCell(9)!=null&&!row.getCell(9).toString().trim().equals("")){
                    String partyStatusName = row.getCell(9).toString().trim();

                    try{
                        Integer partyStatusId = dictMapper.findPartyStatusIdByName(partyStatusName);
                        if (partyStatusId != null){
                            p.setPartyStatusId(partyStatusId);
                        }
                    }catch(Exception exp){

                    }
                }

                //入党日期
                if(row.getCell(10)!=null&&!row.getCell(10).toString().trim().equals("")){
                    String partyDate=row.getCell(10).toString().trim();
                    p.setPartyDate(partyDate);
                }

                //学历情况
                if(row.getCell(11)!=null&&!row.getCell(11).toString().trim().equals("")){
                    String degreeName = row.getCell(11).toString().trim();

                    try{
                        Integer degreeId = dictMapper.findDegreeIdByName(degreeName);
                        if (degreeId != null){
                            p.setDegreeId(degreeId);
                        }
                    }catch(Exception exp){

                    }
                }

                //参加工作日期
                if(row.getCell(12)!=null&&!row.getCell(12).toString().trim().equals("")){
                    String workDate=row.getCell(12).toString().trim();
                    p.setWorkDate(workDate);
                }

                //职务岗位
                if(row.getCell(13)!=null&&!row.getCell(13).toString().trim().equals("")){
                    String jobName=row.getCell(13).toString().trim();
                    p.setJobName(jobName);
                }

                //职级
                if(row.getCell(14)!=null&&!row.getCell(14).toString().trim().equals("")){
                    String jobLevelName = row.getCell(14).toString().trim();

                    try{
                        Integer jobLevelId = dictMapper.findJobLevelIdByName(jobLevelName);
                        if (jobLevelId != null){
                            p.setJobLevelId(jobLevelId);
                        }
                    }catch(Exception exp){

                    }
                }

                //现任职级日期
                if(row.getCell(15)!=null&&!row.getCell(15).toString().trim().equals("")){
                    String jobDate=row.getCell(15).toString().trim();
                    p.setJobDate(jobDate);
                }

                //编制
                if(row.getCell(16)!=null&&!row.getCell(16).toString().trim().equals("")){
                    String formation=row.getCell(16).toString().trim();
                    p.setFormation(formation);
                }

                //党组织关系转入日期
                if(row.getCell(17) != null && !row.getCell(17).toString().trim().equals("")){
                    String partyInDate=row.getCell(17).toString().trim();
                    p.setPartyInDate(partyInDate);
                }

                //党组织关系转出日期
                if(row.getCell(18) != null && !row.getCell(18).toString().trim().equals("")){
                    String partyOutDate=row.getCell(18).toString().trim();
                    p.setPartyOutDate(partyOutDate);
                }

                //备注
                if(row.getCell(19)!=null&&!row.getCell(19).toString().trim().equals("")){
                    String comment=row.getCell(19).toString().trim();
                    p.setComment(comment);
                }

                list.add(p);
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return list;
    }

    //导出excel
    @Override
    public void exportExcel(HttpServletResponse response,String[] idList){
        List list=peoplePartyMapper.selectPeoplePartyVoByIds(idList);
        if(list!=null&&list.size()>0){
            XSSFWorkbook workBook;
            OutputStream os;
            String newFileName="党员人员信息.xlsx";
            try {
                workBook = new XSSFWorkbook();
                XSSFSheet sheet= workBook.createSheet("党员人员信息");
                XSSFCellStyle setBorder= WordUtil.setCellStyle(workBook,true);
                //创建表头
                XSSFRow row=sheet.createRow(0);
                row.createCell(0).setCellValue("序号");row.getCell(0).setCellStyle(setBorder);
                row.createCell(1).setCellValue("人员类别");row.getCell(1).setCellStyle(setBorder);
                row.createCell(2).setCellValue("人员姓名");row.getCell(2).setCellStyle(setBorder);
                row.createCell(3).setCellValue("所在支部");row.getCell(3).setCellStyle(setBorder);
                row.createCell(4).setCellValue("部门");row.getCell(4).setCellStyle(setBorder);
                row.createCell(5).setCellValue("性别");row.getCell(5).setCellStyle(setBorder);
                row.createCell(6).setCellValue("民族");row.getCell(6).setCellStyle(setBorder);
                row.createCell(7).setCellValue("出生日期");row.getCell(7).setCellStyle(setBorder);
                row.createCell(8).setCellValue("籍贯");row.getCell(8).setCellStyle(setBorder);
                row.createCell(9).setCellValue("党员状态");row.getCell(9).setCellStyle(setBorder);
                row.createCell(10).setCellValue("入党日期");row.getCell(10).setCellStyle(setBorder);
                row.createCell(11).setCellValue("学历情况");row.getCell(11).setCellStyle(setBorder);
                row.createCell(12).setCellValue("参加工作日期");row.getCell(12).setCellStyle(setBorder);
                row.createCell(13).setCellValue("职务岗位");row.getCell(13).setCellStyle(setBorder);
                row.createCell(14).setCellValue("职级");row.getCell(14).setCellStyle(setBorder);
                row.createCell(15).setCellValue("现任职级日期");row.getCell(15).setCellStyle(setBorder);
                row.createCell(16).setCellValue("编制");row.getCell(16).setCellStyle(setBorder);
                row.createCell(17).setCellValue("党组织关系转入日期");row.getCell(17).setCellStyle(setBorder);
                row.createCell(18).setCellValue("党组织关系转出日期");row.getCell(18).setCellStyle(setBorder);
                row.createCell(19).setCellValue("备注");row.getCell(19).setCellStyle(setBorder);
                setBorder=WordUtil.setCellStyle(workBook,false);
                for(int i=0;i<list.size();i++){
                    row=sheet.createRow(i+1);
                    PeoplePartyVo p=(PeoplePartyVo)list.get(i);
                    row.createCell(0).setCellValue(i+1);                row.getCell(0).setCellStyle(setBorder);
                    row.createCell(1).setCellValue(p.getPeopleType());  row.getCell(1).setCellStyle(setBorder);
                    row.createCell(2).setCellValue(p.getPeopleName());  row.getCell(2).setCellStyle(setBorder);
                    row.createCell(3).setCellValue(p.getBranchName());  row.getCell(3).setCellStyle(setBorder);
                    row.createCell(4).setCellValue(p.getDepartmentName());row.getCell(4).setCellStyle(setBorder);
                    row.createCell(5).setCellValue(p.getSex()==null?"":(p.getSex()==0?"男":"女"));row.getCell(5).setCellStyle(setBorder);
                    row.createCell(6).setCellValue(p.getNationalName());row.getCell(6).setCellStyle(setBorder);
                    row.createCell(7).setCellValue(p.getBirthday());    row.getCell(7).setCellStyle(setBorder);
                    row.createCell(8).setCellValue(p.getNativeName());  row.getCell(8).setCellStyle(setBorder);
                    row.createCell(9).setCellValue(p.getPartyStatusName());row.getCell(9).setCellStyle(setBorder);
                    row.createCell(10).setCellValue(p.getPartyDate());row.getCell(10).setCellStyle(setBorder);
                    row.createCell(11).setCellValue(p.getDegreeName());row.getCell(11).setCellStyle(setBorder);
                    row.createCell(12).setCellValue(p.getWorkDate());row.getCell(12).setCellStyle(setBorder);
                    row.createCell(13).setCellValue(p.getJobName());row.getCell(13).setCellStyle(setBorder);
                    row.createCell(14).setCellValue(p.getJobLevelName());row.getCell(14).setCellStyle(setBorder);
                    row.createCell(15).setCellValue(p.getJobDate());row.getCell(15).setCellStyle(setBorder);
                    row.createCell(16).setCellValue(p.getFormation());row.getCell(16).setCellStyle(setBorder);
                    row.createCell(17).setCellValue(p.getPartyInDate());row.getCell(17).setCellStyle(setBorder);
                    row.createCell(18).setCellValue(p.getPartyOutDate());row.getCell(18).setCellStyle(setBorder);
                    row.createCell(19).setCellValue(p.getComment());row.getCell(19).setCellStyle(setBorder);
                    row.setHeight((short) 400);
                }
                sheet.setDefaultRowHeightInPoints(21);
                response.reset();
                os = response.getOutputStream();
                response.setHeader("Content-disposition", "attachment; filename=" + new String(newFileName.getBytes("GBK"), "ISO-8859-1"));
                workBook.write(os);
                os.close();
            }catch (IOException e) {
                e.printStackTrace();
            }finally{

            }
        }
    }

    //导出word
    @Override
    public void exportWord(HttpServletResponse response,String id){
        PeoplePartyVo p= peoplePartyMapper.findPeoplePartyVoById(Long.valueOf(id));
        if(p!=null){
            XWPFDocument doc;
            OutputStream os;
            String filePath=this.getClass().getResource("/template/custInfoParty.docx").getPath();
            String newFileName="党员人员信息.docx";

            Map<String,Object> params = new HashMap<String,Object>();
            params.put("${peopleCode}",p.getPeopleCode());
            params.put("${peopleType}",p.getPeopleType());
            params.put("${peopleName}",p.getPeopleName());
            params.put("${branchName}",p.getBranchName()==null?"":p.getBranchName());
            params.put("${departmentName}",p.getDepartmentName()==null?"":p.getDepartmentName());
            params.put("${sex}",p.getSex()==0?"男":"女");
            params.put("${nationalName}",p.getNationalName()==null?"":p.getNationalName());
            params.put("${birthday}",p.getBirthday()==null?"":p.getBirthday());
            params.put("${nativeName}",p.getNativeName());
            params.put("${partyStatusName}",p.getPartyStatusName()==null?"":p.getPartyStatusName());
            params.put("${partyDate}",p.getPartyDate()==null?"":p.getPartyDate());
            params.put("${degreeName}",p.getDegreeName()==null?"":p.getDegreeName());
            params.put("${workDate}",p.getWorkDate()==null?"":p.getWorkDate());
            params.put("${jobName}",p.getJobName());
            params.put("${jobLevelName}",p.getJobLevelName()==null?"":p.getJobLevelName());
            params.put("${jobDate}",p.getJobDate()==null?"":p.getJobDate());
            params.put("${formation}",p.getFormation());
            params.put("${partyInDate}",p.getPartyInDate()==null?"":p.getPartyInDate());
            params.put("${partyOutDate}",p.getPartyOutDate()==null?"":p.getPartyOutDate());
            params.put("${comment}",p.getComment());

            //判断是否有头像
            if(p.getPhoto()!=null&&p.getPhoto().length()>0){
                Map<String, Object> header = WordUtil.PutPhotoIntoWordParameter(p.getPhoto());
                params.put("${photo}",header);
            }
            else
                params.put("${photo}","");

            WordUtil.OutputWord(response, filePath, newFileName, params);
        }
    }

    @Override
    public String findPeoplePartyIDsByCondition(PageInfo pageInfo) {
        String ids = "";
        pageInfo.setFrom(0);
        pageInfo.setSize(100000);
        List<PeoplePartyVo> peoplePartyList = peoplePartyMapper.findPeoplePartyPageCondition(pageInfo);
        if (peoplePartyList == null || peoplePartyList.size() < 1)
            return ids;


        for(int i=0; i<peoplePartyList.size(); i++){
            ids = ids + peoplePartyList.get(i).getId().toString() + ",";
        }

        //刪除最後一個逗号
        ids = ids.substring(0, ids.lastIndexOf(','));

        return ids;
    }
}



