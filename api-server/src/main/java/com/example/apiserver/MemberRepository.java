package com.example.apiserver;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MemberRepository extends PagingAndSortingRepository<Member, Long> {

    Member findByUsername(String username);
}
