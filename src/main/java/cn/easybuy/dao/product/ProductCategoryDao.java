package cn.easybuy.dao.product;

import cn.easybuy.entity.ProductCategory;
import cn.easybuy.param.ProductCategoryParam;

import java.util.List;

/**
 * Created by bdqn on 2016/5/12.
 * addObject(UserAddress userAddress)
 * getRowList(params)
 * getRowCount(params)
 * getById(Integer id)
 * updateByQuery(params)
 */
public interface ProductCategoryDao {
	/**
	 * 根据id删除商品
	 * @param id
	 */
	void deleteById(Integer id);//删除商品分类
	/**
	 * 根据条件查询商品列表
	 * @param param
	 */
	public List<ProductCategory> queryProductCategoryList(ProductCategoryParam param);
	/**
	 * 根据id查询商品分类
	 * @param id
	 */
	public ProductCategory queryProductCategoryById(Integer id);
	/**
	 * 添加商品分类
	 * @param productCategory
	 */
	public Integer add(ProductCategory productCategory) ;
	/**
	 * 根据参数查询商品分类的数目
	 * @param param
	 */
	public Integer queryProductCategoryCount(ProductCategoryParam param);
	/**
	 * 修改商品分类
	 * @param productCategory
	 */
	public void update(ProductCategory productCategory) ;
}
