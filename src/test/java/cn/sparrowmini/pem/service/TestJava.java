package cn.sparrowmini.pem.service;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.AbstractRulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;

public class TestJava {

	public static void main(String[] args) {
		MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader());
//		Rule weatherRule = ruleFactory.createRule(new FileReader("weather-rule.yml"));

		Rule weatherRule = new MVELRule().name("weather rule").description("if it rains then take an umbrella")
				.when("rain.isTrue()").then("facts.put(\"result\", true);");
		// define facts
		Facts facts = new Facts();
		facts.put("rain", new User());
//		facts.put("result", false);

		// define rules
		Rules rules = new Rules();
		rules.register(weatherRule);

		// fire rules on known facts
		RulesEngine rulesEngine = new DefaultRulesEngine();
		((AbstractRulesEngine) rulesEngine).registerRuleListener(new RuleListener() {
            @Override
            public void beforeExecute(Rule rule, Facts facts) {
                facts.put("facts", facts);
            }

            @Override
            public void onSuccess(Rule rule, Facts facts) {
                facts.remove("facts");
            }

            @Override
            public void onFailure(Rule rule, Facts facts, Exception exception) {
                facts.remove("facts");
            }
        });
		rulesEngine.fire(rules, facts);
//		System.out.println(((Result)facts.get("result")).getResult());
		System.out.println(facts.get("result").toString());
	}

}

