package org.iata.bsplink.refund.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue
    @ApiModelProperty(value = "Identificator")
    private Long id;

    @NonNull
    @ApiModelProperty(value = "Text")
    private String text;

    @NonNull
    @ApiModelProperty(
            value = "Comment insert date and time in UTC ISO format (e.g: 2018-01-01T00:00:00Z)")
    @Column(name = "insert_date_time", nullable = false)
    private Instant insertDateTime;

    @NonNull
    @JsonIgnore
    @ApiModelProperty(value = "Id for Refund")
    @Column(name = "refund_id", nullable = false)
    private Long refundId;

}
