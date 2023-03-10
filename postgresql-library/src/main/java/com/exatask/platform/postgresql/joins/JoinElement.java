package com.exatask.platform.postgresql.joins;

import com.exatask.platform.postgresql.AppModel;
import com.exatask.platform.postgresql.utilities.QueryUtility;
import lombok.Getter;

import javax.persistence.criteria.JoinType;
import java.util.ArrayList;
import java.util.List;

public class JoinElement {

  private final Class<? extends AppModel> model;

  @Getter
  private final String attribute;

  @Getter
  private final JoinType type;

  private final List<ConditionElement> conditions;

  @Getter
  private final List<JoinElement> joins;

  public JoinElement(String attribute, JoinType type) {

    this.attribute = attribute;
    this.type = type;
    this.model = null;
    this.conditions = null;
    this.joins = null;
  }

  public JoinElement(String attribute, JoinType type, List<JoinElement> joins) {

    this.attribute = attribute;
    this.type = type;
    this.model = null;
    this.conditions = null;
    this.joins = joins;
  }

  public JoinElement(Class<? extends AppModel> model, JoinType type, List<ConditionElement> conditions) {

    this.model = model;
    this.type = type;
    this.conditions = conditions;
    this.attribute = null;
    this.joins = null;
  }

  public String getJoin() {

    List<String> joinConditions = new ArrayList<>();
    for (ConditionElement condition : conditions) {
      joinConditions.add(condition.getCondition(model));
    }

    return String.format(" %s JOIN %s AS %s ON %s ",
        type.name(),
        QueryUtility.getTableName(model),
        QueryUtility.getClassAlias(model),
        String.join(" AND ", joinConditions)
    );
  }
}
