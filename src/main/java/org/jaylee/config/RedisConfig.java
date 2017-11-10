package org.jaylee.config;

import org.jaylee.domain.Url;
import org.jaylee.repository.UrlRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Bean
	public UrlRepository repository(RedisTemplate<String, Url> redisTemplate) {
		return new UrlRepository(redisTemplate);
	}

	@Bean
	public RedisTemplate<String, Url> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Url> template = new RedisTemplate<String, Url>();

		template.setConnectionFactory(redisConnectionFactory);

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisSerializer<Url> personSerializer = new Jackson2JsonRedisSerializer<>(Url.class);

		template.setKeySerializer(stringSerializer);
		template.setValueSerializer(personSerializer);
		template.setHashKeySerializer(stringSerializer);
		template.setHashValueSerializer(personSerializer);

		return template;
	}
}
