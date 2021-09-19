package ${basePackage}.notes.persistence;

import org.springframework.data.repository.CrudRepository;

/**
 * Spring JPA repository for Notes.
 */
public interface NoteRepository extends CrudRepository<NoteEntity, Long> {
}