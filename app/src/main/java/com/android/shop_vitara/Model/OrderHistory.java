package com.android.shop_vitara.Model;

public class OrderHistory {
    String CSTid;
    String OrderNo;
    String OrderDate;
    String PaymentType;
    String PaymentStatus;
    String OrderStatus;
    String Ordid;
    String Odid;
    String Pid;
    String Qty;
    String Price;
    String ShippingCharge;
    String Finalamt;
    String MRP;
    String Discount;
    String DiscPercent;
    String Tax;
String ProductName;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getOdid() {
        return Odid;
    }

    public void setOdid(String odid) {
        Odid = odid;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getShippingCharge() {
        return ShippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        ShippingCharge = shippingCharge;
    }

    public String getFinalamt() {
        return Finalamt;
    }

    public void setFinalamt(String finalamt) {
        Finalamt = finalamt;
    }

    public String getMRP() {
        return MRP;
    }

    public void setMRP(String MRP) {
        this.MRP = MRP;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getDiscPercent() {
        return DiscPercent;
    }

    public void setDiscPercent(String discPercent) {
        DiscPercent = discPercent;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public OrderHistory() {
    }

    public String getCSTid() {
        return CSTid;
    }

    public void setCSTid(String CSTid) {
        this.CSTid = CSTid;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrdid() {
        return Ordid;
    }

    public void setOrdid(String ordid) {
        Ordid = ordid;
    }
}
