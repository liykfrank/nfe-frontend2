package org.iata.bsplink.user.pojo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import lombok.Data;
import org.iata.bsplink.user.model.entity.UserType;

@Data
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(content = Include.NON_NULL)
public class UserRequest {

    private String id;
    private String username;
    private LocalDateTime expiryDate;
    private UserType userType;
    private String userCode;
    private String name;
    private String email;
    private String telephone;
    private String organization;

}