package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class TodoRepositoryCustomImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .leftJoin(todo.user, user).fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne()
        );
    }

    @Override
    public org.springframework.data.domain.Page<org.example.expert.domain.todo.dto.response.TodoSearchResponse> searchTodos(
            String title, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate, String nickname, org.springframework.data.domain.Pageable pageable) {

        com.querydsl.core.types.dsl.BooleanExpression predicate = todo.isNotNull();

        if (title != null && !title.isBlank()) {
            predicate = predicate.and(todo.title.contains(title));
        }
        if (startDate != null) {
            predicate = predicate.and(todo.modifiedAt.goe(startDate));
        }
        if (endDate != null) {
            predicate = predicate.and(todo.modifiedAt.loe(endDate));
        }
        if (nickname != null && !nickname.isBlank()) {
            org.example.expert.domain.manager.entity.QManager subManager = new org.example.expert.domain.manager.entity.QManager("subManager");
            predicate = predicate.and(
                    todo.id.in(
                            com.querydsl.jpa.JPAExpressions.select(subManager.todo.id)
                                    .from(subManager)
                                    .join(subManager.user, user)
                                    .where(user.nickname.contains(nickname))
                    )
            );
        }

        org.example.expert.domain.manager.entity.QManager manager = org.example.expert.domain.manager.entity.QManager.manager;
        org.example.expert.domain.comment.entity.QComment comment = org.example.expert.domain.comment.entity.QComment.comment;

        java.util.List<org.example.expert.domain.todo.dto.response.TodoSearchResponse> content = queryFactory
                .select(com.querydsl.core.types.Projections.constructor(
                        org.example.expert.domain.todo.dto.response.TodoSearchResponse.class,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .where(predicate)
                .groupBy(todo.id, todo.title)
                .orderBy(todo.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(todo.count())
                .from(todo)
                .where(predicate)
                .fetchOne();

        return new org.springframework.data.domain.PageImpl<>(content, pageable, total);
    }
}
