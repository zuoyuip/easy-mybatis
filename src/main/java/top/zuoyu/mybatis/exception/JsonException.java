
package top.zuoyu.mybatis.exception;

/**
 * @author zuoyu
 */
public class JsonException extends CustomException {

    private static final long serialVersionUID = -5308647038725582448L;


    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Integer code) {
        super(message, code);
    }

    public JsonException(String message, Throwable e) {
        super(message, e);
    }
}
