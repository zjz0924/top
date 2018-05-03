package cn.wow.rest.service;

import java.util.List;
import java.util.Map;
import javax.inject.Named;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import cn.wow.common.domain.Task;
import cn.wow.common.service.TaskService;
import cn.wow.common.utils.pagination.PageMap;
import cn.wow.rest.exception.ApiError;
import cn.wow.rest.exception.ApiResponse;
import cn.wow.rest.exception.Exceptions.BadRequestException;
import cn.wow.rest.exception.Exceptions.NotFoundException;
import cn.wow.rest.model.Student;

@Path("student")
@Named
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StudentService {
	
	@Autowired
	private TaskService taskService;

	@GET
	@Path("{name}")
	public Student getStudentName(@PathParam("name") final String name, @QueryParam("sex") String sex) {
		Map<String, Object> map = new PageMap(0, 10);
		List<Task> taskList = taskService.selectAllList(map);

		if ("Jack".equals(name)) {
			Student stu = new Student();
			stu.setName("Jack");
			stu.setSex("Male");
			stu.setAge(20);
			return stu;
		} else {
			throw new NotFoundException(ApiError.ERROR, "Given student name [" + name + "] not found.");
		}
	}

	@DELETE
	@Path("{name}")
	public ApiResponse delete(@PathParam("name") final String name) {
		if ("Jack".equals(name)) {
			ApiResponse response = new ApiResponse();
			response.setMessage("delete student：" + name);
			response.setDeleted(1);
			return response;
		} else {
			throw new NotFoundException(ApiError.ERROR, "Given student name [" + name + "] not found.");
		}
	}

	@PUT
	public ApiResponse batchUpdate(@Valid List<Student> stuList) {

		if (stuList.isEmpty()) {
			throw new BadRequestException(ApiError.ERROR, "The iput data can not empty.");
		}

		ApiResponse response = new ApiResponse();
		response.setCreated(stuList.size());
		response.setUpdated(0);

		return response;
	}

}
