package com.huayu.ratelimit;

import com.huayu.ratelimit.exception.RateLimitException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringRatelimitStarterApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Test
	public void testRateLimitAnnotation() throws Exception {
		// 假设有一个控制器方法使用了@RateLimit注解
		// 例如：@RateLimit(maxCount = 5, timeRange = 60)
		// 这里我们尝试发送6个请求，应该有一个请求会触发限流异常
		for (int i = 0; i <= 5; i++) {
			try {
				mockMvc.perform(MockMvcRequestBuilders.get("/hi"))
						.andExpect(status().isOk());
			} catch (Exception e) {
				// 检查是否是RateLimitException
				if (e instanceof RateLimitException) {
					// 如果第6个请求抛出RateLimitException，则测试通过
					return;
				}
				throw e;
			}
			Thread.sleep(100);
		}
		// 如果没有抛出RateLimitException，则测试失败
		throw new AssertionError("RateLimitException not thrown");
	}

}
