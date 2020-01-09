package cn.itsource.hrm.service;

public interface ICodeService {

    String genCode(String key);

    boolean validateCode(String key, String code);
}
