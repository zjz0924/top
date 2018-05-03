package cn.wow.common.utils.pagination;

import java.util.List;
import java.util.Map;

public interface IPageMap<K,V> extends Map<K,V> {
	
    boolean andIsNull(String property);

    boolean andIsNotNull(String property);

    boolean andEqualTo(String property, Object value);

    boolean andNotEqualTo(String property, Object value);

    boolean andGreaterThan(String property, Object value);

    boolean andGreaterThanOrEqualTo(String property, Object value);

    boolean andLessThan(String property, Object value);

    boolean andLessThanOrEqualTo(String property, Object value);

    boolean andIn(String property, List<Object> values);

    boolean andNotIn(String property, List<Object> values);

    boolean andBetween(String property, Object value1, Object value2);

    boolean andNotBetween(String property, Object value1, Object value2);

    boolean andLike(String property, String value);

    boolean andNotLike(String property, String value);
}
