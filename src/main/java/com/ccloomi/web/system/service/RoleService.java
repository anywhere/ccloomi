package com.ccloomi.web.system.service;

import java.util.List;

import com.ccloomi.core.common.service.BaseService;
import com.ccloomi.web.system.entity.RoleEntity;
import com.ccloomi.web.system.entity.ViewEntity;

/**© 2015-2015 CCLooMi.Inc Copyright
 * 类    名：RoleService
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2015年7月4日-上午8:55:14
 */
public interface RoleService extends BaseService<RoleEntity>{
	public List<ViewEntity> findViewsByIdUser(String idUser);
	public List<String> findPermissionsByIdUser(String idUser);
	public List<String> findRolesByIdUser(String idUser);
}
