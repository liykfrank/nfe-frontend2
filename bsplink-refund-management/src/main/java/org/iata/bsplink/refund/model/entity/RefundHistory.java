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
@Table(name = "refund_history")
public class RefundHistory {

    @Id
    @GeneratedValue
    @ApiModelProperty(value = "Identificator")
    private Long id;

    @NonNull
    @ApiModelProperty(value = "Action")
    private RefundAction action;

    @NonNull
    @ApiModelProperty(value = "Refund history insert date and time in UTC "
            + "ISO format (e.g: 2018-01-01T00:00:00Z)")
    @Column(name = "insert_date_time", nullable = false)
    private Instant insertDateTime;
  
    @JsonIgnore
    @NonNull
    @ApiModelProperty(value = "Id for Refund")
    @Column(name = "refund_id", nullable = false)
    private Long refundId;

    @ApiModelProperty(value = "File name")
    private String fileName;

}
