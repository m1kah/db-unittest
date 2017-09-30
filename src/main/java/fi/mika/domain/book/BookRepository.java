package fi.mika.domain.book;

import fi.mika.domain.shared.TechnicalErrorException;

import java.util.Optional;

public interface BookRepository {
    Optional<Book> find(long isbn13) throws TechnicalErrorException;
}
