package cn.wow.common.utils.pagination;

import java.util.List;

/**
 * 标准类
 * @author shenjc
 *
 */
public class Criteria {

	public String column;
	
	public String operator;
	
	public Object value;
	
	public Object value2;

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Object getValue2() {
		return value2;
	}

	public void setValue2(Object value2) {
		this.value2 = value2;
	}
	
	
    public Criteria andIsNull(String property) {
		this.setColumn(property);
		this.setOperator("is null");
		return this;
    }

    public Criteria andIsNotNull(String property) {
		this.setColumn(property);
		this.setOperator("is not null");
		return this;
    }
    
    public Criteria andEqualTo(String property, Object value) {
		this.setColumn(property);
		this.setOperator("=");
		this.setValue(value);
		return this;
    }

    public Criteria andNotEqualTo(String property, Object value) {
        this.setColumn(property);
		this.setOperator("<>");
		this.setValue(value);
		return this;
    }

    public Criteria andGreaterThan(String property, Object value) {
		this.setColumn(property);
		this.setOperator(">");
		this.setValue(value);
		return this;
    }

    public Criteria andGreaterThanOrEqualTo(String property, Object value) {
		this.setColumn(property);
		this.setOperator(">=");
		this.setValue(value);
		return this;
    }

    public Criteria andLessThan(String property, Object value) {
		this.setColumn(property);
		this.setOperator("<");
		this.setValue(value);
		return this;
    }

    public Criteria andLessThanOrEqualTo(String property, Object value) {
		this.setColumn(property);
		this.setOperator("<=");
		this.setValue(value);
		return this;
    }
    
	public Criteria andIn(String property, List<Object> values) {
		this.setColumn(property);
		this.setOperator("in");
		this.setValue(values);
		return this;
	}

    public Criteria andNotIn(String property, List<Object> values) {
		this.setColumn(property);
		this.setOperator("not in");
		this.setValue(value);
		return this;
    }

    public Criteria andBetween(String property, Object value1, Object value2) {
		this.setColumn(property);
		this.setOperator("between");
		this.setValue(value1);
		this.setValue2(value2);
		return this;
    }

    public Criteria andNotBetween(String property, Object value1, Object value2) {
    	this.setColumn(property);
		this.setOperator("not between");
		this.setValue(value1);
		this.setValue2(value2);
		return this;
    }

    public Criteria andLike(String property, String value) {
    	this.setColumn(property);
		this.setOperator("like");
		this.setValue(value);
		return this;
    }

    public Criteria andNotLike(String property, String value) {
    	this.setColumn(property);
		this.setOperator("not like");
		this.setValue(value);
		return this;
    }
	
	
}
