package fi.mika.infrastructure.persistence.dbcp;

import fi.mika.domain.book.Book;
import fi.mika.domain.book.BookRepository;
import fi.mika.domain.shared.TechnicalErrorException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class BookRepositoryDbcp implements BookRepository {
    private final DataSource ds;

    public BookRepositoryDbcp(DataSource ds) {
        this.ds = ds;
    }


    @Override
    public Optional<Book> find(long isbn13) throws TechnicalErrorException {
        try (Connection connection = ds.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(BookSql.find());
            statement.setLong(1, isbn13);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(mapBook(rs));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new TechnicalErrorException("Failed to search for a book", e);
        }
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setIsbn13(rs.getLong(1));
        book.setPages(rs.getInt(2));
        book.setTitle(rs.getString(3));
        return book;
    }
}
