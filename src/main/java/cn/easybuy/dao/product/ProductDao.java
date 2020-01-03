package cn.easybuy.dao.product;

import java.util.List;

import cn.easybuy.entity.Product;
import org.apache.ibatis.annotations.Param;

/**
 * 商品查询Dao
 *
 * deleteById(Integer id)
 * getById(Integer id)
 * getRowCount(params)
 * getRowList(params)
 *
 */
public interface ProductDao {

	Integer updateStock(@Param("id") Integer id, @Param("stock") Integer quantity) throws Exception;
	
	public Integer add(Product product) throws Exception;

	public Integer update(Product product) throws Exception;
	
	public Integer deleteProductById(Integer id) throws Exception;
	
	public Product getProductById(Integer id)throws Exception;
	
	public List<Product> getProductList(
            @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,
            @Param("name") String proName, @Param("categoryId") Integer categoryId,
            @Param("level") Integer level)throws Exception;
	
	public Integer queryProductCount(
            @Param("name") String proName, @Param("categoryId") Integer categoryId,
            @Param("level") Integer level)throws Exception;
}
