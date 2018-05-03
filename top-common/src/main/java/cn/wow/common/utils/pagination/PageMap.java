package cn.wow.common.utils.pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页Map
 * @author shenjc
 *
 */
public class PageMap extends HashMap<String, Object> implements IPageMap<String, Object> {
	
	private static final long serialVersionUID = 490650441721738525L;
	
	/**
	 * 默认页码
	 */
	private Integer defaultPageNum = 1;
	
	/**
	 * 默认一页大小
	 */
	private Integer defaultPageSize = 10;
	
	private Boolean isPage = true;
	
	public final static String KEY_CUSTOM_ORDER_SQL = "custom_order_sql";
	
	List<Criteria> criterias= new ArrayList<Criteria>();
	
	Example example = new Example();

	
	@Override
	public boolean andIsNull(String property) {
		return criterias.add(example.createCriteria().andIsNull(property));
    }

	@Override
	public boolean andIsNotNull(String property) {
		return criterias.add(example.createCriteria().andIsNotNull(property));
    }
	
	@Override
	public boolean andEqualTo(String property, Object value) {
		return criterias.add(example.createCriteria().andEqualTo(property, value));
	}

	@Override
	public boolean andNotEqualTo(String property, Object value) {
		return criterias.add(example.createCriteria().andNotEqualTo(property, value));
    }

	@Override
	public boolean andGreaterThan(String property, Object value) {
		return criterias.add(example.createCriteria().andGreaterThan(property, value));
    }

	@Override
	public boolean andGreaterThanOrEqualTo(String property, Object value) {
		return criterias.add(example.createCriteria().andGreaterThanOrEqualTo(property, value));
    }

	@Override
	public boolean andLessThan(String property, Object value) {
		return criterias.add(example.createCriteria().andLessThan(property, value));
    }

	@Override
	public boolean andLessThanOrEqualTo(String property, Object value) {
		return criterias.add(example.createCriteria().andLessThanOrEqualTo(property, value));
    }
	
	@Override
	public boolean andIn(String property, List<Object> values) {
		return criterias.add(example.createCriteria().andIn(property, values));
	}

	@Override
	public boolean andNotIn(String property, List<Object> values) {
		return criterias.add(example.createCriteria().andNotIn(property, values));
    }

	@Override
	public boolean andBetween(String property, Object value1, Object value2) {
		return criterias.add(example.createCriteria().andBetween(property, value1, value2));
    }

	@Override
	public boolean andNotBetween(String property, Object value1, Object value2) {
		return criterias.add(example.createCriteria().andNotBetween(property, value1, value2));
    }

	@Override
	public boolean andLike(String property, String value) {
		return criterias.add(example.createCriteria().andLike(property, value));
    }

	@Override
	public boolean andNotLike(String property, String value) {
		return criterias.add(example.createCriteria().andNotLike(property, value));
    }

	public PageMap() {
		this.put("pageNum", defaultPageNum);
		this.put("pageSize", defaultPageSize);
		this.put("criterias", criterias);
	}
	
	public PageMap(Integer pageNum, Integer pageSize) {
		this.put("pageNum", pageNum);
		this.put("pageSize", pageSize);
		this.put("criterias", criterias);
	}
	
	public PageMap(Boolean isPage) {//显示指定是否分页
		this.isPage = isPage;
		this.put("pageNum", defaultPageNum);
		this.put("pageSize", defaultPageSize);
		this.put("criterias", criterias);
	}
	
	public PageMap(HttpServletRequest request) {
		String isPage = request.getParameter("isPage");
		if("false".equals(isPage)) {//显示指定不分页
			this.isPage = false;
		} else {
			this.isPage = true;
			
			String pageSize = request.getParameter("pageSize");
			if (StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)) {
				this.put("pageSize", Integer.parseInt(pageSize));
			} else {
				pageSize = (String) request.getAttribute("pageSize");
				if (StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)) {
					this.put("pageSize", Integer.parseInt(pageSize));
				} else {
					this.put("pageSize", defaultPageSize);
				}
			}		
			
			String pageNum = request.getParameter("pageNum");
			if(StringUtils.isBlank(pageNum)) {//如果没传pageNum，找startSize（适配不传pageNum传startSize的情况）
				String startRow = request.getParameter("startRow");	//开始记录数
				if(StringUtils.isNotBlank(startRow) && StringUtils.isNumeric(startRow) && StringUtils.isNotBlank(pageSize) && StringUtils.isNumeric(pageSize)  ) {//如果有传startSize
					Integer iPageNum = Integer.valueOf(startRow)/Integer.valueOf(pageSize)+1;
					pageNum = ""+ iPageNum;
				}
				
			}
			
			if(StringUtils.isNotBlank(pageNum) && StringUtils.isNumeric(pageNum) ) {
				this.put("pageNum", Integer.parseInt(pageNum));
			} else {
				this.put("pageNum", defaultPageNum);
			}
			
			
		}		
		
		this.put("criterias", criterias);
	}

	public Boolean getIsPage() {
		return isPage;
	}

	public void setIsPage(Boolean isPage) {
		this.isPage = isPage;
	}
	

	
}
