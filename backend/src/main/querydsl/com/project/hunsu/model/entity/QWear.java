package com.project.hunsu.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QWear is a Querydsl query type for Wear
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWear extends EntityPathBase<Wear> {

    private static final long serialVersionUID = 496558749L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWear wear = new QWear("wear");

    public final StringPath content = createString("content");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final BooleanPath isUpdated = createBoolean("isUpdated");

    public final StringPath title = createString("title");

    public final QUser user;

    public final DateTimePath<java.time.LocalDateTime> writeDate = createDateTime("writeDate", java.time.LocalDateTime.class);

    public QWear(String variable) {
        this(Wear.class, forVariable(variable), INITS);
    }

    public QWear(Path<? extends Wear> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWear(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWear(PathMetadata metadata, PathInits inits) {
        this(Wear.class, metadata, inits);
    }

    public QWear(Class<? extends Wear> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

