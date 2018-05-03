package cn.wow.common.domain;

import java.io.Serializable;

public abstract class JpaEntity implements Serializable {

	private static final long serialVersionUID = -590854761872293234L;

	public abstract Serializable getPrimaryKey();
}
