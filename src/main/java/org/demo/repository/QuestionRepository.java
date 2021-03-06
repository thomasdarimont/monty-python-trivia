package org.demo.repository;

import org.demo.domain.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionRepository extends PagingAndSortingRepository<Question,Long> {

	Iterable<Question> findByCategory (long categoryId);
	
}
