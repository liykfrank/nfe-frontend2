package @packageName@.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.extern.apachecommons.CommonsLog;

import @packageName@.pojo.ResourceSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/resources")
@CrossOrigin(origins = "*", maxAge = 3600)
@CommonsLog
public class ResourceController {

    @Autowired
    private ResourceService service;

    /**
     * Exposes a REST endpoint that returns resources matching the provided search criteria and pagination.
     *
     * @return the list of resources
     * @see BaseResponse
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation(value = "List of current resources")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchCriteria",
                value = "Search criteria", required = false, 
                    dataType = "ResourceSearchCriteria", paramType = "query"),
            @ApiImplicitParam(name = "pageable", value = "Page number", required = false,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "Sort by one or more fields", required = false, 
                    dataType = "string", paramType = "query")})
    public List<Resource> getResourceList(ResourceSearchCriteria searchCriteria, Pageable pageable,
            Sort sort) {

        log.info("received request for getting resource list with search criteria: {}, page: {}, sort: {}", searchCriteria, pageable, sort);

        Page<Resource> response = service.find(searchCriteria, pageable, sort);

        log.info("responding with response: {}", response);

        return response;
    }

    /**
     * Exposes a REST endpoint that returns the resource for the provided id.
     *
     * @param resourceId the resource id
     * 
     * @return the resource for the provided id
     */
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiImplicitParam(name = "id", value = "The ID of the resource",
            required = true, type = "int")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Resource found"),
            @ApiResponse(code = 400, message = "Invalid resource ID"),
            @ApiResponse(code = 404, message = "Resource not found")})
    public Resource getResource(@NotBlank @PathVariable("id") String resourceId) {

        log.info("received request for getting resource with id: {}", resourceId);

        Resource response = service.getResource(resourceId);

        log.info("responding with response: {}", response);

        return response;
    }

    /**
     * Exposes a REST endpoint that creates a new resource.
     *
     * @param request details for creating the resource
     * 
     * @return the created resource
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse createResource(@Valid @RequestBody BaseRequest request) {

        log.info("received request for creating resource: {}", request);

        BaseResponse response = service.createResource(request);

        log.info("responding with response: {}", response);

        return response;
    }

    /**
     * Exposes a REST endpoint that updates the resource for the provided id.
     *
     * @param resourceId the resource id
     * @param request details for update the resource
     * 
     * @return the updated resource
     */
    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public BaseResponse updateResource(@Valid @RequestBody BaseRequest request,
            @NotBlank @PathVariable("id") String resourceId) {

        log.info("received request for updating resource with id: {}", request);

        BaseResponse response = service.updateResource(resourceId, request);

        log.info("responding with response: {}", response);

        return response;
    }

    /**
     * Exposes a REST endpoint that deletes the resource for the provided id.
     *
     * @param resourceId the resource id
     */
    @DeleteMapping(path = "/{id}")
    public void deleteResource(@NotBlank @PathVariable("id") String resourceId) {

        log.info("received request for deleting resource with id: {}", resourceId);

        service.deleteResource(resourceId);

        log.info("resource successfully deleted");
    }
}
