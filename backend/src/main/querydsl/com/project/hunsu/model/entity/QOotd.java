package com.project.hunsu.model.entity;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.*;

import javax.annotation.Generated;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;


/**
 * QOotd is a Querydsl query type for Ootd
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOotd extends EntityPathBase<Ootd> {

    private static final long serialVersionUID = 496330606L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOotd ootd = new QOotd("ootd");

    public final StringPath content = createString("content");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final ListPath<Hashtag, QHashtag> hashtagList = this.<Hashtag, QHashtag>createList("hashtagList", Hashtag.class, QHashtag.class, PathInits.DIRECT2);

    public final NumberPath<Long> idx = createNumber("idx", Long.class);

    public final BooleanPath isUpdated = createBoolean("isUpdated");

    public final QUser user;

    public final DateTimePath<java.time.LocalDateTime> writeDate = createDateTime("writeDate", java.time.LocalDateTime.class);

    public QOotd(String variable) {
        this(Ootd.class, forVariable(variable), INITS);
    }

    public QOotd(Path<? extends Ootd> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOotd(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOotd(PathMetadata metadata, PathInits inits) {
        this(Ootd.class, metadata, inits);
    }

    public QOotd(Class<? extends Ootd> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

