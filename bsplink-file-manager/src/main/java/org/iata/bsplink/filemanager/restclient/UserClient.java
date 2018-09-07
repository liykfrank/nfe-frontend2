package org.iata.bsplink.filemanager.restclient;

import org.iata.bsplink.filemanager.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users", url = "${feign.users.url}", decode404 = true)
public interface UserClient {

    @GetMapping("/{id}")
    ResponseEntity<User> findUser(@PathVariable("id") String id);
}