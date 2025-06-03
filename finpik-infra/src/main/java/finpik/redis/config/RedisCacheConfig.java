package finpik.redis.config;

import java.time.Duration;
import java.util.HashMap;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import static finpik.util.RedisKeyValues.RECOMMENDATION_KEY;
import static finpik.util.Values.FOUR;

@Configuration
@EnableCaching
public class RedisCacheConfig {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        HashMap<String, RedisCacheConfiguration> ttlConfigMap = new HashMap<>();

        ttlConfigMap.put(RECOMMENDATION_KEY, config.entryTtl(Duration.ofHours(FOUR)));

        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config)
                .withInitialCacheConfigurations(ttlConfigMap).build();
    }
}
