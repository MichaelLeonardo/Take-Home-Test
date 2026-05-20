package com.example.takehometest.model;

import com.example.takehometest.base.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_feature")
@AllArgsConstructor
@NoArgsConstructor
public class Feature extends BaseModel {

    @Id
    private Long id;

    @Column(name = "feature_name")
    private String featureName;

    @Column(name = "menu_url")
    private String menuUrl;

    @Column(name = "menu_code")
    private String menuCode;

    @Column(name = "group_name")
    private String groupName;

}
