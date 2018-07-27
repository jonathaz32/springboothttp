package com.learnspring.SpringBootHttp;

import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SpringBootHttpApplicationTests {

	
	final static URI uriSave = URI.create("/save?userLogin=newguy");
	final static URI uriLogin = URI.create("/user?userLogin=newguy");

	@Autowired
	private MockMvc mockMvc;
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRegistrationLogin() throws Exception {
		
		RequestBuilder requestBuilderRegister = MockMvcRequestBuilders.get(uriSave);
		MvcResult mvcResultRegister = this.mockMvc.perform(requestBuilderRegister) // ResultActions
										.andReturn();
		String jsonString =  mvcResultRegister.getResponse().getContentAsString();

		RequestBuilder requestBuilderLogin = MockMvcRequestBuilders.get(uriLogin);
		MvcResult mvcResultLogin = this.mockMvc.perform(requestBuilderLogin).andReturn();
		jsonString = mvcResultLogin.getResponse().getContentAsString();
		
		Assert.hasText("newguy");
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testRankingSystem() throws Exception {
		String user1 = "user1";
		String user2 = "user2";
		String user3 = "user3";
		String user4 = "user4";
		RequestBuilder register1 = MockMvcRequestBuilders.get(URI.create("/save?userLogin=" + user1));
		RequestBuilder register2 = MockMvcRequestBuilders.get(URI.create("/save?userLogin=" + user2));
		RequestBuilder register3 = MockMvcRequestBuilders.get(URI.create("/save?userLogin=" + user3));
		RequestBuilder register4 = MockMvcRequestBuilders.get(URI.create("/save?userLogin=" + user4));
		this.mockMvc.perform(register1);
		this.mockMvc.perform(register2);
		this.mockMvc.perform(register3);
		this.mockMvc.perform(register4);
		
		URI uri = URI.create("/saveGame?winner=user1&loser=user2&winnerScore=21&loserScore=10");
		RequestBuilder user1Beats2 = MockMvcRequestBuilders.get(uri);

		uri = URI.create("/saveGame?winner=user1&loser=user3&winnerScore=21&loserScore=10");
		RequestBuilder user1Beats3 = MockMvcRequestBuilders.get(uri);
		uri = URI.create("/saveGame?winner=user1&loser=user4&winnerScore=21&loserScore=10");
		RequestBuilder user1Beats4 = MockMvcRequestBuilders.get(uri);
		
		uri = URI.create("/saveGame?winner=user2&loser=user4&winnerScore=21&loserScore=10");
		RequestBuilder user2Beats4 = MockMvcRequestBuilders.get(uri);
		uri = URI.create("/saveGame?winner=user3&loser=user1&winnerScore=21&loserScore=10");
		RequestBuilder user3Beats1 = MockMvcRequestBuilders.get(uri);
		
		uri = URI.create("/getLeaderboards");
		RequestBuilder getLeaderboards = MockMvcRequestBuilders.get(uri);

		
		this.mockMvc.perform(user1Beats2);
		this.mockMvc.perform(user1Beats3);
		this.mockMvc.perform(user1Beats4);
		
		this.mockMvc.perform(user2Beats4);
		this.mockMvc.perform(user3Beats1);
		
		// user 3 should have higher ranking than user 2
		MvcResult mvcResult = this.mockMvc.perform(getLeaderboards).andReturn();
		String jsonString = mvcResult.getResponse().getContentAsString();
		JSONArray array = new JSONArray(jsonString);
		JSONObject user2Obj = null;
		JSONObject user3Obj = null;
		for (int i = 0; i < array.length(); i++) {
			JSONObject object = (JSONObject) array.get(i);
			if (object.get("userLogin").equals("user2")) {
				user2Obj = object;
			} else if (object.get("userLogin").equals("user3")) {
				user3Obj = object;
			}
		}

		if (user3Obj.getDouble("rankRating") > user2Obj.getDouble("rankRating")) {
			Assert.isTrue(true);
		}
	}
	
	

}
