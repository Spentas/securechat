package com.spentas.javad.securechat.model;

/**
 *
 */
public class Message {

    private long id;
    private String from;
    private String to;
    private String time;
    private String type;
    private String extra;
    private String message;

    boolean isMine;

    boolean isStatusMessage;
    /**
     * @param id
     * @param from
     * @param to
     * @param time
     * @param type
     * @param extra
     * @param message
     */
    public Message(long id, String from, String to, String time, String type,
                   String extra, String message) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.time = time;
        this.type = type;
        this.extra = extra;
        this.message = message;
    }
    /**
     * @param from
     * @param to
     * @param time
     * @param type
     * @param extra
     * @param message
     * @param isMine
     */
    public Message(String from, String to, String time, String type,
                   String extra, String message, boolean isMine) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.type = type;
        this.extra = extra;
        this.message = message;
        this.isMine = isMine;
    }

    public Message(String message, boolean isMine) {
        super();
        this.message = message;
        this.isMine = isMine;
        this.isStatusMessage = false;
    }

    public Message(boolean status, String message) {
        super();
        this.message = message;
        this.isMine = false;
        this.isStatusMessage = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }

    public boolean isStatusMessage() {
        return isStatusMessage;
    }

    public void setStatusMessage(boolean isStatusMessage) {
        this.isStatusMessage = isStatusMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTime() {
        return time;
    }


    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExtra() {
        return extra;
    }


    public void setExtra(String extra) {
        this.extra = extra;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Message [id=" + id + ", from=" + from + ", to=" + to
                + ", time=" + time + ", type=" + type + ", extra=" + extra
                + ", message=" + message + ", isMine=" + isMine
                + ", isStatusMessage=" + isStatusMessage + "]";
    }

}
