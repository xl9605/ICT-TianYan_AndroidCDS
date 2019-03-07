package ict.ac.humanmotion.uapplication.lpmsbs.model;

/**
 * Created by hbh on 2017/4/20.
 * 实体类，模拟数据
 */

public class WorkListDetail {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开
    private WorkListDetail childBean;

    private String ID;
    private String workListName;

    private String finishTime;
    private String time;
    private String startTime;
    private String oprationPerson;

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOprationPerson() {
        return oprationPerson;
    }

    public void setOprationPerson(String oprationPerson) {
        this.oprationPerson = oprationPerson;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    private String supervisor;

    public String getWorkListName() {
        return workListName;
    }

    public void setWorkListName(String workListName) {
        this.workListName = workListName;
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public WorkListDetail getChildBean() {
        return childBean;
    }

    public void setChildBean(WorkListDetail childBean) {
        this.childBean = childBean;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
