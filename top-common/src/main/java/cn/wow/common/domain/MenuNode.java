package cn.wow.common.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class MenuNode implements Serializable{
	
	private static final long serialVersionUID = -5774273401497230486L;

	private Long id;

    private String name;

    private String url;
    
    private Integer sortNum;
    
    @JsonInclude(Include.NON_NULL) 
    private Long _parentId;
    
    public MenuNode(){
    	
    }
    
	public MenuNode(Long id, String name, String url, Integer sortNum, Long _parentId) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.sortNum = sortNum;
		this._parentId = _parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}

	public Long get_parentId() {
		return _parentId;
	}

	public void set_parentId(Long _parentId) {
		this._parentId = _parentId;
	}
    
}
