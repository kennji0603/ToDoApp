package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.example.demo.entity.Task;


//Spring TestContext Frameworkというテストできる機能を利用する
@SpringJUnitConfig
//テストのたびにテスト起動
@SpringBootTest


@DisplayName("TestServiceImplの結合テスト")
public class TaskServiceImplTest {
	
	@Autowired
	private TaskService taskService;
	
	@Test
	@DisplayName("タスクが取得できない場合のテスト")
	void testGetTaskFormReturnNull() {
		
		try {
			Optional<Task> task = taskService.getTask(0);
		} catch (TaskNotFoundException e) {
			assertEquals(e.getMessage(),"指定されたタスクがありません");
			
		}
	}
	
	@Test
	@DisplayName("全件検索のテスト")
	void testFindAllCheckCount() {
//		全件取得
		List<Task> list = taskService.findAll();
//		Taskテーブルに入っている2件が取得できているか確認(初期データとして2件登録するため）
		assertEquals(2, list.size());
	}
	
	@Test
	@DisplayName("1件のタスクが取得できた場合のテスト")
	void testGetTaskFormReturnOne() {
//		idが1のTaskを取得
		Optional<Task> taskOpt = taskService.getTask(8);
//		取得できたことを確認
		assertEquals("sample", taskOpt.get().getTitle());
		
	}
	
	
	
	
}

