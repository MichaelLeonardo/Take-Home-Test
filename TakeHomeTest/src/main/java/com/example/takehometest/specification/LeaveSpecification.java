package com.example.takehometest.specification;

import com.example.takehometest.base.BaseSpecification;
import com.example.takehometest.model.Leave;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.User;
import com.example.takehometest.utils.Helper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Component
public class LeaveSpecification extends BaseSpecification<Leave> {

    @Override
    public Specification<Leave> containsTextInOmni(String text) {
        return containsTextInAttributes(text, Arrays.asList("id"));
    }

    public Specification<Role> findBySearch(String search) {
        if (search != null && !search.isEmpty()) {
            return (root, query, builder) -> {
                // Spb.spbNumber LIKE :search
                return builder.like(builder.lower(root.get("roleName")), "%" + search.toLowerCase() + "%");
            };
        } else {
            return null;
        }
    }

    private String mappingField(String field) {
        // Mapping field from camelCase to snake_case
        // only for field that has different name in database
        String column = field != null ? Helper.getCamelCaseName(field) : null;
        if (column != null) {
            switch (column) {
                default:
                    return column;
            }
        } else {
            return null;
        }
    }

    public Specification<Leave> sortBy(String sortBy, Boolean isAsc) {
        if (sortBy != null) {
            String column = mappingField(sortBy);
            if (column != null) {
                return (root, query, builder) -> {
                    // column can be nested attribute, so we need to split it
                    String[] columns = column.split("\\.");
                    if (columns.length > 1) {
                        // if column is nested attribute, we need to join the table
                        query.orderBy(isAsc ? builder.asc(root.join(columns[0]).get(columns[1])) : builder.desc(root.join(columns[0]).get(columns[1])));
                    } else {
                        // if column is not nested attribute, we can directly order by the column
                        query.orderBy(isAsc ? builder.asc(root.get(column)) : builder.desc(root.get(column)));
                    }
                    return query.getRestriction();
                };
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Specification<Leave> findByRangeDates(Date dateFrom, Date dateTo) {
        if (dateFrom != null && dateTo != null) {
            if (dateFrom.after(dateTo)) {
                return null;
            } else {
                return ((root, query, builder) -> {
                    // Leave apply at between dateFrom and dateTo
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateTo);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    Date endOfDay = cal.getTime();

                    return builder.between(root.get("createdAt"), dateFrom, endOfDay);
                });
            }
        } else {
            return null;
        }
    }

    public Specification<Leave> findByStatus(Integer status) {
        if (status == null) {
            return null;
        }
        return ((root, query, builder) -> {
            return builder.equal(root.get("leaveStatus"), status);
        });
    }

    public Specification<Leave> findByRequester(User user) {
        if (user == null) {
            return null;
        }
        return ((root, query, builder) -> {
            return builder.equal(root.get("user"), user);
        });
    }
}


