package org.demo.web;

import org.demo.domain.Question;
import org.demo.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

	private final QuestionRepository repository;

	@Autowired
	public QuestionController(QuestionRepository repository) {
		this.repository = repository;
	}

	@RequestMapping("/questions/{id}")
	public Question getQuestion(@PathVariable long id) {
		return repository.findOne(id);
	}
}
