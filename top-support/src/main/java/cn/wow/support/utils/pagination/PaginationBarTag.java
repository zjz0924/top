package cn.wow.support.utils.pagination;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class PaginationBarTag extends TagSupport {
	private static final long serialVersionUID = 1L;
	private String id;
	private String t = "0";
	private String u = "10";
	private int e = 0;
	private String v;
	private int f = 0;
	private String w = "true";
	private String x = "false";

	public String getId() {
		return this.id;
	}

	public void setId(String paramString) {
		this.id = paramString;
	}

	public String getPageSize() {
		return this.u;
	}

	public void setPageSize(String paramString) {
		this.u = paramString;
	}

	public String getStartRow() {
		return this.t;
	}

	public void setStartRow(String paramString) {
		this.t = paramString;
	}

	public String getTotalSize() {
		return this.v;
	}

	public void setTotalSize(String paramString) {
		this.v = paramString;
	}

	public String getShowdetail() {
		return this.x;
	}

	public void setShowdetail(String paramString) {
		this.x = paramString;
	}

	public String getShowbar() {
		return this.w;
	}

	public void setShowbar(String paramString) {
		if ((paramString == null) || ("".equals(paramString)))
			return;
		this.w = paramString;
	}

	public int getCurrPageNo() {
		int i = 0;
		int j = 10;
		try {
			i = Integer.parseInt(this.t);
		} catch (Exception localException1) {
		}
		try {
			j = Integer.parseInt(this.u);
		} catch (Exception localException2) {
		}
		return (i / j + 1);
	}

	public int getLastPage() {
		int i = 0;
		int j = 10;
		try {
			i = Integer.parseInt(this.v);
		} catch (Exception localException1) {
		}
		try {
			j = Integer.parseInt(this.u);
		} catch (Exception localException2) {
		}
		return ((i % j == 0) ? i / j : i / j + 1);
	}

	public String getNumNavBar() {
		int i = this.e;
		StringBuffer localStringBuffer = new StringBuffer();
		int j;
		if ((i >= 6) && (i + 3 < this.f) && (6 < this.f)) {
			localStringBuffer.append("<li><a href='javascript:gopage(1,\"" + getId() + "\")'>" + 1 + "</a></li>");
			localStringBuffer.append("<li><a href='javascript:gopage(2,\"" + getId() + "\")'>" + 2 + "</a></li>");
			
			for (j = i - 1; j <= i + 2; ++j) {
				if ((j == i - 1) && (this.f > 8)){
					localStringBuffer.append("<li><span class='ellipsis'>...</span></li>");
				}
				if (j == i){
					localStringBuffer.append("<li class=\"active\"><span>" + j + "</span></li>");
				}else{
					localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
				}
			}
			if (this.f - i != 4){
				localStringBuffer.append("<li><span class='ellipsis'>...</span></li>");
			}
			
			j = this.f - 1;
			localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
			localStringBuffer.append("<li><a href='javascript:gopage(" + this.f + ",\"" + getId() + "\")'>" + this.f + "</a></li>");
		} else if ((i >= 6) && (i + 3 >= this.f) && (6 < this.f)) {
			localStringBuffer.append("<li><a href='javascript:gopage(1,\"" + getId() + "\")'>" + 1 + "</a></li>");
			localStringBuffer.append("<li><a href='javascript:gopage(2,\"" + getId() + "\")'>" + 2 + "</a></li>");
			for (j = this.f - 5; j <= this.f; ++j) {
				if ((j == this.f - 5) && (this.f > 8)){
					localStringBuffer.append("<li><span class='ellipsis'>...</span></li>");
				}
				if (j == i){
					localStringBuffer.append("<li class=\"active\"><span>" + j + "</span></li>");
				}else{
					localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
				}
			}
		} else if ((i < 6) && (this.f > 8)) {
			for (j = 1; j <= 6; ++j){
				if (j == i){
					localStringBuffer.append("<li class=\"active\"><span>" + j + "</span></li>");
				}else{
					localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
				}
			}
			
			if (this.f - i > 2) {
				localStringBuffer.append("<li><span class='ellipsis'>...</span></li>");
				j = this.f - 1;
				localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
				localStringBuffer.append("<li><a href='javascript:gopage(" + this.f + ",\"" + getId() + "\")'>" + this.f + "</a></li>");
			} else {
				localStringBuffer.append("<li><span class='ellipsis'>...</span></li>");
			}
		} else {
			for (j = 1; j <= this.f; ++j){
				if (j == i){
					localStringBuffer.append("<li class=\"active\"><span>" + j + "</span></li>");
				}else{
					localStringBuffer.append("<li><a href='javascript:gopage(" + j + ",\"" + getId() + "\")'>" + j + "</a></li>");
				}
			}
		}
		return localStringBuffer.toString();
	}

	public int doEndTag() throws JspException {
		try {
			JspWriter localJspWriter = this.pageContext.getOut();
			this.e = getCurrPageNo();
			this.f = getLastPage();
			if ((this.f < 2) && ("false".equals(this.w))){
				return 6;
			}
				
			localJspWriter.write("<script type='text/javascript'>");
			localJspWriter.write("nav_ready(function(){");
			localJspWriter.write("nav_add('" + getId() + "','startRow'," + this.t + ");");
			localJspWriter.write("nav_add('" + getId() + "','pageSize'," + this.u + ");");
			localJspWriter.write("nav_add('" + getId() + "','totalSize'," + this.v + ");");
			localJspWriter.write("});");
			localJspWriter.write("</script>");
			localJspWriter.write("<div style='text-align:right;margin-right:20px;'>");
			localJspWriter.write("<ul class='pagination'>");
			
			if (!("0".equals(this.v))) {
				if (this.e == 1){
					localJspWriter.write("<li class='page-pre disabled'><span><</span></li>");// 上一页
				}else{
					localJspWriter.write("<li><a href='javascript:pre_page(\"" + getId() + "\");'><</a></li>");// 上一页
				}
				
				localJspWriter.write(getNumNavBar());
				if (this.f == this.e){
					localJspWriter.write("<li class='page-pre disabled'><span>></span>");// 下一页
				}else{
					localJspWriter.write("<li><a href='javascript:next_page(\"" + getId() + "\");'>></a></li>");// 下一页
				}
				
				if ("true".equals(this.x)) {
					localJspWriter.write("<li><span>");
					localJspWriter.write("共&nbsp;" + this.v + "&nbsp;条记录");
					localJspWriter.write("</span></li>");
				}
			}
			localJspWriter.write("</ul>");
			localJspWriter.write("</div>");
			return 6;
		} catch (IOException localIOException) {
			throw new JspException("分页错误.");
		}
	}
}