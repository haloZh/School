package com.ronguan.bean;

import java.io.Serializable;

/**
 * 用户bean
 * @author zhoujian
 *
 */
public class UserBean{
    public Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public class Query{
        private String run_number;

        public String getRun_number() {
            return run_number;
        }

        public void setRun_number(String run_number) {
            this.run_number = run_number;
        }
    }
}
