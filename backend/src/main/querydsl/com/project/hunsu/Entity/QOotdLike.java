package com.project.hunsu.Entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOotdLike is a Querydsl query type for OotdLike
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOotdLike extends EntityPathBase<OotdLike> {

    private static final long serialVersionUID = -54778843L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOotdLike ootdLike = new QOotdLike("ootdLike");

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final QOotd ootd;

    public final QUser user;

    public QOotdLike(String variable) {
        this(OotdLike.class, forVariable(variable), INITS);
    }

    public QOotdLike(Path<? extends OotdLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOotdLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOotdLike(PathMetadata metadata, PathInits inits) {
        this(OotdLike.class, metadata, inits);
    }

    public QOotdLike(Class<? extends OotdLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ootd = inits.isInitialized("ootd") ? new QOotd(forProperty("ootd"), inits.get("ootd")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

