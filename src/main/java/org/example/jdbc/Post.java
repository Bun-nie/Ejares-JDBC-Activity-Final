package org.example.jdbc;

import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Post {
    public int postid = 1;
    public int acctid;
    public String title;
    public String content;

    public void setAcctid(int acctid) {
        this.acctid = acctid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

    public int getAcctid() {
        return acctid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getPostid() {
        return postid;
    }
}
