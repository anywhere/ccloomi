package com.ccloomi.web.system.service;

import java.util.Map;

import com.ccloomi.core.common.service.BaseService;
import com.ccloomi.web.system.entity.DataDictionaryEntity;

/**© 2015-2015 CCLooMi.Inc Copyright
 * 类    名：DataDictionaryService
 * 类 描 述：
 * 作    者：Chenxj
 * 邮    箱：chenios@foxmail.com
 * 日    期：2015年9月19日-下午11:05:42
 */
public interface DataDictionaryService extends BaseService<DataDictionaryEntity>{

	/**描述：
	 * 作者：Chenxj
	 * 日期：2015年11月14日 - 下午9:06:40
	 * @param id
	 * @return
	 */
	public int removeById(Object id);

	/**描述：
	 * 作者：Chenxj
	 * 日期：2015年11月14日 - 下午9:06:47
	 * @param map
	 * @return
	 */
	public Map<String, Object> findddsByPage(Map<String, Object> map);
	
}
