package org.iata.bsplink.user.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class UserRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    private String name;

    private boolean isDefault;

    @ElementCollection
    @Column(length = 2)
    private List<String> isoCountryCodes = new ArrayList<>();

}
