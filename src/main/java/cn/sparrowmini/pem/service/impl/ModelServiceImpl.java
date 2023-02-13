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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Model;
import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.token.PermissionToken;
import cn.sparrowmini.pem.model.token.SparrowPermissionToken;
import cn.sparrowmini.pem.service.ModelService;
import cn.sparrowmini.pem.service.repository.ModelAttributeRepository;
import cn.sparrowmini.pem.service.repository.ModelRepository;
import cn.sparrowmini.pem.service.repository.PermissionTokenRepository;

@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	ModelRepository modelRepository;

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
	public void removePermission(String modelId) {
		Model model = modelRepository.getById(modelId);
		model.setSparrowPermissionToken(null);
		modelRepository.save(model);
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
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
		return modelRepository.findAll(Example.of(model, matcher), pageable);
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
	public void addPermission(String modelId, PermissionToken permissionToken) {
		Model model = modelRepository.findById(modelId).get();
		SparrowPermissionToken sparrowPermissionToken = model.getSparrowPermissionToken();
		if (sparrowPermissionToken == null) {
			sparrowPermissionToken = new SparrowPermissionToken(permissionToken);
			model.setSparrowPermissionToken(permissionTokenRepository.save(sparrowPermissionToken));
			modelRepository.save(model);
		} else {
			sparrowPermissionToken.setPermissionToken(permissionToken);
			permissionTokenRepository.save(sparrowPermissionToken);
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

}
