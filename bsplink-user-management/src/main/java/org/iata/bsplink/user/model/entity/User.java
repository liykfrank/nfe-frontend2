package org.iata.bsplink.user.model.entity;

import static org.iata.bsplink.user.validation.ValidationMessages.INCORRECT_SIZE;
import static org.iata.bsplink.user.validation.ValidationMessages.NON_NULL_MESSAGE;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

import org.iata.bsplink.user.model.view.UserTemplateView;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@JsonView(UserTemplateView.class)
public class User implements Serializable {

    private static final long serialVersionUID = -4263626642022961775L;

    @Id
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(max = 36, message = INCORRECT_SIZE + "max 36")
    private String id;

    @NotNull(message = NON_NULL_MESSAGE)
    @Size(max = 255, message = INCORRECT_SIZE + "max 255")
    private String username;

    @CreatedDate
    @Column(name = "register_date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime registerDate;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "user_type")
    @NotNull(message = NON_NULL_MESSAGE)
    private UserType userType;

    @Column(name = "user_code")
    @NotNull(message = NON_NULL_MESSAGE)
    @Size(max = 10, message = INCORRECT_SIZE + "max 10")
    private String userCode;

    @Size(max = 49, message = INCORRECT_SIZE + "max 49")
    private String name;
    
    @Size(max = 100, message = INCORRECT_SIZE + "max 100")
    private String lastName;

    @Size(max = 15, message = INCORRECT_SIZE + "max 15")
    private String telephone;

    @Size(max = 30, message = INCORRECT_SIZE + "max 30")
    private String organization;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime lastModifiedDate;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;


    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    private List<UserTemplate> templates;
}
