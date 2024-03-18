package cn.sparrowmini.pem.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.sparrowmini.pem.model.Rule;
import cn.sparrowmini.pem.service.RuleService;
import cn.sparrowmini.pem.service.repository.RuleRepository;

@Service
public class RuleServiceImpl implements RuleService {
	@Autowired
	private RuleRepository ruleRepository;

	@Override
	public List<String> create(List<Rule> rules) {
		this.ruleRepository.saveAll(rules);
		return rules.stream().map(m->m.getId()).collect(Collectors.toList());
	}

}
