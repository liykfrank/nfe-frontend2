package org.iata.bsplink.user.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
@Embeddable
public class UserTemplate implements Serializable {

    private static final long serialVersionUID = 1107405596572166130L;

    @ApiModelProperty(value = "Identifier")
    @Id
    @Column(length = 69)
    @JsonIgnore
    private String id;

    @ApiModelProperty(value = "Id for User")
    @Column(name = "user_id", length = 36)
    @JsonIgnore
    private String userId;

    @ApiModelProperty(value = "Id for Bsplink Template")
    @Column(name = "template", length = 32)
    private String template;

    @ElementCollection
    @Column(length = 2)
    private List<String> isoCountryCodes;
}
