package fi.mika.infrastructure.persistence.dbcp;

final class BookSql {
    private BookSql() {
    }


    public static String find() {
        return "SELECT ISBN_13, PAGES, TITLE FROM BOOKS WHERE ISBN_13 = ?";
    }
}
