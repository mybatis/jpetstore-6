package org.mybatis.jpetstore.persistence;

import java.util.List;

import org.mybatis.jpetstore.domain.Category;

public interface CategoryMapper {

  List<Category> getCategoryList();

  Category getCategory(String categoryId);

}
