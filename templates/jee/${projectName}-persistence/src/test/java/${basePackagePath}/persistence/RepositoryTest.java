package ${basePackage}.persistence;

        import ${basePackage}.persistence.entity.NoteEntity;
        import ${basePackage}.persistence.repository.NoteRepository;
        import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
        import org.junit.Assert;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import javax.inject.Inject;

@RunWith(CdiTestRunner.class)
public class RepositoryTest {

    @Inject
    private NoteRepository repository;

    @Test
    public void testCRUD() {

        // create
        NoteEntity note = new NoteEntity();
        note.setText("Aloha");

        Assert.assertFalse(note.isPersistent());
        note = repository.save(note);
        Assert.assertTrue(note.isPersistent());
        Assert.assertNotNull(note.getId());

        // read
        note = repository.findById(note.getId()).orElseThrow(NoSuchElementException::new);
        Assert.assertEquals("Aloha", note.getText());

        // update
        note.setText("Lorem ipsum dolor sit amet");
        note = repository.save(note);

        // delete
        Assert.assertFalse(repository.existsById(note.getId()));
    }
}
