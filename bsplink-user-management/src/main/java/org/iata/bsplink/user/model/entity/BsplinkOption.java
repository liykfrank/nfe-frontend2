package org.iata.bsplink.user.model.entity;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.user.model.view.BsplinkOptionTemplateView;

@Data
@Entity
public class BsplinkOption  implements Serializable {

    private static final long serialVersionUID = 2515486701058152262L;

    @Id
    @NotNull
    @Size(max = 32)
    @Column(length = 32)
    @JsonView(BsplinkOptionTemplateView.class)
    private String id;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @NotNull
    private List<UserType> userTypes = new ArrayList<>();
}
