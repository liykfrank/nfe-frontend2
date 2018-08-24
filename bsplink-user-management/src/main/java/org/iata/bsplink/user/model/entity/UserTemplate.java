package org.iata.bsplink.user.model.entity;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

import org.iata.bsplink.user.model.view.UserTemplateView;

@Data
@Entity
@JsonView(UserTemplateView.class)
public class UserTemplate implements Serializable {

    private static final long serialVersionUID = 4030651341786929422L;

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne
    private BsplinkTemplate template;

    @ElementCollection
    @NotNull
    @Column(length = 2)
    private List<String> isoCountryCodes = new ArrayList<>();
}
