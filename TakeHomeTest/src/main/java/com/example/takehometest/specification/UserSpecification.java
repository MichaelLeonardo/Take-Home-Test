package com.example.takehometest.specification;

import com.example.takehometest.base.BaseSpecification;
import com.example.takehometest.model.Role;
import com.example.takehometest.model.User;
import com.example.takehometest.utils.Helper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserSpecification extends BaseSpecification<User> {

    @Override
    public Specification<User> containsTextInOmni(String text) {
        return containsTextInAttributes(text, Arrays.asList("username"));
    }

    public Specification<User> findBySearch(String search) {
        if (search != null && !search.isEmpty()) {
            return (root, query, builder) -> {
                // Spb.spbNumber LIKE :search
                return builder.like(builder.lower(root.get("username")), "%" + search.toLowerCase() + "%");
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

    public Specification<User> sortBy(String sortBy, Boolean isAsc) {
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
}
