package com.example.takehometest.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface BasePageInterface<T, S extends BaseSpecification<T>, R, ID> {
    default public List<R> getAllList(String search) {
        return new ArrayList<>();
    }

    default public R getById(ID id) { return null; }

    default public R get(String search) { return null; }

    default public Page<R> getAll(Map<String, String> queryMap) {
        return null;
    }

    default public Page<R> getAll(String search, Integer page, Integer limit, List<String> sortBy, Boolean desc, String status) {
        return getAll(search, page, limit, sortBy, desc, status, null);
    }

    default public Page<R> getAll(String search, Integer page, Integer limit, List<String> sortBy, Boolean desc, String status, Map<String, String> queryMap) {
        return null;
    }

    default Pageable defaultPage(String search, Integer page, Integer limit, List<String> sortBy, Boolean desc) {
        page = (page != null) ? page : 0;
        limit = (limit != null) ? limit : 5;

        List<Sort.Order> orders = new ArrayList<>();
        for (String rowSort:sortBy) {
            String[] properties = rowSort.split("\\|");
            Sort.Order order = null;
            if("asc".equalsIgnoreCase(properties[1].toLowerCase())){
                order = new Sort.Order(Sort.Direction.ASC, properties[0]);
            } else{
                order = new Sort.Order(Sort.Direction.DESC, properties[0]);
            }
            orders.add(order);
        }
        Sort sort = Sort.by(orders);
        return PageRequest.of(page, limit, sort);
    }

    default Specification<T> defaultSpec(String search, String status, S spec) {
        search = (search != null) ? search : "";
        status = (status != null) ? status.toLowerCase() : "";
        switch (status) {
            case "isdeleted":
                return spec.containsTextInOmni(search).and(spec.isDeleted());
            case "notdeleted":
                return spec.containsTextInOmni(search).and(spec.notDeleted());
            default: //ambil semua yg delete ataupun tidak
                return spec.containsTextInOmni(search);
        }
    }
}
