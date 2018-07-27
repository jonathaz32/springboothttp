package com.learnspring.SpringBootHttp.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PersonRepo extends CrudRepository<Person, Long> {
	Person findByUserLogin(@Param("userLogin") String userLogin);
	List<Person> findAll();
}