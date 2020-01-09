package cn.itsource.hrm.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@Document(indexName = "hrm",type = "course")
public class CourseDocument {
    @Id
    private Long id;

    private String name;

    private String users;

    private Long courseTypeId;

    private String courseTypeName;

    private Long gradeId;

    private String gradeName;

    private Integer status;

    private Long tenantId;

    private String tenantName;

    private Long userId;

    private String userName;

    private Long startTime;

    private Long endTime;

    private String intro;

    private String resources; //图片

    private Long expires; //过期时间

    private BigDecimal priceOld; //原价

    private BigDecimal price;

    private String qq;

    //关键字搜索，把关键字搜索所有设计到的列 字符串拼接到一个all字段中
    // 课程名称: java提高课程
    // 课程介绍: java很牛逼
    // 机构名称: 源码时代，成就高新
    // ALL = Java提高课程 java很牛逼 源码时代，成就高新
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String all;
}
