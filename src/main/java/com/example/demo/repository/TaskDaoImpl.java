package com.example.demo.repository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Task;
import com.example.demo.entity.TaskType;

@Repository
public class TaskDaoImpl implements TaskDao {
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public TaskDaoImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Override
	public List<Task> findAll(){
		String sql = "SELECT task.id, user_id, type_id, title, detail, deadline, "
				+ "type, comment FROM task "
				+ "INNER JOIN task_type ON task.type_id = task_type.id";
        //タスク一覧をMapのListで取得
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		
		//return用の空のリストを用意	
		List<Task> list = new ArrayList<Task>();
		
		for(Map<String,Object> result : resultList) {
			Task task = new Task();
			task.setId((int)result.get("id"));
			task.setUserId((int)result.get("user_id"));
			task.setTypeId((int)result.get("type_id"));
			task.setTitle((String)result.get("title"));
			task.setDetail((String)result.get("detail"));
			task.setDeadline((LocalDateTime) result.get("deadline"));

			TaskType type = new TaskType();
			type.setId((int)result.get("type_id"));
			type.setType((String)result.get("type"));
			type.setComment((String)result.get("comment"));

			//TaskにTaskTypeをセット.テーブルの結合がオブジェクト指向上で表現
			task.setTaskType(type);
			list.add(task);
		}
		return list;	
	}
    //idに紐ついて一件のデータを返す処理	
	@Override
	public Optional<Task> findById(int id) {
		String sql = "SELECT task.id, user_id, type_id, title, detail, deadline, "
				+ "type, comment FROM task "
				+ "INNER JOIN task_type ON task.type_id = task_type.id "
				+ "WHERE task.id = ? ";
		
		//タスクを一件取得
		Map<String, Object> result = jdbcTemplate.queryForMap(sql, id);
		
		Task task = new Task();
		task.setId((int)result.get("id"));
		task.setUserId((int)result.get("user_id"));
		task.setTypeId((int)result.get("type_id"));
		task.setTitle((String)result.get("title"));
		task.setDetail((String)result.get("detail"));
		task.setDeadline((LocalDateTime) result.get("deadline"));

		TaskType type = new TaskType();
		type.setId((int)result.get("type_id"));
		type.setType((String)result.get("type"));
		type.setComment((String)result.get("comment"));

		//TaskにTaskTypeをセット.テーブルの結合がオブジェクト指向上で表現
		task.setTaskType(type);
		
        //taskをオプショナルでラップする
		Optional<Task> taskOpt = Optional.ofNullable(task);
		
		return taskOpt;
		
		
	}
	
	
	@Override
	public void insert(Task task) {
		jdbcTemplate.update("INSERT INTO task(user_id, type_id, title, detail, deadline) VALUES(?, ?, ?, ?,?)",
				 task.getUserId(), task.getTypeId(), task.getTitle(), task.getDetail(), task.getDeadline() );
	}

	@Override
	public int update(Task task) {
		return jdbcTemplate.update("UPDATE task SET type_id = ?, title = ?, detail = ?,deadline = ? WHERE id = ?",
				task.getTypeId(), task.getTitle(), task.getDetail(), task.getDeadline(), task.getId() );
	}

	@Override
	public int deleteById(int id) {
		return jdbcTemplate.update("DELETE FROM task WHERE id = ?", id);
	}
//	

}
