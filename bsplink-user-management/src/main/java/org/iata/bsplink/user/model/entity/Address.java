package org.iata.bsplink.user.model.entity;

import static org.iata.bsplink.user.validation.ValidationMessages.INCORRECT_SIZE;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.user.model.view.UserTemplateView;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "address")
@EntityListeners(AuditingEntityListener.class)
@JsonView(UserTemplateView.class)
public class Address implements Serializable {

    private static final long serialVersionUID = -9049001224698825736L;

    @Id
    @JsonIgnore
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
    private Long id;

    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    @ApiModelProperty(value = "Address description", required = false)
    private String description;

    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    @ApiModelProperty(value = "Locality", required = false)
    private String locality;

    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    @ApiModelProperty(value = "City", required = false)
    private String city;

    @Size(max = 10, message = INCORRECT_SIZE + "max 10")
    @ApiModelProperty(value = "Zip code", required = false)
    private String zip;

    @Size(max = 26, message = INCORRECT_SIZE + "max 26")
    @ApiModelProperty(value = "country", required = false)
    private String country;

}
