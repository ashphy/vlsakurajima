package com.appspot.vlsakurajima.tweet;

import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MessageBuilderTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetMessageFromEruptInfo() {
	}

	@Test
	public void testGetShotURL() {
		String shortURL = MessageBuilder.getShotURL("http://ashphy.com/");
		assertThat(shortURL, is("http://bit.ly/cmVEVO"));
	}

	@Test
	public void testSaveMessage() {
	}

	@Test
	public void testDeleteMessage() {
	}

	@Test
	public void testBuildMessageFromMentions() {
	}

	@Test
	public void testGetAllMessage() {
	}

	@Test
	public void testGetAllMessageOrderByCreated() {
	}

	@Test
	public void testGetMessageWithPublishedCount() {
	}

	@Test
	public void testIncrementPublishCount() {
	}

	@Test
	public void testGetMessageWithRank() {
	}

	@Test
	public void testGetValidateMessage() {
	}

}
