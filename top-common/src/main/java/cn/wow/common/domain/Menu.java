package cn.wow.common.domain;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Menu extends JpaEntity {
	
	private static final long serialVersionUID = -7001176848220281363L;

	private Long id;

    private String name;

    private String url;

    private Long pId;

    private Integer sortNum;
    
    @JsonIgnore
    private String isParent;
    
    @JsonIgnore
    private List<Menu> subList;
    
    //菜单别名，用来设置权限，必须唯一
    @JsonIgnore
    private String alias;
    
    // 是否有权限
    @JsonIgnore
    private boolean isAuthorized = true;

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
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public List<Menu> getSubList() {
		return subList;
	}

	public void setSubList(List<Menu> subList) {
		this.subList = subList;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean getIsAuthorized() {
		return isAuthorized;
	}

	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	
	@JsonIgnore
	public Serializable getPrimaryKey() {
		return id;
	}
}