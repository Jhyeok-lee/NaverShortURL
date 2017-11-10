package org.jaylee.controller;

import javax.validation.Valid;

import org.jaylee.domain.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class UrlController {

	private CrudRepository<Url, String> repository;

	@Autowired
	public UrlController(CrudRepository<Url, String> repository)
	{
		this.repository = repository;
	}

	@RequestMapping(method = RequestMethod.GET)
	public Iterable<Url> persons()
	{
		return repository.findAll();
	}

	@RequestMapping(method = RequestMethod.PUT)
    public Url add(@RequestBody @Valid Url url) {
        return repository.save(url);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Url update(@RequestBody @Valid Url url) {
        return repository.save(url);
    }

    @RequestMapping(value = "/{originURL:.+}", method = RequestMethod.GET)
    public Url getById(@PathVariable String originURL) {
        return repository.findOne(originURL);
    }

    @RequestMapping(value = "/{originURL:.+}", method = RequestMethod.DELETE)
    public void deleteById(@PathVariable String originURL) {
        repository.delete(originURL);
    }
}
