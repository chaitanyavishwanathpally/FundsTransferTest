package com.rest.api.persistance;

import org.apache.commons.lang3.StringUtils;

public class GenericDAOQueryCriteria
{
  private String propertyName;
  private Object propertyValue;
  private Object propertyValue1;
  public Object getPropertyValue1() {
	return propertyValue1;
}

public void setPropertyValue1(Object propertyValue1) {
	this.propertyValue1 = propertyValue1;
}

private CriteriaOperator criteriaOperator;

  public GenericDAOQueryCriteria(String propertyName, Object propertyValue, CriteriaOperator criteriaOperator)
  {
    if ((StringUtils.isEmpty(propertyName)) || (criteriaOperator == null)) {
      throw new IllegalArgumentException("PropertyName & criteria cannot be null");
    }
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
    this.criteriaOperator = criteriaOperator;
  }

  public GenericDAOQueryCriteria(String propertyName, Object propertyValue, Object propertyValue1, CriteriaOperator criteriaOperator)
  {
    if ((StringUtils.isEmpty(propertyName)) || (criteriaOperator == null)) {
      throw new IllegalArgumentException("PropertyName & criteria cannot be null");
    }
    this.propertyName = propertyName;
    this.propertyValue = propertyValue;
    this.propertyValue1 = propertyValue1;
    this.criteriaOperator = criteriaOperator;
  }

  public String getPropertyName()
  {
    return this.propertyName;
  }

  public Object getPropertyValue()
  {
    return this.propertyValue;
  }

  public CriteriaOperator getCriteriaOperator()
  {
    return this.criteriaOperator;
  }

  public static enum CriteriaOperator
  {
    LIKE_BEGINS_WITH, LIKE_ENDS_WITH, LIKE_IN_BETWEEN, 
    EQUALS, IN, NOT_IN, LE, GE, GT, LT, IS_NULL, IS_NOT_NULL, 
    IS_EMPTY, IS_NOT_EMPTY, NOT_EQUALS, ASC, DESC, LIKE_CASE_INSENSITIVE, 
    START_LETTER_WITH, matches_in_columns,BETWEEN;
  }
}