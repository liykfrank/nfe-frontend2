package org.iata.bsplink.agencymemo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@ApiModel(description = "Comment")
public class CommentRequest {
    
    @NonNull
    private Long acdmId;
    @NonNull
    private String text;
    
}
