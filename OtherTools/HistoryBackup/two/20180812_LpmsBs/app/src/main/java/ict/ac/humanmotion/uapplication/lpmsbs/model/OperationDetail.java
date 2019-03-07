package ict.ac.humanmotion.uapplication.lpmsbs.model;

public class OperationDetail {
    private int type; //标志是否正在进行 0 进行 1 不进行
    private String detail;//操作项目条目的详细描述
    private int order;//操作顺序

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
