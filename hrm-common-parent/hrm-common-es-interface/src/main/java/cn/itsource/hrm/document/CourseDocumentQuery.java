package cn.itsource.hrm.document;

import lombok.Data;

@Data
public class CourseDocumentQuery {

    private Integer page;
    private Integer rows;
    private String keyword;
    private Long courseTypeId;
    private Double minPrice;
    private Double maxPrice;
    //以哪一个字段排序
    private String orderFiled;
    //1表示降序 0表示升序
    private Integer orderType;
}
