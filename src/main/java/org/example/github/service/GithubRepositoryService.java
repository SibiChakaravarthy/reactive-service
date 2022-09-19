package org.example.github.service;


import org.example.github.model.entity.github.Branch;
import org.example.github.model.entity.github.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class GithubRepositoryService {

    private static final String GITHUB_BASE_PATH = "https://api.github.com";
    private static final String GET_USER_REPOS = "/users/{username}/repos";
    private static final String GET_BRANCHES = "/repos/{owner}/{repo}/branches";

    private final WebClient client = WebClient.create(GITHUB_BASE_PATH);

    public Flux<org.example.github.model.domain.Repository> getRepositories(String userName) {
        System.out.println("Inside get Repository");
        return getUserRepositories(userName)
                .filterWhen(repository -> Flux.just(!repository.isFork()))
                .flatMap(repository -> getBranches(repository.getOwner().getLogin(), repository.getName())
                        .flatMap(branch -> {
                            repository.addBranch(branch);
                            return Flux.just(repository.toDomain());
                        }));
    }

    private Flux<Repository> getUserRepositories(String repositoryName) {
        return client.get()
                .uri(GET_USER_REPOS, repositoryName)
                .retrieve()
                .bodyToFlux(Repository.class);
    }

    private Flux<Branch> getBranches(String user, String repository) {
        return client.get()
                .uri(GET_BRANCHES, user, repository)
                .retrieve()
                .bodyToFlux(Branch.class);
    }


}
