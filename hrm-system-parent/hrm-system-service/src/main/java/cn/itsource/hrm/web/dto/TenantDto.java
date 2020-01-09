package cn.itsource.hrm.web.dto;

import cn.itsource.hrm.domain.Tenant;
import lombok.Data;
import lombok.ToString;

/**
 * @Description 租户注册的参数接收
 * @Author solargen
 * @Date 2019/12/28 11:54
 * @Version v1.0
 **/
@Data
@ToString
public class TenantDto {

    private Tenant tenant;

    private String username;

    private String password;

    private Long meal;

}
