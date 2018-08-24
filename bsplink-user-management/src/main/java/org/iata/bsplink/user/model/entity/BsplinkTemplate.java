package org.iata.bsplink.user.model.entity;

import static org.iata.bsplink.user.validation.ValidationMessages.NON_NULL_MESSAGE;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.user.model.view.BsplinkOptionTemplateView;
import org.iata.bsplink.user.model.view.UserTemplateView;

@Data
@Entity
public class BsplinkTemplate implements Serializable {

    private static final long serialVersionUID = 8534620206035476169L;

    @ApiModelProperty(value = "Bsplink Template Name", required = true)
    @Id
    @Size(max = 32)
    @Column(length = 32)
    @JsonView({ BsplinkOptionTemplateView.class, UserTemplateView.class })
    private String id;

    @ApiModelProperty(value = "List of Bsplink Options", required = true)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        uniqueConstraints = @UniqueConstraint(columnNames = {"bsplink_template_id", "options_id"})
    )
    @NotNull(message = NON_NULL_MESSAGE)
    @OrderBy("id")
    @Valid
    @NotNull
    @JsonView(BsplinkOptionTemplateView.class)
    private List<BsplinkOption> options = new ArrayList<>();

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @NotNull
    @JsonView(BsplinkOptionTemplateView.class)
    private List<UserType> userTypes = new ArrayList<>();
}
