package org.example.github.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.github.model.domain.Repository;
import org.example.github.service.GithubRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Tag(name = "Repository API", description = "APIs to get repository resources")
@RestController
@RequestMapping("api/v1/repository")
public class RepositoryController extends BaseController {

    private final GithubRepositoryService githubRepositoryService;

    @Autowired
    public RepositoryController(final GithubRepositoryService repositoryService) {
        this.githubRepositoryService = repositoryService;
    }

    @Operation(summary = "List the repositories owned by the user")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Success"))
    @GetMapping(path = "/{userName}")
    public Flux<Repository> getRepositoryInformation(@RequestHeader(name = "Accept", required = false) String responseContentType,
                                                     @Parameter(name = "userName", description = "user name to fetch repositories")
                                                     @PathVariable("userName") String userName) {
        throwIfContentTypeNotSupported(responseContentType);
        return githubRepositoryService.getRepositories(userName);
    }

}
