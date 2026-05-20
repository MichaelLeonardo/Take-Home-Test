package com.example.takehometest.base;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class BaseSpecification<T> {
    private Boolean caseSensitive = false;

    public Specification<T> containsTextInAttributes(String text, List<String> attributes) {
        if (!text.contains("%")) {
            text = "%" + text + "%";
        }
        final String finalText = caseSensitive ? text : text.toLowerCase();
        return (root, query, builder) -> builder.or(root.getModel().getDeclaredSingularAttributes().stream()
                .filter(a -> attributes.contains(a.getName()))
                .map(a -> builder.like(caseSensitive ? root.get(a.getName()) : builder.lower(root.get(a.getName())), finalText))
                .toArray(Predicate[]::new)
        );
    }

    public abstract Specification<T> containsTextInOmni(String text);

    public Specification<T> containsTextInOmni(String text, Map<String, String> queryMap) {
        Specification<T> containsSpec = this.containsTextInOmni(text);
        if (queryMap.size() > 0) {
            return containsSpec.and(this.generateFilter(queryMap));
        } else {
            return containsSpec;
        }
    }

    public Specification<T> generateFilter(Map<String, String> queryMap) {

        String filterKeyword = "filter.";
        int keywordLength = filterKeyword.length();
        Map<String, String> filterMap;

        filterMap = queryMap.entrySet().stream()
                .filter(map -> map.getKey().startsWith(filterKeyword))
                .collect(Collectors.toMap(p -> p.getKey().substring(p.getKey().indexOf(filterKeyword) + keywordLength), p -> p.getValue()));
        Map<String, BaseFilterCriteria> mapCriteria = extractFilter(filterMap);

        return (root, query, builder) -> {
            Predicate[] predicates = root.getModel().getSingularAttributes().stream()
                    .filter(a -> {
                        Boolean isNullValue = false;
                        BaseFilterCriteria baseFilterCriteria = mapCriteria.get(a.getName());
                        if (null != baseFilterCriteria) {
                            isNullValue = (baseFilterCriteria.getValue() == null);
                        }
                        return mapCriteria.containsKey(a.getName()) && !isNullValue;
                    })
                    .map(a -> {
                        BaseFilterCriteria baseFilterCriteria = mapCriteria.get(a.getName());
                        if (root.get(baseFilterCriteria.getKey()).getJavaType().getSimpleName().equalsIgnoreCase("integer") ||
                                root.get(baseFilterCriteria.getKey()).getJavaType().getSimpleName().equalsIgnoreCase("bigdecimal")) {
                            if (null == baseFilterCriteria.getOperation() ||
                                    "".equalsIgnoreCase(baseFilterCriteria.getOperation()) ||
                                    "ew".equalsIgnoreCase(baseFilterCriteria.getOperation()) ||
                                    "sw".equalsIgnoreCase(baseFilterCriteria.getOperation())
                            ) {
                                baseFilterCriteria.setOperation("eq");
                            }
                        }
                        return generateFilter(builder, root, baseFilterCriteria);
                    })
                    .toArray(Predicate[]::new);
            String mode = queryMap.get("filterMode");
            if ("or".equalsIgnoreCase(mode)) {
                return builder.or(predicates);
            } else {
                return builder.and(predicates);
            }
        };
    }

    public Predicate generateFilter(CriteriaBuilder builder, Root<T> root, BaseFilterCriteria criteria) {
        String strVal;
        if (null == criteria.getValue()) {
            strVal = "";
        } else {
            strVal = criteria.getValue().toString().toLowerCase();
        }
        if ("eq".equalsIgnoreCase(criteria.getOperation())) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if ("neq".equalsIgnoreCase(criteria.getOperation())) {
            return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
        } else if ("gt".equalsIgnoreCase(criteria.getOperation())) {
            return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if ("gteq".equalsIgnoreCase(criteria.getOperation())) {
            return builder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if ("lt".equalsIgnoreCase(criteria.getOperation())) {
            return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if ("lteq".equalsIgnoreCase(criteria.getOperation())) {
            return builder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if ("ew".equalsIgnoreCase(criteria.getOperation())) {
            return builder.like(caseSensitive ? root.get(criteria.getKey()) : builder.lower(root.get(criteria.getKey())), "%" + strVal);
        } else if ("sw".equalsIgnoreCase(criteria.getOperation())) {
            return builder.like(caseSensitive ? root.get(criteria.getKey()) : builder.lower(root.get(criteria.getKey())), strVal + "%");
        } else {
            if ((root.get(criteria.getKey()).getJavaType().getSimpleName().equalsIgnoreCase("integer")) ||
                    (root.get(criteria.getKey()).getJavaType().getSimpleName().equalsIgnoreCase("bigdecimal"))) {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
            return builder.like(caseSensitive ? root.get(criteria.getKey()) : builder.lower(root.get(criteria.getKey())), "%" + strVal + "%");
        }
    }

    public Map<String, BaseFilterCriteria> extractFilter(Map<String, String> filterMap) {

        // https://www.ibm.com/support/knowledgecenter/SSLKT6_7.6.0.9/com.ibm.mif.doc/gp_intfrmwk/rest_api/c_resource_attribute_param.html

        //"eq", // Equals
        //"neq", // Not equals
        //"gt", // Greater than
        //"gteq",  // Greater than equals
        //"lt", // Less than
        //"lteq", // Less than equals
        //"ew", // Ends with
        //"sw" // Starts with

        String[] operations = new String[]{
                // agar tidak perlu sorting lagi setiap run
                "eq", // Equals
                "ew", // Ends with
                "gt", // Greater than
                "gteq",  // Greater than equals
                "lt", // Less than
                "lteq", // Less than equals
                "neq", // Not equals
                "sw" // Starts with
        };
        // Arrays.sort(operations);

        return filterMap.entrySet().stream()
                .collect(Collectors.toMap(map -> map.getKey(), map -> {
                    BaseFilterCriteria criteria = new BaseFilterCriteria();
                    int i = 1;
                    StringTokenizer token = new StringTokenizer(map.getKey(), ".");

                    // extract key dan mode(orMode/andMode)
                    while (token.hasMoreTokens()) {
                        String tokenStr = token.nextToken();
                        if (i == 1) {
                            criteria.setKey(tokenStr);
                        }
                        if (i++ == 2) {
                            criteria.setMode(tokenStr);
                        }
                    }

                    // extract condition dan filterValue
                    i = 1;
                    token = new StringTokenizer(map.getValue(), "~");
                    while (token.hasMoreTokens()) {
                        String tokenStr = token.nextToken();
                        if (Arrays.binarySearch(operations, tokenStr) > -1) {
                            criteria.setOperation(tokenStr);
                        } else {
                            criteria.setValue(tokenStr);
                        }
                    }

                    return criteria;
                }));

    }

    public Specification<T> notDeleted() {
        return (root, query, builder) -> builder.equal(root.get("isDeleted"), Boolean.FALSE);
    }

    public Specification<T> isDeleted() {
        return (root, query, builder) -> builder.equal(root.get("isDeleted"), Boolean.TRUE);
    }

}
