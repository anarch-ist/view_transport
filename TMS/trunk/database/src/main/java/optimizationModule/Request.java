package optimizationModule;


import java.util.Date;

public class Request{
    private int requestId;
    private int clientId;
    private String deliveryAddress;
    private Date plannedDeliveryTime;

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getPlannedDeliveryTime() {
        return plannedDeliveryTime;
    }

    public void setPlannedDeliveryTime(Date plannedDeliveryTime) {
        this.plannedDeliveryTime = plannedDeliveryTime;
    }
}
