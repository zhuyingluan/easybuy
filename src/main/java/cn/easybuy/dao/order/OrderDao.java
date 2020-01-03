package cn.easybuy.dao.order;
import java.util.List;

import cn.easybuy.entity.Order;
import org.apache.ibatis.annotations.Param;

/***
 * 订单处理的dao层
 * getRowCount
 * getRowList(Params params)
 * getById(Integer id)
 * addObject(Params params)
 */
public interface OrderDao {

	public void add(Order order) ;

	public void deleteById(Integer id);
	
	public Order getOrderById(Integer id) ;
	
	public List<Order> getOrderList(@Param("userId") Integer userId, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize) ;
	
	public Integer count(@Param("userId") Integer userId);

	Order getOrderBySerialNumber(String serialNumber);
}
