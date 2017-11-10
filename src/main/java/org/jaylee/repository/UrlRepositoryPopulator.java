package org.jaylee.repository;

import java.util.ArrayList;
import java.util.Collection;

import org.jaylee.domain.Url;
import org.jaylee.utils.AsyncNaverShortenURL;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UrlRepositoryPopulator implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;
    private ApplicationContext applicationContext;

    public UrlRepositoryPopulator()
    {
    	ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("urls.json");
    }

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		 this.applicationContext = applicationContext;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 if (event.getApplicationContext().equals(applicationContext)) {
	            CrudRepository personRepository =
	                    BeanFactoryUtils.beanOfTypeIncludingAncestors(applicationContext, CrudRepository.class);
	            if (personRepository != null && personRepository.count() == 0) {
	                populate(personRepository);
	            }
	        }
	}

    @SuppressWarnings("unchecked")
    public void populate(CrudRepository repository) {
    	
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Url url : (Collection<Url>) entity) {
                if (url != null) {
                	System.out.println(url.getOriginURL());
                	AsyncNaverShortenURL shorten = new AsyncNaverShortenURL(url);
                	shorten.init();
                	try {
						shorten.run();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	ArrayList<Url> shortenURLs = new ArrayList<Url>(shorten.getOutput());
                	System.out.println(shortenURLs.size());
                	for(int i=0; i<shortenURLs.size(); i++) {
                		System.out.println(shortenURLs.get(i).getShortURL());
                	}
                    repository.save(shortenURLs);
                }
            }
        } else {
            repository.save(entity);
        }
    }

    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
