package com.ruoyi.common.exception.submit;


/**
 * @Description
 * @Date 2023/5/16 10:32
 * @Author maoly
 **/
public class RepeatSubmitException extends RuntimeException {

    /**
     * 返回给客户端的提示信息
     */
    private final String tips;

    public RepeatSubmitException(String tips) {
        super(tips);
        this.tips = tips;
    }

    public RepeatSubmitException(String tips, String message) {
        super(message);
        this.tips = tips;
    }

    public String getTips() {
        return tips;
    }
}
