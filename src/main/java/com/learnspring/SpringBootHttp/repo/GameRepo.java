package com.learnspring.SpringBootHttp.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface GameRepo extends CrudRepository<Game, Long> {
	List<Game> findByWinner(String userLogin);
	List<Game> findByLoser(String userLogin);

}
