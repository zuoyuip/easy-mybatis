package top.zuoyu.mybatis.exception;

/**
 * EasyMybatis异常 .
 *
 * @author: zuoyu
 * @create: 2021-11-14 15:03
 */
public class EasyMybatisException extends CustomException {

    private static final long serialVersionUID = -6192276367409830243L;

    public EasyMybatisException(String message) {
        super(message);
    }

    public EasyMybatisException(String message, Integer code) {
        super(message, code);
    }

    public EasyMybatisException(String message, Throwable e) {
        super(message, e);
    }
}
