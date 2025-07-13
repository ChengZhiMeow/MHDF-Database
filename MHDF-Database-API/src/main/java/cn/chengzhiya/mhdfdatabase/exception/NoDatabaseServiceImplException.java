package cn.chengzhiya.mhdfdatabase.exception;

public class NoDatabaseServiceImplException extends Exception {
    public NoDatabaseServiceImplException(String type) {
        super("找不到数据库类型 " + type + " 的数据库实现");
    }
}
