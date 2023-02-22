package cn.sparrowmini.pem.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import cn.sparrowmini.pem.model.Group;
import cn.sparrowmini.pem.model.constant.GroupTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "pemgroup", description = "权限组服务")
@RequestMapping("/pemgroups")
public interface GroupService {
	
	@Operation(summary = "群组列表")
	@ResponseBody
	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<Group> list(@Nullable Pageable pageable, @Nullable Group group);

	@Operation(summary = "新建群组")
	@ResponseBody
	@PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.CREATED)
	public String create(Group group);

	@Operation(summary = "更新群组")
	@ResponseBody
	@PatchMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = Group.class)))
	public void update(@PathVariable String groupId,@RequestBody Map<String, Object> map);

	@Operation(summary = "删除群组")
	@ResponseBody
	@DeleteMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable String groupId);

	@Operation(summary = "群组详情")
	@ResponseBody
	@GetMapping(value = "/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Group get(@PathVariable String groupId);
	
	@Operation(summary = "添加群组成员")
	@ResponseBody
	@PostMapping(value = "/{groupId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void addMembers(@PathVariable String groupId, GroupTypeEnum type, @RequestBody String[] ids);
	
	@Operation(summary = "删除群组成员")
	@ResponseBody
	@DeleteMapping(value = "/{groupId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void removeMembers(@PathVariable String groupId, GroupTypeEnum type, @RequestBody String[] ids);
	
	@Operation(summary = "群组成员列表")
	@ResponseBody
	@GetMapping(value = "/{groupId}/members", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<?> members(@PathVariable String groupId, GroupTypeEnum type, Pageable pageable);

}
