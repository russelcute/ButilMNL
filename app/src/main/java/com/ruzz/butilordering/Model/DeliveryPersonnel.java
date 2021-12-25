package com.ruzz.butilordering.Model;

import java.util.List;

public class DeliveryPersonnel {
    private boolean active = false;
    private List<OrderModel> assignedDeliver;

    public DeliveryPersonnel() {}

    public DeliveryPersonnel(List<OrderModel> deliver) {
        this.assignedDeliver = deliver;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<OrderModel> getAssignedDeliver() {
        return assignedDeliver;
    }

    public boolean isActive() {
        return active;
    }
}
