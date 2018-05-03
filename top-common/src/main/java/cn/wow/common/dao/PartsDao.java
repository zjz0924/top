package cn.wow.common.dao;

import cn.wow.common.domain.Parts;

public interface PartsDao extends SqlDao{

    Parts selectByCode(String code);
}