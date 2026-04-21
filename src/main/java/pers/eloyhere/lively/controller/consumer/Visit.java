package pers.eloyhere.lively.controller.consumer;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pers.eloyhere.lively.entity.BaseEntity;

@Entity
@Table(name = "visit")
public class Visit extends BaseEntity {

    private String module;


}