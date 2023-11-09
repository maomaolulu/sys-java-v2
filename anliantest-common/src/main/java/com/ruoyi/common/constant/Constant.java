package com.ruoyi.common.constant;


public class Constant {
    public static final int SUPER_ADMIN = 1;
    public static final String SQL_FILTER = "sql_filter";
    public static final String PAGE = "page";
    public static final String LIMIT = "limit";
    public static final String ORDER_FIELD = "sidx";
    public static final String ORDER = "order";
    public static final String ASC = "asc";

    public Constant() {
    }

    public static enum CloudService {
        QINIU(1),
        ALIYUN(2),
        QCLOUD(3);

        private int value;

        private CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum ScheduleStatus {
        NORMAL(0),
        PAUSE(1);

        private int value;

        private ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static enum MenuType {
        CATALOG(0),
        MENU(1),
        BUTTON(2);

        private int value;

        private MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}