package cn.wow.common.utils.pagination;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * 分页Helper
 * @author shenjc
 *
 */
public class PageHelperExt extends PageHelper {

	 /**
     * 开始分页
     *
     * @param params
     */
    public static Page startPage(Object params) {
    	if(params instanceof PageMap) {
    		PageMap pageMap = (PageMap) params;
    		if(pageMap.getIsPage()==true) {//如果是否分页为真，才执行分页
    			Page page = PageHelper.startPage(params);
        		return page;
    		}    		
    	}
    	return null;        
    }
}
