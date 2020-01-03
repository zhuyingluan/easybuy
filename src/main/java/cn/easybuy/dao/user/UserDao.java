package cn.easybuy.dao.user;

import java.util.List;

import cn.easybuy.entity.User;
import org.apache.ibatis.annotations.Param;

/***
 * UserDao 用户业务的dao层
 * 从父类继承下的被使用的方法
 * User getById(userId);
 * Integer userDao.getRowCount(params);
 * List<User> userDao.getRowList(params);
 */
public interface UserDao {

	int add(User user) throws Exception;//新增用户信息

	int update(User user) throws Exception;//更新用户信息

	public int deleteUserById(Integer id) throws Exception;

	/**
	 * 分页获取用户列表
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<User> getUserList(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize)throws Exception;
	
	public Integer count() throws Exception;
	
	public User getUser(@Param("id") Integer id, @Param("loginName") String loginName) throws Exception;
}
