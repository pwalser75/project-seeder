package ${basePackage}.persistence.repository;

        import org.springframework.data.repository.CrudRepository;
        import ${basePackage}.persistence.entity.NoteEntity;

public interface NoteRepository extends CrudRepository<NoteEntity, Long> {
}
