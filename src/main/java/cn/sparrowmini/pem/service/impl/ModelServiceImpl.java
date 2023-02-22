package cn.sparrowmini.pem.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Model;
import cn.sparrowmini.pem.model.ModelAttribute;
import cn.sparrowmini.pem.model.common.ModelPermission;
import cn.sparrowmini.pem.model.relation.SysroleModel;
import cn.sparrowmini.pem.model.relation.UserModel;
import cn.sparrowmini.pem.model.relation.SysroleModel.SysroleModelId;
import cn.sparrowmini.pem.model.relation.UserModel.UserModelId;
import cn.sparrowmini.pem.service.ModelPermissionResponseBody;
import cn.sparrowmini.pem.service.ModelService;
import cn.sparrowmini.pem.service.PermissionRequestBody;
import cn.sparrowmini.pem.service.repository.SysroleModelRepository;
import cn.sparrowmini.pem.service.repository.UserModelRepository;

@Service
public class ModelServiceImpl implements ModelService {

	@Autowired
	private SysroleModelRepository sysroleModelRepository;
	@Autowired
	private UserModelRepository userModelRepository;

	@PersistenceContext
	private EntityManager entityManager;


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

	@Override
	public Page<Model> models(Pageable pageable, Model model) {
//		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING);
//		return modelRepository.findAll(Example.of(model, matcher), pageable);

		Set<EntityType<?>> entityTypes = this.entityManager.getMetamodel().getEntities();
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
	public ModelPermissionResponseBody permissions(String modelId) {
		ModelPermissionResponseBody permission = new ModelPermissionResponseBody();
		permission.getSysroles()
				.addAll(this.sysroleModelRepository.findByIdModelId(modelId, Pageable.unpaged()).getContent());
		permission.getUsernames()
				.addAll(this.userModelRepository.findByIdModelId(modelId, Pageable.unpaged()).getContent());
		return permission;

	}

}
