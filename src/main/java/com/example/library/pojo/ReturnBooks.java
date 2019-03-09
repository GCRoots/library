package com.example.library.pojo;

public class ReturnBooks {
    private String userName;

    private String title;

    private String borrowingTime;

    private String returnTime;

    private String isReturned;

    private String tid;

    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBorrowingTime() {
        return borrowingTime;
    }

    public void setBorrowingTime(String borrowingTime) {
        this.borrowingTime = borrowingTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(String isReturned) {
        this.isReturned = isReturned;
    }




}
