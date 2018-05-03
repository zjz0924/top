package cn.wow.common.utils.pageinterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 对分页的基本数据进行一个简单的封装
 */
public class Page<T> {

	private int start = 1;// 页码，默认是第一页
	private int pageSize = 15;// 每页显示的记录数，默认是15
	private int totalRecord;// 总记录数
	private int totalPage;// 总页数
	private List<T> results;// 对应的当前页记录
	private Map<String, Object> params = new HashMap<String, Object>();// 其他的参数我们把它分装成一个Map对象

	
	public Page(){}
	
	public Page(int start, int pageSize) {
		super();
		this.start = start;
		this.pageSize = pageSize;
	}
	
	public Page(int start,int pageSize,int totalRecord,List<T> results) {
		this.start = start;
		this.pageSize = pageSize;
		this.results = results;
		this.setTotalRecord(totalRecord);
		
	}
	

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
		// 在设置总条数的时候计算出对应的总页数，在下面的三目运算中加法拥有更高的优先级，所以最后可以不加括号。
		int totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
		this.setTotalPage(totalPage);
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public int getTotalRecord() {
		return totalRecord;
	}
	
}
