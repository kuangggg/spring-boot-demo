

package com.example.sentienl.demos.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@RestController
public class DemoController {

    @GetMapping("/test")
    public String test() {
        return handle();
    }

    @SentinelResource(value = "res_test",
            blockHandler = "blockHandler",
            fallback = "fallback"
    )
    public String handle() {
        double random = Math.random();
        System.out.println(1 / 0);
        return "biz success";
    }

    public String blockHandler() {
        return "block handle";
    }

    public String fallback() {
        return "fallback";
    }


    /**
     * 流控测试
     *
     * @return
     */
    @GetMapping("/test/flow")
    public String testFlow() {
        String resName = "res_flow";

        Entry entry = null;
        try {
            entry = SphU.entry(resName);

            return "flow test success ret";
        } catch (BlockException e) {
            return "请求资源被限流";
        } catch (Exception e) {
            return "请求资源降级";
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @PostConstruct
    private static void initFlowRules() {
        List<FlowRule> rules = new ArrayList<>();

        FlowRule rule = new FlowRule();
        rule.setResource("res_flow");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setLimitApp("default");
        rule.setCount(1);
        rules.add(rule);

        FlowRuleManager.loadRules(rules);
    }

    /**
     * 降级测试
     *
     * @return
     */
    @GetMapping("/test/degrade")
    public String testDegrade() {
        String resName = "res_degrade";

        Entry entry = null;
        try {
            entry = SphU.entry(resName);

            if (System.currentTimeMillis() % 10 > 5) {
                throw new RuntimeException("somthing is wrong");
            }

            return "degrade test success ret";
        } catch (BlockException e) {
            return "请求资源被限流";
        } catch (Exception e) {
            return "请求资源降级";
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @PostConstruct
    private static void initDegradeRules() {
        List<DegradeRule> rules = new ArrayList<>();

        DegradeRule rule = new DegradeRule();
        rule.setResource("res_degrade");
        // 异常次数/异常比例/慢调用比例
//        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        // 60s 内统计，三次出现一次异常
        rule.setCount(1);
        rule.setMinRequestAmount(3);
        rule.setStatIntervalMs(60 * 1000);

        // 10s 后解除熔断，如果解除后第一次就发生异常，立刻熔断
        rule.setTimeWindow(10);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
    }


}
