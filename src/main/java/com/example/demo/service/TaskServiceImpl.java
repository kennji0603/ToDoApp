package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskDao;


@Service
public class TaskServiceImpl implements TaskService {
	
	private final TaskDao dao;
	
	public TaskServiceImpl(TaskDao dao) {
		this.dao = dao;
	}

	@Override
	public List<Task> findAll() {
		return dao.findAll();
	}

	@Override
	public Optional<Task> getTask(int id) {
		try {
		    return dao.findById(id);
		}catch (EmptyResultDataAccessException e) {
			throw new TaskNotFoundException("指定されたタスクがありません");
		}
		                                          
		                                          
	}

	@Override
	public void insert(Task task) {
		dao.insert(task);

	}

	@Override
	public void update(Task task) {
       //Taskを更新。Idがなければ例外発生
		if(dao.update(task)==0) {
			throw new TaskNotFoundException("更新するタスクがありません");
		}
		
	}

	@Override
	public void deleteById(int id) {
		//Taskを更新。Idがなければ例外発生
		if(dao.deleteById(id)==0) {
			throw new TaskNotFoundException("削除するタスクがありません");
		}
	}

}
