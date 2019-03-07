package ict.ac.humanmotion.uapplication.lpmsbs.model;

/**
 * Created by Administrator on 2018/8/12 0012.
 */

public class Operation {

    private String Extra;

    private Integer Id;

    private String  Imgsc;

    private Integer Isfinished;

    private String Operationdetail;

    private Integer Order;

    private Worktable WorkID;

    public String getExtra() {
        return Extra;
    }

    public void setExtra(String extra) {
        Extra = extra;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getImgsc() {
        return Imgsc;
    }

    public void setImgsc(String imgsc) {
        Imgsc = imgsc;
    }

    public Integer getIsfinished() {
        return Isfinished;
    }

    public void setIsfinished(Integer isfinished) {
        Isfinished = isfinished;
    }

    public String getOperationdetail() {
        return Operationdetail;
    }

    public void setOperationdetail(String operationdetail) {
        Operationdetail = operationdetail;
    }

    public Integer getOrder() {
        return Order;
    }

    public void setOrder(Integer order) {
        Order = order;
    }

    public Worktable getWorkID() {
        return WorkID;
    }

    public void setWorkID(Worktable workID) {
        WorkID = workID;
    }
}
