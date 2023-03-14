package com.example.demo.app;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Task;
import com.example.demo.service.TaskService;

import jakarta.validation.Valid;


@Controller
@RequestMapping
public class TaskController {
	
	private final TaskService taskService;
	
	@Autowired
	public TaskController(TaskService taskService) {
		this.taskService = taskService;
	}
	
	@GetMapping
	public String task(TaskForm taskForm, Model model) {
		
		//新規登録か更新かを判断する
		taskForm.setIsnewTask(true);
		
        //Taskリストを取得
		List<Task> list = taskService.findAll();
		
		model.addAttribute("list", list);
		model.addAttribute("title", "タスク一覧");
		return "index";
	}
	
    //新規登録処理	
//   一件タスクデータを取得し、フォーム内に表示

	@PostMapping("/insert")
	public String insert(@Valid @ModelAttribute TaskForm taskForm,
			BindingResult result,
			Model model) {
		
//　　　　　//TaskFormのデータをTaskに格納.冗長化するためprivateメソッドを呼び出す
		Task task = makeTask (taskForm, 0); //Idが0の場合、新規
//		Task task = new Task();
//		task.setUserId(1);
//		task.setTypeId(taskForm.getTypeId());
//		task.setTitle(taskForm.getTitle());
//		task.setDetail(taskForm.getDetail());
//		task.setDeadline(taskForm.getDeadline());
		
//		一件挿入後、リダイレクト処理	
		if(!result.hasErrors()) {
			taskService.insert(task);
			return "redirect:/";
		} else {
			taskForm.setIsnewTask(true);
			model.addAttribute("taskForm", taskForm);
			List<Task> list = taskService.findAll();
			model.addAttribute("list", list);
			model.addAttribute("title", "タスク一覧（バリデーション）");
			return "index";
		}
		
	}
	
//	一件タスクデータを取得し、フォーム内に表示
	@GetMapping("/{id}")
	public String showUpdate(
			TaskForm taskForm,
			@PathVariable int id,
			Model model) {
//		Taskを1件取得し、optionalでラップ
		Optional<Task> taskOpt = taskService.getTask(id);
//		TaskFormへの詰め直し処理
		Optional<TaskForm> taskFormOpt;
	    if (taskOpt.isPresent()) {
	        taskFormOpt = Optional.of(makeTaskForm(taskOpt.get()));
	    } else {
	        taskFormOpt = Optional.empty();
	    }
		
//		TaskFormがNullでなければ、中身を取り出す
		if(taskFormOpt.isPresent()) {
			taskForm = taskFormOpt.get();
		}
		
		model.addAttribute("taskForm", taskForm);
		model.addAttribute("taskId", id);
		model.addAttribute("title", "更新フォーム");
		
		return "index";
		
	}
	
//	タスクIDを取得し、1件のデータ更新
	@PostMapping("/update")
	public String update(@Valid @ModelAttribute TaskForm taskForm,
			BindingResult result,
			@RequestParam("taskId") int taskId,
			Model model,
			RedirectAttributes redirectAttributes) {
		
//		TaskFormのデータをTaskに格納
		Task task = makeTask(taskForm, taskId);
		if(!result.hasErrors()) {
			taskService.update(task);
			redirectAttributes.addFlashAttribute("complete","更新完了");
			return "redirect:/" + taskId;
		} else {
			model.addAttribute("taskForm", taskForm);
			model.addAttribute("title", "タスク一覧");
			return "index";
		}
		
	}
	
//	タスクIdを取得し、一件のデータ削除
	@PostMapping("/delete")
	public String delete(@RequestParam("taskId") int id,
			Model model) {
		taskService.deleteById(id);
		return "redirect:/";
		
	}

	private Task makeTask(TaskForm taskForm, int taskId) {
        Task task = new Task();
        if(taskId != 0) {
        	task.setId(taskId);
        }
        task.setUserId(1);
        task.setTypeId(taskForm.getTypeId());
        task.setTitle(taskForm.getTitle());
        task.setDetail(taskForm.getDetail());
        task.setDeadline(taskForm.getDeadline());
        return task;
    }
	
	private TaskForm makeTaskForm(Task task) {
		TaskForm taskForm = new TaskForm(null, null, null, null, null);
		
		taskForm.setTypeId(task.getTypeId());
		taskForm.setTitle(task.getTitle());
		taskForm.setDetail(task.getDetail());
		taskForm.setDeadline(task.getDeadline());
		taskForm.setIsnewTask(false);
		
		return taskForm;
	}
	

}
