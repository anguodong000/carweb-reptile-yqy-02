package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.entity.CarWebEntity;
import com.reptile.carwebreptileyqy.entity.ErrorRecordEntity;
import com.reptile.carwebreptileyqy.entity.GraspRecordEntity;
import com.reptile.carwebreptileyqy.mapper.ReptileMapper;
import com.reptile.carwebreptileyqy.service.ReptileService;
import com.reptile.carwebreptileyqy.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ReptileServiceImpl implements ReptileService {

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";
    private String sessionValue = "UserID=HD363999; JSESSIONID=0000-ITxMgq2DKJjcLlgeVk31_q:1b9i8aoq5; WPCLOGIN=-ITxMgq2DKJjcLlgeVk31_q";

    @Autowired
    ReptileMapper reptileMapper;

    @Override
    public String resolveExcel(MultipartFile file) {
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            if (originalFilename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (Exception e) {
            log.info(originalFilename);
            e.printStackTrace();
        }
        if (workbook == null) {
            log.info(originalFilename);
            //throw new BusinessException(ReturnCode.CODE_FAIL, "格式错误");
        } else {
            //获取所有的工作表的的数量
            int numOfSheet = workbook.getNumberOfSheets();
            //遍历这个这些表
            for (int i = 1; i < numOfSheet; i++) {
                //获取一个sheet也就是一个工作簿
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int lastRowNum = sheet.getLastRowNum();
                //从第一行开始第一行一般是标题
                for (int j = 1; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    CarWebEntity carWebEntity = new CarWebEntity();

                    if (row.getCell(0) != null) {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        String type = row.getCell(0).getStringCellValue();
                        carWebEntity.setType(type);
                    }
                    //组号
                    if (row.getCell(1) != null) {
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        String groupNo = row.getCell(1).getStringCellValue();
                        carWebEntity.setGroupNo(groupNo);
                    }
                    //件号
                    if (row.getCell(2) != null) {
                        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        String partNo = row.getCell(2).getStringCellValue();
                        carWebEntity.setPartNo(partNo);
                    }
                    //SNC
                    if (row.getCell(3) != null) {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        String snc = row.getCell(3).getStringCellValue();
                        carWebEntity.setSuc(snc);
                    }
                    //PNC
                    if (row.getCell(4) != null) {
                        row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                        String pnc = row.getCell(4).getStringCellValue();
                        carWebEntity.setPnc(pnc);
                    }
                    //中文名称
                    if (row.getCell(5) != null) {
                        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                        String chName = row.getCell(5).getStringCellValue();
                        carWebEntity.setChName(chName);
                    }
                    //英文名称
                    if (row.getCell(6) != null) {
                        row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                        String enName = row.getCell(6).getStringCellValue();
                        carWebEntity.setEnName(enName);
                    }
                    if (row.getCell(7) != null) {
                        row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
                        String itc = row.getCell(7).getStringCellValue();
                        carWebEntity.setItc(itc);
                    }
                    if (row.getCell(8) != null) {
                        row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
                        String newPartNo = row.getCell(8).getStringCellValue();
                        carWebEntity.setNewPartNo(newPartNo);
                    }
                    if (!row.getCell(9).toString().equals("")&&!row.getCell(9).toString().equals("*")) {
                        row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                        int amountUsed = Integer.valueOf(row.getCell(9).getStringCellValue());
                        carWebEntity.setAmountUsed(amountUsed);
                    }
                    if (row.getCell(10) != null) {
                        row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
                        String engine = row.getCell(10).getStringCellValue();
                        carWebEntity.setEngine(engine);
                    }
                    if (row.getCell(11) != null) {
                        row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
                        String transmission = row.getCell(11).getStringCellValue();
                        carWebEntity.setTransmission(transmission);
                    }
                    if (row.getCell(12) != null) {
                        row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
                        String option1 = row.getCell(12).getStringCellValue();
                        carWebEntity.setOption1(option1);
                    }
                    if (row.getCell(13) != null) {
                        row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);
                        String option2 = row.getCell(13).getStringCellValue();
                        carWebEntity.setOption1(option2);
                    }
                    if (row.getCell(14) != null) {
                        row.getCell(14).setCellType(Cell.CELL_TYPE_STRING);
                        String option3 = row.getCell(14).getStringCellValue();
                        carWebEntity.setOption1(option3);
                    }
                    if (row.getCell(15) != null) {
                        row.getCell(15).setCellType(Cell.CELL_TYPE_STRING);
                        String option4 = row.getCell(15).getStringCellValue();
                        carWebEntity.setOption1(option4);
                    }
                    if (row.getCell(16) != null) {
                        row.getCell(16).setCellType(Cell.CELL_TYPE_STRING);
                        String option5 = row.getCell(16).getStringCellValue();
                        carWebEntity.setOption1(option5);
                    }
                    if (row.getCell(17) != null) {
                        row.getCell(17).setCellType(Cell.CELL_TYPE_STRING);
                        String option6 = row.getCell(17).getStringCellValue();
                        carWebEntity.setOption1(option6);
                    }
                    if (row.getCell(18) != null) {
                        row.getCell(18).setCellType(Cell.CELL_TYPE_STRING);
                        String option7 = row.getCell(18).getStringCellValue();
                        carWebEntity.setOption1(option7);
                    }
                    if (row.getCell(19) != null) {
                        row.getCell(19).setCellType(Cell.CELL_TYPE_STRING);
                        String option8 = row.getCell(19).getStringCellValue();
                        carWebEntity.setOption1(option8);
                    }
                    if (row.getCell(20) != null) {
                        row.getCell(20).setCellType(Cell.CELL_TYPE_STRING);
                        String option9 = row.getCell(20).getStringCellValue();
                        carWebEntity.setOption1(option9);
                    }
                    if (row.getCell(21) != null) {
                        row.getCell(21).setCellType(Cell.CELL_TYPE_STRING);
                        String option10 = row.getCell(21).getStringCellValue();
                        carWebEntity.setOption1(option10);
                    }
                    if (row.getCell(22) != null) {
                        row.getCell(22).setCellType(Cell.CELL_TYPE_STRING);
                        String option11 = row.getCell(22).getStringCellValue();
                        carWebEntity.setOption1(option11);
                    }
                    if (row.getCell(23) != null) {
                        row.getCell(23).setCellType(Cell.CELL_TYPE_STRING);
                        String option12 = row.getCell(23).getStringCellValue();
                        carWebEntity.setOption1(option12);
                    }
                    if (row.getCell(24) != null) {
                        row.getCell(24).setCellType(Cell.CELL_TYPE_STRING);
                        String option13 = row.getCell(24).getStringCellValue();
                        carWebEntity.setOption1(option13);
                    }
                    if (row.getCell(25) != null) {
                        row.getCell(25).setCellType(Cell.CELL_TYPE_STRING);
                        String option14 = row.getCell(25).getStringCellValue();
                        carWebEntity.setOption1(option14);
                    }
                    if (row.getCell(26) != null) {
                        row.getCell(26).setCellType(Cell.CELL_TYPE_STRING);
                        String exclusiveOp1 = row.getCell(26).getStringCellValue();
                        carWebEntity.setExclusiveOp1(exclusiveOp1);
                    }
                    if (row.getCell(27) != null) {
                        row.getCell(27).setCellType(Cell.CELL_TYPE_STRING);
                        String remark = row.getCell(27).getStringCellValue();
                        carWebEntity.setRemark(remark);
                    }
                    carWebEntity.setId(UUID.randomUUID().toString().toUpperCase());
                    carWebEntity.setCarType(sheetName);
                    carWebEntity.setCreateTime(new Date());
                    reptileMapper.insertCarWeb(carWebEntity);
                }
            }
        }
        return "上传成功";
    }

    @Override
    public String resolveBddehExcel(MultipartFile file) {
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            if (originalFilename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (Exception e) {
            log.info(originalFilename);
            e.printStackTrace();
        }
        if (workbook == null) {
            log.info(originalFilename);
            //throw new BusinessException(ReturnCode.CODE_FAIL, "格式错误");
        } else {
            //获取所有的工作表的的数量
            int numOfSheet = workbook.getNumberOfSheets();
            //遍历这个这些表
            for (int i = 1; i < numOfSheet; i++) {
                //获取一个sheet也就是一个工作簿
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int lastRowNum = sheet.getLastRowNum();
                //从第一行开始第一行一般是标题
                for (int j = 1; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    CarWebEntity carWebEntity = new CarWebEntity();

                    if (row.getCell(1) != null) {
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        String partNo = row.getCell(1).getStringCellValue();
                        carWebEntity.setPartNo(partNo);
                    }

                    if (row.getCell(2) != null&&!row.getCell(2).toString().equals("")) {
                        System.out.println("sheetName:"+sheetName+"j:"+j);
                        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        String suc = row.getCell(2).getStringCellValue();
                        carWebEntity.setSuc(suc);
                    }

                    if (row.getCell(3) != null) {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        String pnc = row.getCell(3).getStringCellValue();
                        carWebEntity.setPnc(pnc);
                    }

                    if (row.getCell(4) != null) {
                        row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                        String enName = row.getCell(4).getStringCellValue();
                        carWebEntity.setEnName(enName);
                    }

                    if (row.getCell(5) != null) {
                        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                        String chName = row.getCell(5).getStringCellValue();
                        carWebEntity.setChName(chName);
                    }

                    if (row.getCell(6) != null) {
                        row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                        String newPartNo = row.getCell(6).getStringCellValue();
                        carWebEntity.setNewPartNo(newPartNo);
                    }

                    if (row.getCell(7) != null) {
                        row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
                        String itc = row.getCell(7).getStringCellValue();
                        carWebEntity.setItc(itc);
                    }
                    if (row.getCell(8) != null) {
                        row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
                        String groupNo = row.getCell(8).getStringCellValue();
                        carWebEntity.setGroupNo(groupNo);
                    }
                    if (row.getCell(9) != null) {
                        row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
                        String remark = row.getCell(9).getStringCellValue();
                        carWebEntity.setRemark(remark);
                    }
                    carWebEntity.setId(UUID.randomUUID().toString().toUpperCase());
                    carWebEntity.setCarType(sheetName);
                    carWebEntity.setCreateTime(new Date());
                    reptileMapper.insertCarWeb(carWebEntity);
                }
            }
        }
        return "上传成功";
    }

    @Override
    public String graspData1() {


        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo1();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    public int getCharacterPositionFirst(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 1){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionSecond(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 2){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionThird(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 3){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionFourth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 4){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionFifth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 5){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionSixth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 6){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionSeventh(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 7){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionEighth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 8){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositioNinth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 9){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionTenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 10){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionEleven(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 11){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionTwelfth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 12){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionThirteenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 13){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionFourteenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 14){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionFifteenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 15){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionSixteenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 16){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionSeventeenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 17){
                break;
            }
        }
        return slashMatcher.start();
    }

    public int getCharacterPositionEighteenth(String string){
        //这里是获取"\:"符号的位置
        Matcher slashMatcher = Pattern.compile("\\:").matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"\:"符号第二次出现的位置
            if(mIdx == 18){
                break;
            }
        }
        return slashMatcher.start();
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel1(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList1();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出1.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel2(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList2();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出2.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel3(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList3();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出3.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel4(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList4();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出4.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel5(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList5();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出5.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel6(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList6();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出6.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel7(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList7();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出7.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel8(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList8();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出8.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel9(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList9();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出9.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel10(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList10();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出10.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel11(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList11();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出11.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel12(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList12();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出12.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel13(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList13();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出13.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel14(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList14();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出14.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel15(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList15();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出15.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel16(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList16();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出16.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel17(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList17();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出17.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel18(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList18();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出18.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel19(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList19();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出19.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel20(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList20();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出20.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel21(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList21();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出21.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel22(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList22();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出22.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel23(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList23();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出23.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel24(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList24();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出24.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel25(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList25();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出25.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel26(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList26();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出26.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel27(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList27();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出27.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel28(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList28();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出28.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel29(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList29();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出29.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel30(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList30();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出30.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel31(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList31();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出31.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel32(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList32();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出32.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel33(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList33();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出33.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel34(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList34();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出34.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel35(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList35();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出35.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel36(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList36();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出36.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel37(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList37();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出37.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel38(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList38();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出38.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exportExcel39(HttpServletResponse response) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("说明记录");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("从");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("到");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配件号");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellValue("配件名");
        cell.setCellStyle(style);

        cell = row.createCell(4);
        cell.setCellValue("说明");
        cell.setCellStyle(style);

        cell = row.createCell(5);
        cell.setCellValue("组号");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("pnc");
        cell.setCellStyle(style);

        //获取数据
        List<GraspRecordEntity> graspRecordEntityList = reptileMapper.getGraspRecordList39();

        int rowNum=1;
        for(GraspRecordEntity graspRecordEntity:graspRecordEntityList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(graspRecordEntity.getStartTime());
            row1.createCell(1).setCellValue(graspRecordEntity.getEndTime());
            row1.createCell(2).setCellValue(graspRecordEntity.getPartNo());
            row1.createCell(3).setCellValue(graspRecordEntity.getPartName());
            row1.createCell(4).setCellValue(graspRecordEntity.getInstruction());
            row1.createCell(5).setCellValue(graspRecordEntity.getGroupNo());
            row1.createCell(6).setCellValue(graspRecordEntity.getPnc());
            rowNum++;
        }
        String fileName = "说明记录导出39.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public String graspData2() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo2();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData3() {

        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo3();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData4() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo4();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData5() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo5();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData6() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo6();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData7() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo7();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData8() {

        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo8();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData9() {

        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo9();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "sessionValue");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getExceptionPnc().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }

    @Override
    public String graspData() {
        List<CarWebEntity> gpList = reptileMapper.getGrNoAndPncNo();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("Cookie", "JSESSIONID=00001LlxLxz7VAkkZZ2Ike_ElHA:1b9i8atrq; WPCLOGIN=1LlxLxz7VAkkZZ2Ike_ElHA");
        gpList.forEach(carWebEntity->{
            String pnc=carWebEntity.getPartNo().trim();
            //String pnc = "86511";
            try{
                String partsStr=HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartsList2&ptno="+pnc+"&pncd=&lang=CH&page=1&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                System.out.println("partsStr:"+partsStr);
                if(!partsStr.equals("")){
                    if(partsStr.equals("NoSessionException")){
                        ErrorRecordEntity errorRecordEntity = new ErrorRecordEntity();
                        errorRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                        errorRecordEntity.setExceptionPnc(pnc);
                        reptileMapper.insertErrorRecord(errorRecordEntity);
                    }
                    String[] partsArr = partsStr.split("\\^");
                    for(int i=0;i<partsArr.length;i++){
                        try{
                            String partNo = partsArr[i].substring(this.getCharacterPositionFirst(partsArr[i])+1,this.getCharacterPositionSecond(partsArr[i])-1);
                            System.out.println("partNo:"+partNo);
                            String partCatalogs = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartCatalogList2&ptno="+partNo+"&lang=CH&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR",paramMap);
                            System.out.println("partCatalogs:"+partCatalogs);
                            if(!partCatalogs.equals("")){
                                String[] partCatalogArr = partCatalogs.split("\\^");
                                for(int j=0;j<partCatalogArr.length;j++){
                                    try{
                                        String bpno = partCatalogArr[j].substring(this.getCharacterPositionThird(partCatalogArr[j])+1,this.getCharacterPositionFourth(partCatalogArr[j])-1);
                                        System.out.println("bpno:"+bpno);
                                        String partCatalogInfo = HttpUtil.sendGetWithHeader("http://wpc.mobis.co.kr/Parts?cmd=getPartInfo2&ptno="+partNo+"&bpno="+bpno+"&hkgb=K&regns=CHN&views=Y&sgmts=E%2CJ%2CN%2CR&bpnos=&mf=A%2C",paramMap);
                                        System.out.println("partCatalogInfo:"+partCatalogInfo);
                                        String crnm=partCatalogInfo.substring(0,this.getCharacterPositionFirst(partCatalogInfo)-1);
                                        String bpno1=partCatalogInfo.substring(this.getCharacterPositionFirst(partCatalogInfo)+1,this.getCharacterPositionSecond(partCatalogInfo)-1);
                                        String hkgb=partCatalogInfo.substring(this.getCharacterPositionSecond(partCatalogInfo)+1,this.getCharacterPositionThird(partCatalogInfo)-1);
                                        String hsgb=partCatalogInfo.substring(this.getCharacterPositionThird(partCatalogInfo)+1,this.getCharacterPositionFourth(partCatalogInfo)-1);
                                        String pkgb=partCatalogInfo.substring(this.getCharacterPositionFourth(partCatalogInfo)+1,this.getCharacterPositionFifth(partCatalogInfo)-1);
                                        String opty=partCatalogInfo.substring(this.getCharacterPositionFifth(partCatalogInfo)+1,this.getCharacterPositionSixth(partCatalogInfo)-1);
                                        String pccd=partCatalogInfo.substring(this.getCharacterPositionSixth(partCatalogInfo)+1,this.getCharacterPositionSeventh(partCatalogInfo)-1);
                                        String view=partCatalogInfo.substring(this.getCharacterPositionSeventh(partCatalogInfo)+1,this.getCharacterPositionEighth(partCatalogInfo)-1);
                                        String efin=partCatalogInfo.substring(this.getCharacterPositionEighth(partCatalogInfo)+1,this.getCharacterPositioNinth(partCatalogInfo)-1);
                                        String efot=partCatalogInfo.substring(this.getCharacterPositioNinth(partCatalogInfo)+1,this.getCharacterPositionTenth(partCatalogInfo)-1);
                                        String vhcd=partCatalogInfo.substring(this.getCharacterPositionTenth(partCatalogInfo)+1,this.getCharacterPositionEleven(partCatalogInfo)-1);
                                        String grty=partCatalogInfo.substring(this.getCharacterPositionEleven(partCatalogInfo)+1,this.getCharacterPositionTwelfth(partCatalogInfo)-1);
                                        String sgmt=partCatalogInfo.substring(this.getCharacterPositionTwelfth(partCatalogInfo)+1,this.getCharacterPositionThirteenth(partCatalogInfo)-1);
                                        String ucct=partCatalogInfo.substring(this.getCharacterPositionThirteenth(partCatalogInfo)+1,this.getCharacterPositionFourteenth(partCatalogInfo)-1);
                                        String lrcd=partCatalogInfo.substring(this.getCharacterPositionSeventeenth(partCatalogInfo)+1,this.getCharacterPositionEighteenth(partCatalogInfo)-1);
                                        String groupNo=partCatalogInfo.substring(this.getCharacterPositionFourteenth(partCatalogInfo)+1,this.getCharacterPositionFifteenth(partCatalogInfo)-1);
                                        String pncNo=partCatalogInfo.substring(this.getCharacterPositionFifteenth(partCatalogInfo)+1,this.getCharacterPositionSixteenth(partCatalogInfo)-1);
                                        String result = HttpUtil.sendPostWithHeaderFull(
                                                "http://wpc.mobis.co.kr/TextSearch?cmd=getFinalGroupDataList",
                                                "CRNM="+crnm+"&BPNO="+bpno1+"&HKGB="+hkgb+"&HSGB="+hsgb+"&PKGB="+pkgb+"&OPTY="+opty+"&PCCD="+pccd+"&VIEW="+view+"&EFIN="+efin+"&EFOT="+efot+"&VHCD="+vhcd+"&GRTY="+grty+"&SGMT="+sgmt+"&UCCT="+ucct+"&ISVIN=MODEL&AS=N&MATCH=OFF&LRCD="+lrcd+"&LANG=CH&GRNO="+groupNo+"&PNCD="+pncNo,
                                                ContentType.APPLICATION_FORM_URLENCODED.toString(),
                                                Consts.UTF_8,
                                                paramMap);
                                        System.out.println("getFinalGroupDataListResult:"+result);
                                        if(!result.equals("")){
                                            String[] splResult = result.split("@");
                                            for(int h=0;h<splResult.length;h++){
                                                System.out.println("i:"+i);
                                                int secondPosition = this.getCharacterPositionSecond(splResult[h]);
                                                int thirdPosition = this.getCharacterPositionThird(splResult[h]);
                                                int fourthPosition = this.getCharacterPositionFourth(splResult[h]);
                                                int fifthPosition = this.getCharacterPositionFifth(splResult[h]);
                                                try{
                                                    String statrTimeStr = splResult[h].substring(0,splResult[h].indexOf("\\:"));
                                                    String endTimeStr = splResult[h].substring(splResult[h].indexOf("\\:")+2,secondPosition-1);
                                                    String partNo2 = splResult[h].substring(secondPosition+1,thirdPosition-1);
                                                    String partName = splResult[h].substring(thirdPosition+1,fourthPosition-1);
                                                    String instruction = splResult[h].substring(fourthPosition+2,fifthPosition-2);
                                                    GraspRecordEntity graspRecordEntity=new GraspRecordEntity();
                                                    graspRecordEntity.setId(UUID.randomUUID().toString().toUpperCase());
                                                    graspRecordEntity.setPartNo(partNo2);
                                                    graspRecordEntity.setPartName(partName);
                                                    graspRecordEntity.setStartTime(statrTimeStr);
                                                    graspRecordEntity.setEndTime(endTimeStr);
                                                    graspRecordEntity.setInstruction(instruction);
                                                    graspRecordEntity.setGroupNo(groupNo);
                                                    graspRecordEntity.setPnc(pnc);
                                                    reptileMapper.insertGraspRecord(graspRecordEntity);
                                                }catch(Exception e){
                                                    System.out.println("groupNo:"+groupNo+";"+"pnc:"+pnc+";"+"抓取失败");
                                                    e.printStackTrace();
                                                }finally {
                                                    continue;
                                                }
                                            }
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }finally {
                                        continue;
                                    }
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            continue;
                        }
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        });
        return "抓取成功";
    }
}
