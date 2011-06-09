package com.k99k.khunter;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskManagerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String webRoot = "f:/works/workspace_keel/KHunter/WebContent/WEB-INF/";
		String jsonFilePath = webRoot+"kconfig.json";
		String classPath = webRoot+"classes/";
		ActionManager.init(jsonFilePath, classPath);
		TaskManager.init(jsonFilePath, classPath);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	};

	@Test
	public final void testRemoveFromTaskMap() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddExeTask() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddScheduledTask() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddRateTask() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testCancelTask() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testAddSingleTask() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testMakeNewTask() {
		fail("Not yet implemented"); // TODO
	}

}
