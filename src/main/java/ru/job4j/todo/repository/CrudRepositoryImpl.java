package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class CrudRepositoryImpl implements CrudRepository {
    private final SessionFactory sf;

    @Override
    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    @Override
    public boolean query(String query, Map<String, Object> args) {
        Function<Session, Boolean> command =
                session -> {
                    Query qr = session.createQuery(query);
                    args.keySet().forEach(key -> qr.setParameter(key, args.get(key)));
                    return qr.executeUpdate() > 0;
                };
        return tx(command);
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command =
                session -> {
                    Query<T> qr = session.createQuery(query, cl);
                    args.keySet().forEach(key -> qr.setParameter(key, args.get(key)));
                    return qr.list();
                };
        return tx(command);
    }

    @Override
    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command =
                session -> {
                    Query<T> qr = session.createQuery(query, cl);
                    return qr.list();
                };
        return tx(command);
    }

    @Override
    public <T> Optional<T> optional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command =
                session -> {
                    Query<T> qr = session.createQuery(query, cl);
                    args.keySet().forEach(key -> qr.setParameter(key, args.get(key)));
                    return qr.uniqueResultOptional();
                };
        return tx(command);
    }

    private <T> T tx(Function<Session, T> command) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }
}
