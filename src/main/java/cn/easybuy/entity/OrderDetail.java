package cn.easybuy.entity;

import java.io.Serializable;

public class OrderDetail implements Serializable{
	private Integer id;//ID
	private Integer orderId;//订单ID
	private Integer quantity;//数量 
	private Float cost;//单价
	private Integer productId;//商品id

	private Product product;//商品

	public OrderDetail() {
	}

	public OrderDetail(Integer id, Integer orderId, Integer quantity, Float cost, Integer productId, Product product) {
		this.id = id;
		this.orderId = orderId;
		this.quantity = quantity;
		this.cost = cost;
		this.productId = productId;
		this.product = product;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
