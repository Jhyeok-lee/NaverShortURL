package org.jaylee.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jaylee.domain.Url;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;

public class UrlRepository implements CrudRepository<Url, String> {
	
	public static final String URLS_KEY = "urls";

	private final HashOperations<String, String, Url> hashOps;

	public UrlRepository(RedisTemplate<String, Url> redisTemplate) {
		this.hashOps = redisTemplate.opsForHash();
	}

	@Override
	public long count() {
		return hashOps.keys(URLS_KEY).size();
	}

	@Override
	public void delete(String originURL) {
		hashOps.delete(URLS_KEY, originURL);
	}

	@Override
	public void delete(Url url) {
		hashOps.delete(URLS_KEY,  url.getOriginURL());
	}

	@Override
	public void delete(Iterable<? extends Url> urls) {
		for (Url u : urls) {
			delete(u);
		}
	}

	@Override
	public void deleteAll() {
		Set<String> originURLs = hashOps.keys(URLS_KEY);
		for (String originURL : originURLs) {
			delete(originURL);
		}

	}

	@Override
	public boolean exists(String originURL) {
		return hashOps.hasKey(URLS_KEY,  originURL);
	}

	@Override
	public Iterable<Url> findAll() {
		return hashOps.values(URLS_KEY);
	}

	@Override
	public Iterable<Url> findAll(Iterable<String> originURLs) {
		return hashOps.multiGet(URLS_KEY, convertIterableToList(originURLs));
	}

	@Override
	public Url findOne(String originURL) {
		return hashOps.get(URLS_KEY, originURL);
	}

	@Override
	public <S extends Url> S save(S url) {
		hashOps.put(URLS_KEY, url.getOriginURL(), url);

		return url;
	}

	@Override
	public <S extends Url> Iterable<S> save(Iterable<S> urls) {
		List<S> result = new ArrayList<S>();

		for(S entity : urls) {
			save(entity);
			result.add(entity);
		}

		return result;
	}

	private <T> List<T> convertIterableToList(Iterable<T> iterable) {
        List<T> list = new ArrayList<>();
        for (T object : iterable) {
            list.add(object);
        }
        return list;
    }
}
