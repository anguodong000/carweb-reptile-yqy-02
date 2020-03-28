package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.entity.CarWebEntity;
import com.reptile.carwebreptileyqy.entity.ErrorRecordEntity;
import com.reptile.carwebreptileyqy.entity.GraspRecordEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ReptileMapper {

    @Insert("insert into car_web(id,car_type,part_no,group_no,type,suc,pnc,ch_name,en_name,itc,new_part_no,amount_used," +
            "engine,transmission,option1,option2,option3,option4,option5,option6,option7,option8,option9,option10," +
            "option11,option12,option13,option14,exclusive_op1,remark,create_time)" +
            "values (#{id},#{carType},#{partNo},#{groupNo},#{type},#{suc},#{pnc},#{chName},#{enName},#{itc},#{newPartNo}," +
            "#{amountUsed},#{engine},#{transmission},#{option1},#{option2},#{option3},#{option4},#{option5},#{option6},#{option7}," +
            "#{option8},#{option9},#{option10},#{option11},#{option12},#{option13},#{option14},#{exclusiveOp1},#{remark},#{createTime})")
    int insertCarWeb(CarWebEntity carWebEntity);


    List<CarWebEntity> getGrNoAndPncNo1();


    List<CarWebEntity> getGrNoAndPncNo2();

    List<CarWebEntity> getGrNoAndPncNo3();


    List<CarWebEntity> getGrNoAndPncNo4();


    List<CarWebEntity> getGrNoAndPncNo5();


    List<CarWebEntity> getGrNoAndPncNo6();


    List<CarWebEntity> getGrNoAndPncNo7();


    List<CarWebEntity> getGrNoAndPncNo8();


    List<CarWebEntity> getGrNoAndPncNo9();

    @Insert("insert into grasp_record(id,part_no,part_name,group_no,pnc,start_time,end_time,instruction)" +
            "values (#{id},#{partNo},#{partName},#{groupNo},#{pnc},#{startTime},#{endTime},#{instruction})")
    int insertGraspRecord(GraspRecordEntity graspRecordEntity);

    @Select(" SELECT * FROM (SELECT part_no AS partNo,group_no as groupNo,pnc,start_time as startTime,end_time as endTime,instruction,part_name as partName FROM grasp_record GROUP BY part_no,group_no,pnc,start_time,end_time,instruction,part_name) AS a LIMIT 0,60000 ")
    List<GraspRecordEntity> getGraspRecordList();

    @Insert("insert into error_record1(id,exception_pnc) " +
            "values (#{id},#{exceptionPnc})")
    int insertErrorRecord(ErrorRecordEntity errorRecordEntity);


    List<CarWebEntity> getGrNoAndPncNo();

    List<GraspRecordEntity> getGraspRecordList1();

    List<GraspRecordEntity> getGraspRecordList2();

    List<GraspRecordEntity> getGraspRecordList3();

    List<GraspRecordEntity> getGraspRecordList4();
    List<GraspRecordEntity> getGraspRecordList5();
    List<GraspRecordEntity> getGraspRecordList6();
    List<GraspRecordEntity> getGraspRecordList7();
    List<GraspRecordEntity> getGraspRecordList8();
    List<GraspRecordEntity> getGraspRecordList9();
    List<GraspRecordEntity> getGraspRecordList10();
    List<GraspRecordEntity> getGraspRecordList11();
    List<GraspRecordEntity> getGraspRecordList12();
    List<GraspRecordEntity> getGraspRecordList13();
    List<GraspRecordEntity> getGraspRecordList14();
    List<GraspRecordEntity> getGraspRecordList15();
    List<GraspRecordEntity> getGraspRecordList16();
    List<GraspRecordEntity> getGraspRecordList17();
    List<GraspRecordEntity> getGraspRecordList18();
    List<GraspRecordEntity> getGraspRecordList19();
    List<GraspRecordEntity> getGraspRecordList20();
    List<GraspRecordEntity> getGraspRecordList21();
    List<GraspRecordEntity> getGraspRecordList22();
    List<GraspRecordEntity> getGraspRecordList23();
    List<GraspRecordEntity> getGraspRecordList24();
    List<GraspRecordEntity> getGraspRecordList25();
    List<GraspRecordEntity> getGraspRecordList26();
    List<GraspRecordEntity> getGraspRecordList27();
    List<GraspRecordEntity> getGraspRecordList28();
    List<GraspRecordEntity> getGraspRecordList29();
    List<GraspRecordEntity> getGraspRecordList30();
    List<GraspRecordEntity> getGraspRecordList31();
    List<GraspRecordEntity> getGraspRecordList32();
    List<GraspRecordEntity> getGraspRecordList33();
    List<GraspRecordEntity> getGraspRecordList34();
    List<GraspRecordEntity> getGraspRecordList35();
    List<GraspRecordEntity> getGraspRecordList36();
    List<GraspRecordEntity> getGraspRecordList37();
    List<GraspRecordEntity> getGraspRecordList38();
    List<GraspRecordEntity> getGraspRecordList39();
}
