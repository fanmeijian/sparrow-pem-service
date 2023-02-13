package cn.sparrowmini.pem.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.ModelAttribute.ModelAttributePK;
import cn.sparrowmini.pem.model.token.PermissionToken;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

public interface ModelAttributeService extends ModelAttributeRestService{

	public Page<ModelAttribute> all(@Nullable Pageable pageable, @Nullable ModelAttribute modelAttribute);

	public ModelAttribute create(@RequestBody ModelAttribute modelAttribute);

	public ModelAttribute get(
			@Parameter(content = @Content(schema = @Schema(implementation = String.class)), example = "modelName_attributeName") @PathVariable("attributeId") ModelAttributePK attributePK);

	public ModelAttribute update(
			@Parameter(content = @Content(schema = @Schema(implementation = String.class)), example = "modelName_attributeName") @PathVariable("attributeId") ModelAttributePK attributePK,
			@RequestBody Map<String, Object> map);

	public void addPermission(
			@Parameter(content = @Content(schema = @Schema(implementation = String.class)), example = "modelName_attributeName") @PathVariable("attributeId") ModelAttributePK attributePK,
			@RequestBody PermissionToken permissionToken);

	public void removePermission(
			@Parameter(content = @Content(schema = @Schema(implementation = String.class)), example = "modelName_attributeName") @PathVariable("attributeId") ModelAttributePK attributePK);

	public void delete(@RequestBody List<ModelAttributePK> ids);
}
