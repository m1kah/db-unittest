package fi.mika.infrastructure.persistence.dbcp;

import fi.mika.domain.book.Book;
import fi.mika.domain.book.BookRepository;
import fi.mika.domain.shared.TechnicalErrorException;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BookRepositoryDbcpTest {
    private static BasicDataSource ds;
    private BookRepository repository;

    @BeforeAll
    public static void initDatabase() throws Exception {
        initDataSource();
        initSchema();
        initTestData();
    }

    private static void initDataSource() {
        ds = new BasicDataSource();
        ds.setUrl("jdbc:h2:mem:test;mode=oracle");
    }

    private static void initSchema() throws Exception {
        executeSqlFile("db/create-tables.sql");
    }

    private static void initTestData() throws Exception {
        executeSqlFile("db/test-data.sql");
    }

    private static void executeSqlFile(String file) throws Exception {
        try (Connection connection = ds.getConnection()) {
            for (String sql : toStatements(loadSql(file))) {
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
            }
        }
    }

    private static String[] toStatements(String sql) {
        return sql.split(";");
    }

    private static String loadSql(String file) throws Exception {
        URI scriptUri = BookRepositoryDbcpTest.class
                .getClassLoader()
                .getResource(file).toURI();
        return new String(Files.readAllBytes(Paths.get(scriptUri)));
    }

    @BeforeEach
    void setup() {
        repository = new BookRepositoryDbcp(ds);
    }

    @Test
    public void test() throws TechnicalErrorException {
        Optional<Book> result = repository.find(9781501163401L);
        assertThat(result.isPresent(), is(true));
        Book book = result.get();
        assertThat(book.getIsbn13(), is(9781501163401L));
        assertThat(book.getPages(), is(720));
        assertThat(book.getTitle(), is("Sleeping Beauties: A Novel"));
    }
}
