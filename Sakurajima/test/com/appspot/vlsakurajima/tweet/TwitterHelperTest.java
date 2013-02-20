package com.appspot.vlsakurajima.tweet;

import static org.junit.Assert.*;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;

public class TwitterHelperTest {

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		helper.setUp();
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testSend() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSendDM() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNewMentions() {
		ResponseList<Status> mentions = TwitterHelper.getNewMentions();
		assertThat(mentions, notNullValue());
		assertThat(0, is(not(mentions.size())));
	}

	@Test
	public void testGetTwitter() throws Exception {
		Twitter twitter = TwitterHelper.getTwitter();
		assertThat(twitter, notNullValue());
		assertThat(twitter.getOAuthAccessToken(), notNullValue());
		assertThat(twitter.verifyCredentials(), notNullValue());
	}

}
