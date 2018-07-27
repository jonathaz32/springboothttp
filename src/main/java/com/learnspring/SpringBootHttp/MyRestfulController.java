package com.learnspring.SpringBootHttp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learnspring.SpringBootHttp.repo.Game;
import com.learnspring.SpringBootHttp.repo.GameRepo;
import com.learnspring.SpringBootHttp.repo.Person;
import com.learnspring.SpringBootHttp.repo.PersonRepo;

@RestController
@CrossOrigin("*")
public class MyRestfulController {
	@Autowired
	PersonRepo mPersonRepo;
	
	@Autowired
	GameRepo mGameRepo;
	
	@RequestMapping(method=RequestMethod.GET, value="/testGet")
	public String testGet() {
		return "testGet";
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/users")
	public List<Person> getAllUsers(HttpServletRequest request) {
		Iterable<Person> people= mPersonRepo.findAll();
		if (people != null) {
			return Lists.newArrayList(people);		
		}
		return null;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/user")
	public Person user(@RequestParam(value="userLogin") String userLogin) {
		Person person = mPersonRepo.findByUserLogin(userLogin);
		return person;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/save")
	public void save(@RequestParam(value="userLogin") String userLogin) {
		// make sure String has something
		if (userLogin == null) {
			return;
		}		
		// make sure not saving any duplicate entries
		Iterable<Person> allPeopleSaved = mPersonRepo.findAll();
		Iterator<Person> it = allPeopleSaved.iterator();
		while(it.hasNext()) { 
			Person p = it.next();
			if (p.getUserLogin() == userLogin) {
				return; 
			}
		}
		// save if passes above checks
		Person person = new Person();
		person.setUserLogin(userLogin);		
		mPersonRepo.save(person);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/saveGame")
	public void saveGame(@RequestParam(value="winner") String winner, @RequestParam(value="loser") String loser, @RequestParam(value="winnerScore") int winnerScore, @RequestParam(value="loserScore") int loserScore) throws Exception {
		if (winnerScore < 21 || (winnerScore-loserScore) < 2) {
			throw new Exception("Invalid Scoring Inputs");
		}
		
		Game game = new Game();
		game.setTimestamp(new Date());
		game.setWinner(winner);
		game.setLoser(loser);
		game.setWinnerScore(winnerScore);
		game.setLoserScore(loserScore);
		mGameRepo.save(game);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/getUserGames")
	public List<Game> getUserGames(String userLogin) {
		
		List<Game> games = new ArrayList<Game>();
		
		List<Game> gamesWon = mGameRepo.findByWinner(userLogin);
		List<Game> gamesLossed = mGameRepo.findByLoser(userLogin);
		
		games.addAll(gamesWon);
		games.addAll(gamesLossed);
		
		Collections.sort(games, new Comparator<Game>() {
			@Override
			public int compare(Game g1, Game g2) {
				Long d1 = g1.timestamp.getTime();
				Long d2 = g2.timestamp.getTime();
				return d1.compareTo(d2);
			}
		});
		return games;
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/getLeaderboards")
	public List<Person> getLeaderboards() {
		List<Person> all = mPersonRepo.findAll();
		all.forEach(person -> {
			List<Game> gamesWon = mGameRepo.findByWinner(person.getUserLogin());
			List<Game> gamesLost = mGameRepo.findByLoser(person.getUserLogin());
			double totalGames = (double) (gamesWon.size() + gamesLost.size());
			double gamesWonDB = (double) gamesWon.size();
			person.winPercentage = gamesWonDB / totalGames;		
		});
		
		
		all.forEach(person -> {
			person.rankBonus = 0;
			person.rankRating = 0;
			List<Game> gamesWon = mGameRepo.findByWinner(person.getUserLogin());
			List<Game> gamesLost = mGameRepo.findByLoser(person.getUserLogin());

			gamesWon.forEach(game -> {
				String loser = game.getLoser();
				Person opponentLoser = mPersonRepo.findByUserLogin(loser);
				person.rankBonus += opponentLoser.winPercentage;
			});
			double totalGames = (double) (gamesWon.size() + gamesLost.size());
			double gamesWonPlusRankBonus = (double) gamesWon.size() + person.rankBonus;
			person.rankRating = gamesWonPlusRankBonus / totalGames;
		});
		
		
		return all;
	}
	
	
}
