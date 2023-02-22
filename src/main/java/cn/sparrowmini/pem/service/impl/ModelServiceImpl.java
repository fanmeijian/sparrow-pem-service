package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Model;
import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.SysroleModel;
import cn.sparrowmini.pem.model.SysroleModel.SysroleModelId;
import cn.sparrowmini.pem.model.UserModel;
import cn.sparrowmini.pem.model.UserModel.UserModelId;
import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.service.ModelPermissionResponseBody;
import cn.sparrowmini.pem.service.ModelService;
import cn.sparrowmini.pem.service.PermissionRequestBody;
import cn.sparrowmini.pem.service.repository.ModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.ModelRepository;
import cn.sparrowmini.pem.service.repository.PermissionTokenRepository;
import cn.sparrowmini.pem.service.repository.SysroleModelRepository;
import cn.sparrowmini.pem.service.repository.UserModelRepository;

@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	ModelRepository modelRepository;
	@Autowired
	private SysroleModelRepository sysroleModelRepository;
	@Autowired
	private UserModelRepository userModelRepository;

	// @Autowired UserModelPermissionRepository userModelPermissionRepository;
	// @Autowired SysroleModelPermissionRepository sysroleModelPermissionRepository;
	// @Autowired OrganizationModelPermissionRepository
	// organizationModelPermissionRepository;
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	ModelAttributeRepository modelAttributeRepository;
	@Autowired
	PermissionTokenRepository permissionTokenRepository;

	@Override
	public Model getModel(String modelId) {
		return modelRepository.findById(modelId).orElse(null);
	}

//	@Override
//	@Transactional
//	public void addPermissions(ModelPermission modelPermission) {
//		modelPermission.getModelName().forEach(modelName -> {
//			this.addPermission(modelName, modelPermission.getPermissionToken());
//		});
//	}

	@Override
	@Transactional
	public void removePermission(String modelId, PermissionRequestBody body) {
		if (body.getSysroles() != null) {
			body.getSysroles().forEach(f -> {
				this.sysroleModelRepository.deleteById(
						new SysroleModelId(modelId, f.getSysroleId(), f.getPermissionType(), f.getPermission()));
			});
		}

		if (body.getUsernames() != null) {
			body.getUsernames().forEach(f -> {
				this.userModelRepository.deleteById(
						new UserModelId(modelId, f.getUsername(), f.getPermissionType(), f.getPermission()));
			});
		}
	}

	@Transactional
	@Override
	public void init() {
		Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();
		entityTypes.forEach(e -> {
			modelRepository.save(new Model(e.getJavaType().getName(), true));
			List<String> attributes = new ArrayList<String>();
			e.getAttributes().forEach(a -> {
				attributes.add(a.getName());

				modelAttributeRepository
						.save(new ModelAttribute(e.getJavaType().getName(), a.getName(), a.getJavaType().getName()));
			});

		});
	}

	@Override
	public Page<Model> models(Pageable pageable, Model model) {
//		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
//		return modelRepository.findAll(Example.of(model, matcher), pageable);

		Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();
		List<Model> models = new ArrayList<Model>();
		entityTypes.forEach(e -> {
			if (e.getJavaType().isAnnotationPresent(ModelPermission.class)) {
				Model model1 = new Model(e.getJavaType().getName());

				List<ModelAttribute> attributes = new ArrayList<ModelAttribute>();
				e.getAttributes().forEach(a -> {
					ModelAttribute modelAttribute = new ModelAttribute(e.getJavaType().getName(), a.getName(),
							a.getJavaType().getName());
					attributes.add(modelAttribute);
				});
				model1.setModelAttributes(attributes);
				models.add(model1);
			}

		});
		return new PageImpl<>(models);
	}

	@Override
	public Model create(Model model) {
		return modelRepository.save(model);
	}

	@Override
	public Model update(String modelId, Map<String, Object> map) {
		Model source = modelRepository.getById(modelId);
		PatchUpdateHelper.merge(source, map);
		return modelRepository.save(source);
	}

	@Override
	public void delete(List<String> ids) {
		modelRepository.deleteAllByIdInBatch(ids);
	}

	@Override
	public void addPermission(String modelId, PermissionRequestBody body) {
		if (body.getSysroles() != null) {
			body.getSysroles().forEach(f -> {
				this.sysroleModelRepository
						.save(new SysroleModel(modelId, f.getSysroleId(), f.getPermissionType(), f.getPermission()));
			});
		}

		if (body.getUsernames() != null) {
			body.getUsernames().forEach(f -> {
				this.userModelRepository
						.save(new UserModel(modelId, f.getUsername(), f.getPermissionType(), f.getPermission()));
			});
		}

	}

	@Override
	public List<Model> getAllEntities() {
		Set<EntityType<?>> entityTypes = entityManager.getMetamodel().getEntities();
		List<Model> models = new ArrayList<Model>();
		entityTypes.forEach(e -> {
			Model model = new Model(e.getJavaType().getName());

			List<ModelAttribute> attributes = new ArrayList<ModelAttribute>();
			e.getAttributes().forEach(a -> {
				ModelAttribute modelAttribute = new ModelAttribute(e.getJavaType().getName(), a.getName(),
						a.getJavaType().getName());
				attributes.add(modelAttribute);
			});
			model.setModelAttributes(attributes);
			models.add(model);
		});
		return models;
	}

	@Override
	public ModelPermissionResponseBody permissions(String modelId) {
		ModelPermissionResponseBody permission = new ModelPermissionResponseBody();
		permission.getSysroles()
				.addAll(this.sysroleModelRepository.findByIdModelId(modelId, Pageable.unpaged()).getContent());
		permission.getUsernames()
				.addAll(this.userModelRepository.findByIdModelId(modelId, Pageable.unpaged()).getContent());
		return permission;

	}

}
